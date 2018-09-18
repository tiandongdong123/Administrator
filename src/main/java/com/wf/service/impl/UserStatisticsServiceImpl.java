package com.wf.service.impl;

import com.google.protobuf.util.Timestamps;
import com.wanfangdata.grpcchannel.BindAccountChannel;
import com.wanfangdata.rpc.bindauthority.BindDetail;
import com.wanfangdata.rpc.bindauthority.SearchBindDetailsRequest;
import com.wanfangdata.rpc.bindauthority.SearchBindDetailsResponse;
import com.wf.bean.WfksPayChannelResources;
import com.wf.bean.userStatistics.*;
import com.wf.dao.PersonMapper;
import com.wf.dao.UserStatisticsMapper;
import com.wf.dao.WfksPayChannelResourcesMapper;
import com.wf.service.UserStatisticsService;
import com.xxl.conf.core.XxlConfClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfks.accounting.account.Account;
import wfks.accounting.account.AccountDao;
import wfks.authentication.AccountId;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


@Service
public class UserStatisticsServiceImpl implements UserStatisticsService {

    private static final Logger log = Logger.getLogger(UserStatisticsServiceImpl.class);


    private static final String DATEFORMAT = "yyyy-MM-dd";
    private static final String MONTHFORMAT = "yyyy-MM";


    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

    public static DateFormat getDayFormat() {
        DateFormat dayFormat = threadLocal.get();
        if (dayFormat == null) {
            dayFormat = new SimpleDateFormat(DATEFORMAT);
            threadLocal.set(dayFormat);
        }
        return dayFormat;
    }

    public static DateFormat getMonthFormat() {
        DateFormat monthFormat = threadLocal.get();
        if (monthFormat == null) {
            monthFormat = new SimpleDateFormat(MONTHFORMAT);
            threadLocal.set(monthFormat);
        }
        return monthFormat;
    }

    //倒序排列
    private static final int DESC = 1;


    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private UserStatisticsMapper userStatisticsMapper;
    @Autowired
    private BindAccountChannel bindAccountChannel;
    @Autowired
    private WfksPayChannelResourcesMapper payChannelResourcesMapper;
    @Autowired
    private AccountDao accountDao;


    @Override
    public UserStatistics selectStatisticsByDate(String dateTime) {
        Connection conn = null;
        PreparedStatement sumPs = null;
        ResultSet sumResultSet = null;
        UserStatistics userStatistics = new UserStatistics();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String URL = XxlConfClient.get("wf-public.jdbc.adminManager.url", null);
            String USERNAME = XxlConfClient.get("wf-public.jdbc.username", null);
            String PASSWORD = XxlConfClient.get("wf-public.jdbc.password", null);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sumSql = "select ifnull(sum(userType = 0),0),ifnull(sum(user_roles = 0),0),ifnull(sum(user_roles = 1),0),ifnull(sum(user_roles = 2),0), " +
                    "ifnull(sum(userType in(2,3)),0),ifnull(sum(userType = 3),0),ifnull(sum(userType = 3 and extend = 'Check_Regist_BDStudentSub'),0)," +
                    "ifnull(sum(userType = 1),0) from `user` where DATE_FORMAT(registration_time,'%Y-%m-%d') = ?";
            sumPs = conn.prepareStatement(sumSql);
            sumPs.setString(1, dateTime);
            sumResultSet = sumPs.executeQuery();
            sumResultSet.next();
            userStatistics.setPersonUser(sumResultSet.getInt(1));
            userStatistics.setOrdinaryUser(sumResultSet.getInt(2));
            userStatistics.setAuthenticatedUser(sumResultSet.getInt(3));
            userStatistics.setScholarUser(sumResultSet.getInt(4));
            userStatistics.setInstitutionAccount(sumResultSet.getInt(5));
            userStatistics.setInstitutionSubaccount(sumResultSet.getInt(6));
            userStatistics.setStudentaccount(sumResultSet.getInt(6));
            userStatistics.setInstitutionSubaccount(sumResultSet.getInt(7));
            userStatistics.setInstitutionAdmin(sumResultSet.getInt(8));


        } catch (Exception e) {
            log.error("查询统计数量总和失败 查询日期：" + dateTime);
        } finally {
            close(sumResultSet, sumPs, conn);
        }

        String today = getDayFormat().format(new Date());
        try {
            //个人绑定机构新增个人账号
            SearchBindDetailsRequest countRequest = SearchBindDetailsRequest.newBuilder()
                    .setStartAddTime(Timestamps.fromMillis(getDayFormat().parse(dateTime).getTime()))
                    .setEndAddTime(Timestamps.fromMillis(getDayFormat().parse(today).getTime()))
                    .build();
            SearchBindDetailsResponse countResponse = bindAccountChannel.getBlockingStub().searchBindDetailsOrderUser(countRequest);
            int bindCount = 0;
            //因每个个人账号的一条权限作为一条记录，所有一个账号可能会有多条记录。
            //新增的权限所对应的个人账号可能以前就开通过其他权限，要排除已存在的个人账号
            if (countResponse.getTotalCount() == 0) {
                bindCount = countResponse.getTotalCount();
            } else {
                //查询已存在的个人账号数量
                List<BindDetail> bindDetailList = countResponse.getItemsList();
                List<com.wanfangdata.rpc.bindauthority.AccountId> userIdList = new ArrayList<>();
                for (BindDetail bindDetail : bindDetailList) {
                    com.wanfangdata.rpc.bindauthority.AccountId accountId = com.wanfangdata.rpc.bindauthority.AccountId.newBuilder()
                            .setAccounttype("Person")
                            .setKey(bindDetail.getUser().getKey())
                            .build();
                    userIdList.add(accountId);
                }
                Date now = getDayFormat().parse(dateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                calendar.add(Calendar.DATE, -1);
                SearchBindDetailsRequest existedRequest = SearchBindDetailsRequest.newBuilder()
                        .addAllUser(userIdList)
                        .setEndAddTime(Timestamps.fromMillis(calendar.getTime().getTime()))
                        .build();
                SearchBindDetailsResponse existedResponse = bindAccountChannel.getBlockingStub().searchBindDetailsOrderUser(existedRequest);
                bindCount = countResponse.getTotalCount() - existedResponse.getTotalCount();
            }

            userStatistics.setPersonBindInstitution(bindCount);

        } catch (Exception e) {
            log.error("查询个人绑定机构新增数量失败，时间：" + dateTime);
        }

        Integer institutionCount = personMapper.getInstitutionCount(dateTime);


        if (institutionCount != 0) {
            Integer existedCount = personMapper.getExistedInstitutionCountByDate(dateTime);
            if (existedCount != 0) {
                institutionCount = institutionCount - existedCount;
            }
        }
        userStatistics.setInstitution(institutionCount);

        List<String> unFreezeInstitutionAccountList = personMapper.selectunFreezeInstitutionAccount();
        int vaildIntitutionAccountNumber = 0;

        for (String unFreezeInstitutionAccount : unFreezeInstitutionAccountList) {
            List<WfksPayChannelResources> payChannelResourcesList = payChannelResourcesMapper.selectByUserId(unFreezeInstitutionAccount);
            if (payChannelResourcesList == null || payChannelResourcesList.size() < 1) {
                continue;
            }
            //是否添加机构账号数量
            boolean pidState = false;
            //是否添加机构自账号数量
            boolean subAccountState = false;

            for (WfksPayChannelResources payChannelResource : payChannelResourcesList) {
                AccountId accountId = new AccountId();
                accountId.setAccountType(payChannelResource.getPayChannelid());
                accountId.setKey(unFreezeInstitutionAccount);
                Account account = null;
                try {
                    account = accountDao.get(accountId, null);
                    if (account == null) {
                        continue;
                    }

                    Method method = account.getClass().getMethod("getEndDateTime");
                    if (method == null) {
                        continue;
                    }

                    Date endDateTime = (Date) method.invoke(account);
                    if (endDateTime.compareTo(getDayFormat().parse(today)) != 1) {
                        continue;
                    }


                    if (payChannelResource.getPayChannelid().contains("Time")) {
                        pidState = true;
                        //不是资源限时，不添加子账号的数量
                        if (!payChannelResource.getPayChannelid().equals("GTimeLimit")) {
                            continue;
                        }
                        subAccountState = true;
                        continue;
                    }

                    method = account.getClass().getMethod("getBalance");
                    BigDecimal balance = null;
                    if (method.invoke(account) instanceof BigDecimal) {
                        balance = (BigDecimal) method.invoke(account);
                    } else {
                        balance = new BigDecimal((Long) method.invoke(account));
                    }

                    if (balance == null || balance.compareTo(BigDecimal.ZERO) != 1) {
                        continue;
                    }
                    pidState = true;
                    if (payChannelResource.getPayChannelid().equals("GBalanceLimit")
                            || payChannelResource.getPayChannelid().equals("GNstlBalanceLimit")
                            || payChannelResource.getPayChannelid().equals("IsticBalanceLimit")) {
                        subAccountState = true;
                    }

                } catch (Exception e) {
                    log.error("获取指定资源账号失败，AccountId:" + accountId, e);
                }
            }

            if (pidState) {
                vaildIntitutionAccountNumber++;
            }
            if (subAccountState) {
                List<String> subAccount = personMapper.getSubaccount(unFreezeInstitutionAccount);
                if (subAccount.size() > 0) {
                    vaildIntitutionAccountNumber += subAccount.size();
                }
            }
        }
        userStatistics.setValidInstitutionAccount(vaildIntitutionAccountNumber);
        userStatistics.setDate(dateTime);
        return userStatistics;
    }

    @Override
    public int insert(UserStatistics userStatistics) {
        return userStatisticsMapper.insert(userStatistics);
    }

    @Override
    public List<Integer> selectSingleTypeNewData(ChartsParameter parameter) {

        return userStatisticsMapper.selectSingleTypeNewData(parameter);
    }


    /**
     * 按指标类型查询总数
     *
     * @param parameter 参数
     * @return
     */
    @Override
    public List<Integer> selectTotalDataByType(ChartsParameter parameter) {
        List<Integer> result = new ArrayList<>();
        int previousSum = userStatisticsMapper.selectSingleTypePreviousSum(parameter.getType(), parameter.getStartTime());
        List<Integer> newDataList = userStatisticsMapper.selectSingleTypeNewData(parameter);
        for (Integer newData : newDataList) {
            previousSum += newData;
            result.add(previousSum);
        }
        return result;
    }

    /**
     * 查询所有指标总数
     *
     * @param request 参数
     * @return
     */
    @Override
    public List<TableResponse> selectTotalDataForTable(StatisticsRequest request) {

        List<TableResponse> result = new ArrayList<>();
        if (request.getCompareStartTime() == null || request.getCompareStartTime() == "" || request.getCompareEndTime() == null ||
                request.getCompareEndTime() == "") {
            TableParameter parameter = new TableParameter();
            parameter.setStartTime(request.getStartTime());
            parameter.setEndTime(request.getEndTime());
            parameter.setTimeUnit(request.getTimeUnit());
            parameter.setPage(request.getPage());
            parameter.setPageSize(request.getPageSize());
            parameter.setSort(request.getSort());
            result = ordinaryTotalData(parameter);
        } else {
            result = compareTotalData(request);
        }
        return result;
    }

    @Override
    public StatisticsModel selectSumByExample(UserStatisticsExample example) {

        return userStatisticsMapper.selectSumByExample(example);
    }

    @Override
    public List<UserStatistics> selectByExample(UserStatisticsExample example) {

        return userStatisticsMapper.selectByExample(example);
    }


    @Override
    public List<TableResponse> selectNewDataForTable(StatisticsRequest request) {

        List<TableResponse> result = new ArrayList<>();
        if (request.getCompareStartTime() == null || request.getCompareStartTime() == "" || request.getCompareEndTime() == null ||
                request.getCompareEndTime() == "") {
            TableParameter parameter = new TableParameter();
            parameter.setStartTime(request.getStartTime());
            parameter.setEndTime(request.getEndTime());
            parameter.setTimeUnit(request.getTimeUnit());
            parameter.setPage(request.getPage());
            parameter.setPageSize(request.getPageSize());
            parameter.setSort(request.getSort());
            result = ordinaryNewData(parameter);
        } else {
            result = comparaNewData(request);
        }
        return result;
    }


    /**
     * 获取日期集合（按日/按周/按月）
     *
     * @param timeUnit  时间单位
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @Override
    public List<String> getDateList(Integer timeUnit, String startTime, String endTime) {
        List<String> dateList = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = getDayFormat().parse(startTime);
            endDate = getDayFormat().parse(endTime);
        } catch (ParseException e) {
            log.error("时间转换失败", e);
        }

        if (timeUnit != null) {
            switch (timeUnit) {
                case 1: {
                    if (startDate.compareTo(endDate) == 0) {
                        dateList.add(startTime);
                        break;
                    }
                    dateList.add(startTime);
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    start.setTime(startDate);
                    end.setTime(endDate);
                    start.add(Calendar.DATE, 1);

                    while (start.before(end)) {
                        dateList.add(getDayFormat().format(start.getTime()));
                        start.add(Calendar.DATE, 1);
                    }
                    dateList.add(endTime);
                    break;
                }
                case 2: {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startDate);
                        String startDayOfWeek = getDayFormat().format(startDate);
                        int whichDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        if (whichDay == 0) {
                            whichDay = 7;
                        }
                        calendar.add(Calendar.DATE, 7 - whichDay);
                        String endDayOfWeek = getDayFormat().format(calendar.getTime());

                        while (getDayFormat().parse(endDayOfWeek).compareTo(endDate) == -1) {
                            dateList.add(startDayOfWeek + "-" + endDayOfWeek);
                            calendar.add(Calendar.DATE, 1);
                            startDayOfWeek = getDayFormat().format(calendar.getTime());
                            calendar.add(Calendar.DATE, 6);
                            endDayOfWeek = getDayFormat().format(calendar.getTime());
                        }
                        dateList.add(startDayOfWeek + "-" + endTime);

                        break;
                    } catch (Exception e) {
                        log.error("时间转换失败", e);
                    }
                }
                case 3: {
                    Calendar min = Calendar.getInstance();
                    Calendar max = Calendar.getInstance();

                    try {
                        min.setTime(getMonthFormat().parse(startTime));
                        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
                        max.setTime(getMonthFormat().parse(endTime));
                        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
                        Calendar calendar = min;
                        String startDayOfMonth = startTime;
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        String endDayOfMonth = getDayFormat().format(calendar.getTime());
                        while (getDayFormat().parse(endDayOfMonth).compareTo(endDate) == -1) {
                            dateList.add(startDayOfMonth + "-" + endDayOfMonth);
                            calendar.add(Calendar.DATE, 1);
                            startDayOfMonth = getDayFormat().format(calendar.getTime());
                            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                            endDayOfMonth = getDayFormat().format(calendar.getTime());
                        }
                        dateList.add(startDayOfMonth + "-" + endTime);
                        break;
                    } catch (ParseException e) {
                        log.error("时间转换失败", e);
                    }
                }
            }
        }
        return dateList;
    }

    /**
     * 每周/每月的最后一天的集合
     *
     * @param timeUnit
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<String> lastDateOfWeekOrMonth(Integer timeUnit, String startTime, String endTime) {
        List<String> lastDateList = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = getDayFormat().parse(startTime);
            endDate = getDayFormat().parse(endTime);

        } catch (ParseException e) {
            log.error("时间转换失败", e);
        }

        switch (timeUnit) {
            case 2: {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    int whichDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    if (whichDay == 0) {
                        whichDay = 7;
                    }
                    calendar.add(Calendar.DATE, 7 - whichDay);
                    String endDayOfWeek = getDayFormat().format(calendar.getTime());
                    while (getDayFormat().parse(endDayOfWeek).compareTo(endDate) == -1) {
                        lastDateList.add(endDayOfWeek);
                        calendar.add(Calendar.DATE, 7);
                        endDayOfWeek = getDayFormat().format(calendar.getTime());

                    }
                    lastDateList.add(endTime);

                    break;
                } catch (Exception e) {
                    log.error("时间转换失败", e);
                }
            }
            case 3: {
                Calendar min = Calendar.getInstance();
                Calendar max = Calendar.getInstance();

                try {
                    min.setTime(getMonthFormat().parse(startTime));
                    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
                    max.setTime(getMonthFormat().parse(endTime));
                    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
                    Calendar calendar = min;
                    while (calendar.before(max)) {
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        if (calendar.getTime().compareTo(endDate) == -1) {
                            String lastDayOfMonth = getDayFormat().format(calendar.getTime());
                            lastDateList.add(lastDayOfMonth);
                        }
                        calendar.add(Calendar.MONTH, 1);
                    }
                    lastDateList.add(endTime);
                    break;
                } catch (ParseException e) {
                    log.error("时间转换失败", e);
                }
            }

        }
        return lastDateList;
    }

    /**
     * 查询总数表格数据
     *
     * @param parameter
     * @return
     */
    public List<TableResponse> ordinaryTotalData(TableParameter parameter) {

        List<TableResponse> result = new ArrayList<>();

        try {
            //分页数量
            int page = parameter.getPage();
            int offset = (parameter.getPage() - 1) * parameter.getPageSize();
            parameter.setPage(offset);

            //分页日期
            List<String> pagingDateList = new ArrayList<>();
            List<String> dateList = getDateList(parameter.getTimeUnit(), parameter.getStartTime(), parameter.getEndTime());
            if (parameter.getSort() == DESC) {
                Collections.reverse(dateList);
            }
            if (dateList.size() > page * parameter.getPageSize()) {
                pagingDateList = dateList.subList(offset, page * parameter.getPageSize());
            } else {
                pagingDateList = dateList.subList(offset, dateList.size());
            }

            if (parameter.getSort() == DESC) {
                Collections.reverse(pagingDateList);
            }

            //开始时间前的数据总和
            UserStatisticsExample example = new UserStatisticsExample();
            UserStatisticsExample.Criteria criteria = example.createCriteria();
            criteria.andDateLessThan(pagingDateList.get(0));
            StatisticsModel previousData = userStatisticsMapper.selectSumByExample(example);
            int personUser = previousData.getPersonUser();
            int authenticatedUser = previousData.getAuthenticatedUser();
            int personBindInstitution = previousData.getPersonBindInstitution();
            int institution = previousData.getInstitution();
            int institutionAccount = previousData.getInstitutionAccount();
            int institutionAdmin = previousData.getInstitutionAdmin();


            parameter.setStartTime(pagingDateList.get(0));
            parameter.setStartTime(pagingDateList.get(pagingDateList.size() - 1));
            List<StatisticsModel> userStatisticsList = userStatisticsMapper.selectNewDate(pagingDateList.get(0), pagingDateList.get(pagingDateList.size() - 1), parameter.getTimeUnit());

            List<TableResponse> responseList = new ArrayList<>();
            for (int i = 0; i < userStatisticsList.size(); i++) {
                TableResponse tableResponse = new TableResponse();
                tableResponse.setPersonUser(String.valueOf(personUser + userStatisticsList.get(i).getPersonUser()));
                tableResponse.setAuthenticatedUser(String.valueOf(authenticatedUser + userStatisticsList.get(i).getAuthenticatedUser()));
                tableResponse.setPersonBindInstitution(String.valueOf(personBindInstitution + userStatisticsList.get(i).getPersonBindInstitution()));
                tableResponse.setInstitution(String.valueOf(institution + userStatisticsList.get(i).getInstitution()));
                tableResponse.setInstitutionAccount(String.valueOf(institutionAccount + userStatisticsList.get(i).getInstitutionAccount()));
                tableResponse.setInstitutionAdmin(String.valueOf(institutionAdmin + userStatisticsList.get(i).getInstitutionAdmin()));
                if (userStatisticsList.get(i).getValidInstitutionAccount() != null) {
                    tableResponse.setValidInstitutionAccount(String.valueOf(userStatisticsList.get(i).getValidInstitutionAccount()));
                }
                tableResponse.setDate(userStatisticsList.get(i).getDate());
                responseList.add(tableResponse);
                personUser += userStatisticsList.get(i).getPersonUser();
                authenticatedUser += userStatisticsList.get(i).getAuthenticatedUser();
                personBindInstitution += userStatisticsList.get(i).getPersonBindInstitution();
                institution += userStatisticsList.get(i).getInstitution();
                institutionAccount += userStatisticsList.get(i).getInstitutionAccount();
                institutionAdmin += userStatisticsList.get(i).getInstitutionAdmin();
            }


            if (parameter.getSort() == DESC) {
                Collections.reverse(pagingDateList);
            }

            date:
            for (String date : pagingDateList) {
                response:
                for (TableResponse response : responseList) {
                    if (response.getDate().equals(date)) {
                        result.add(response);
                        continue date;
                    }
                }
                TableResponse tableResponse = new TableResponse();
                tableResponse.setPersonUser("-");
                tableResponse.setAuthenticatedUser("-");
                tableResponse.setPersonBindInstitution("-");
                tableResponse.setInstitution("-");
                tableResponse.setInstitutionAccount("-");
                tableResponse.setInstitutionAdmin("-");
                tableResponse.setValidInstitutionAccount("-");
                tableResponse.setDate(date);
                result.add(tableResponse);
            }
        } catch (Exception e) {
            log.error("查询总数表格失败，parameter:" + parameter);
        }

        return result;
    }

    /**
     * 对比时间后总数表格数据
     *
     * @return
     */

    public List<TableResponse> compareTotalData(StatisticsRequest request) {
        List<TableResponse> result = new ArrayList<>();
        if (request.getCompareStartTime() == null || request.getCompareEndTime() == null) {
            log.error("对比开始时间或结束时间为空，request：" + request);
            return new ArrayList<>();
        }

        try {
            TableParameter parameter = new TableParameter();
            parameter.setStartTime(request.getStartTime());
            parameter.setEndTime(request.getEndTime());
            parameter.setTimeUnit(request.getTimeUnit());
            parameter.setPage(request.getPage());
            parameter.setPageSize(request.getPageSize());
            parameter.setSort(request.getSort());
            List<TableResponse> ordinaryData = ordinaryTotalData(parameter);
            TableParameter compareParameter = new TableParameter();
            compareParameter.setStartTime(request.getCompareStartTime());
            compareParameter.setEndTime(request.getCompareEndTime());
            compareParameter.setTimeUnit(request.getTimeUnit());
            compareParameter.setPage(request.getPage());
            compareParameter.setPageSize(request.getPageSize());
            compareParameter.setSort(request.getSort());
            List<TableResponse> compareData = ordinaryTotalData(compareParameter);

            for (int i = 0; i < ordinaryData.size(); i++) {
                TableResponse tableResponse = new TableResponse();
                tableResponse.setDate(ordinaryData.get(i).getDate() + "</br></br>" + compareData.get(i).getDate());
                tableResponse.setPersonUser(ordinaryData.get(i).getPersonUser() + "</br></br>" + compareData.get(i).getPersonUser());
                tableResponse.setAuthenticatedUser(ordinaryData.get(i).getAuthenticatedUser() + "</br></br>" + compareData.get(i).getAuthenticatedUser());
                tableResponse.setPersonBindInstitution(ordinaryData.get(i).getPersonBindInstitution() + "</br></br>" + compareData.get(i).getPersonBindInstitution());
                tableResponse.setInstitution(ordinaryData.get(i).getInstitution() + "</br></br>" + compareData.get(i).getInstitution());
                tableResponse.setInstitutionAccount(ordinaryData.get(i).getInstitutionAccount() + "</br></br>" + compareData.get(i).getInstitutionAccount());
                tableResponse.setValidInstitutionAccount(ordinaryData.get(i).getValidInstitutionAccount() + "</br></br>" + compareData.get(i).getValidInstitutionAccount());
                tableResponse.setInstitutionAdmin(ordinaryData.get(i).getInstitutionAdmin() + "</br></br>" + compareData.get(i).getInstitutionAdmin());
                result.add(tableResponse);
            }
        } catch (Exception e) {
            log.error("选择对比时间后查询总数表格失败，request：" + request);
        }

        return result;

    }

    /**
     * 新增普通选择时表格数据
     *
     * @param parameter
     * @return
     */
    public List<TableResponse> ordinaryNewData(TableParameter parameter) {

        List<TableResponse> result = new ArrayList<>();

        try {
            int page = parameter.getPage();
            int offset = (parameter.getPage() - 1) * parameter.getPageSize();

            //分页日期
            List<String> pagingDateList = new ArrayList<>();
            List<String> dateList = getDateList(parameter.getTimeUnit(), parameter.getStartTime(), parameter.getEndTime());
            if (parameter.getSort() == DESC) {
                Collections.reverse(dateList);
            }
            if (dateList.size() > page * parameter.getPageSize()) {
                pagingDateList = dateList.subList(offset, page * parameter.getPageSize());
            } else {
                pagingDateList = dateList.subList(offset, dateList.size());
            }

            if (parameter.getSort() == DESC) {
                Collections.reverse(pagingDateList);
            }

            parameter.setStartTime(pagingDateList.get(0));
            parameter.setStartTime(pagingDateList.get(pagingDateList.size() - 1));
            List<StatisticsModel> userStatisticsList = userStatisticsMapper.selectNewDate(pagingDateList.get(0), pagingDateList.get(pagingDateList.size() - 1), parameter.getTimeUnit());


            List<TableResponse> responseList = new ArrayList<>();
            for (int i = 0; i < userStatisticsList.size(); i++) {
                TableResponse tableResponse = new TableResponse();
                tableResponse.setPersonUser(String.valueOf(userStatisticsList.get(i).getPersonUser()));
                tableResponse.setAuthenticatedUser(String.valueOf(userStatisticsList.get(i).getAuthenticatedUser()));
                tableResponse.setPersonBindInstitution(String.valueOf(userStatisticsList.get(i).getPersonBindInstitution()));
                tableResponse.setInstitution(String.valueOf(userStatisticsList.get(i).getInstitution()));
                tableResponse.setInstitutionAccount(String.valueOf(userStatisticsList.get(i).getInstitutionAccount()));
                tableResponse.setInstitutionAdmin(String.valueOf(userStatisticsList.get(i).getInstitutionAdmin()));
                tableResponse.setDate(userStatisticsList.get(i).getDate());
                responseList.add(tableResponse);
            }


            if (parameter.getSort() == DESC) {
                Collections.reverse(pagingDateList);
            }
            date:
            for (String date : pagingDateList) {
                response:
                for (TableResponse response : responseList) {
                    if (response.getDate().equals(date)) {
                        result.add(response);
                        continue date;
                    }
                }
                TableResponse tableResponse = new TableResponse();
                tableResponse.setPersonUser("-");
                tableResponse.setAuthenticatedUser("-");
                tableResponse.setPersonBindInstitution("-");
                tableResponse.setInstitution("-");
                tableResponse.setInstitutionAccount("-");
                tableResponse.setInstitutionAdmin("-");
                tableResponse.setValidInstitutionAccount("-");
                tableResponse.setDate(date);
                result.add(tableResponse);
            }

        } catch (Exception e) {
            log.error("查询新增表格失败，parameter：" + parameter);
        }

        return result;
    }

    /**
     * 对比新增表格数据
     *
     * @param request
     * @return
     */
    public List<TableResponse> comparaNewData(StatisticsRequest request) {
        List<TableResponse> result = new ArrayList<>();
        if (request.getCompareStartTime() == null || request.getCompareEndTime() == null) {
            log.error("对比开始时间或结束时间为空，request：" + request);
            return new ArrayList<>();
        }
        try {
            TableParameter parameter = new TableParameter();
            parameter.setStartTime(request.getStartTime());
            parameter.setEndTime(request.getEndTime());
            parameter.setTimeUnit(request.getTimeUnit());
            parameter.setPage(request.getPage());
            parameter.setPageSize(request.getPageSize());
            parameter.setSort(request.getSort());
            List<TableResponse> ordinaryData = ordinaryNewData(parameter);
            TableParameter compareParameter = new TableParameter();
            compareParameter.setStartTime(request.getCompareStartTime());
            compareParameter.setEndTime(request.getCompareEndTime());
            compareParameter.setTimeUnit(request.getTimeUnit());
            compareParameter.setPage(request.getPage());
            compareParameter.setPageSize(request.getPageSize());
            compareParameter.setSort(request.getSort());
            List<TableResponse> compareData = ordinaryNewData(compareParameter);


            for (int i = 0; i < ordinaryData.size(); i++) {
                TableResponse tableResponse = new TableResponse();
                tableResponse.setDate(ordinaryData.get(i).getDate() + "</br></br>" + compareData.get(i).getDate());
                tableResponse.setPersonUser(ordinaryData.get(i).getPersonUser() + "</br></br>" + compareData.get(i).getPersonUser());
                tableResponse.setAuthenticatedUser(ordinaryData.get(i).getAuthenticatedUser() + "</br></br>" + compareData.get(i).getAuthenticatedUser());
                tableResponse.setPersonBindInstitution(ordinaryData.get(i).getPersonBindInstitution() + "</br></br>" + compareData.get(i).getPersonBindInstitution());
                tableResponse.setInstitution(ordinaryData.get(i).getInstitution() + "</br></br>" + compareData.get(i).getInstitution());
                tableResponse.setInstitutionAccount(ordinaryData.get(i).getInstitutionAccount() + "</br></br>" + compareData.get(i).getInstitutionAccount());
                tableResponse.setValidInstitutionAccount(ordinaryData.get(i).getValidInstitutionAccount() + "</br></br>" + compareData.get(i).getValidInstitutionAccount());
                tableResponse.setInstitutionAdmin(ordinaryData.get(i).getInstitutionAdmin() + "</br></br>" + compareData.get(i).getInstitutionAdmin());
                result.add(tableResponse);
            }
        } catch (Exception e) {
            log.error("选择对比时间后查询新增表格失败，request：" + request);
        }
        return result;
    }

    /**
     * 关闭数据库连接
     *
     * @param rs
     * @param stmt
     * @param conn
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }
}
