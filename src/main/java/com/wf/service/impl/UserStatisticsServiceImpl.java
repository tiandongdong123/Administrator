package com.wf.service.impl;

import com.google.protobuf.util.Timestamps;
import com.wanfangdata.grpcchannel.BindAccountChannel;
import com.wanfangdata.rpc.bindauthority.SearchBindDetailsRequest;
import com.wanfangdata.rpc.bindauthority.SearchBindDetailsResponse;
import com.wf.bean.WfksPayChannelResources;
import com.wf.bean.userStatistics.*;
import com.wf.dao.PersonMapper;
import com.wf.dao.UserStatisticsMapper;
import com.wf.dao.WfksPayChannelResourcesMapper;
import com.wf.service.UserStatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wfks.accounting.account.Account;
import wfks.accounting.account.AccountDao;
import wfks.authentication.AccountId;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
public class UserStatisticsServiceImpl implements UserStatisticsService {

    private static final Logger log = Logger.getLogger(UserStatisticsServiceImpl.class);
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

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

        UserStatistics userStatistics = personMapper.selectStatisticsByDate(dateTime);
        String today = dayFormat.format(new Date());
        try {
            //个人绑定机构新增个人账号
            SearchBindDetailsRequest countRequest = SearchBindDetailsRequest.newBuilder()
                    .setStartAddTime(Timestamps.fromMillis(dayFormat.parse(dateTime).getTime()))
                    .setEndAddTime(Timestamps.fromMillis(dayFormat.parse(today).getTime()))
                    .build();
            SearchBindDetailsResponse countResponse = bindAccountChannel.getBlockingStub().searchBindDetailsOrderUser(countRequest);
            int bindCount = countResponse.getTotalCount();
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
                    if (endDateTime.compareTo(dayFormat.parse(today)) == 1) {
                        if (payChannelResource.getPayChannelid().contains("Time")) {
                            vaildIntitutionAccountNumber++;
                            continue;
                        }
                        method = account.getClass().getMethod("getBalance");
                        BigDecimal balance = null;
                        if (method.invoke(account) instanceof BigDecimal) {
                            balance = (BigDecimal) method.invoke(account);
                        } else {
                            balance = new BigDecimal((Long) method.invoke(account));
                        }

                        if (balance != null && balance.compareTo(BigDecimal.ZERO) == 1) {
                            vaildIntitutionAccountNumber++;
                            continue;
                        }

                    }

                } catch (Exception e) {
                    log.error("获取指定资源账号失败，AccountId:" + accountId, e);
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
    public List<Integer> selectSingleTypeNewData(StatisticsParameter parameter) {

        return userStatisticsMapper.selectSingleTypeNewData(parameter);
    }


    /**
     * 按指标类型查询总数
     *
     * @param parameter 参数
     * @return
     */
    @Override
    public List<Integer> selectTotalDataByType(StatisticsParameter parameter) {
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
     * @param parameter 参数
     * @return
     */
    @Override
    public List<StatisticsModel> selectTotalData(StatisticsParameter parameter) {
        List<StatisticsModel> result = new ArrayList<>();

        UserStatisticsExample example = new UserStatisticsExample();
        UserStatisticsExample.Criteria criteria = example.createCriteria();
        if (parameter.getStartTime() != null && !"".equals(parameter.getStartTime())) {
            criteria.andDateLessThan(parameter.getStartTime());
        }


        //开始时间前的数据总和
        StatisticsModel previousData = userStatisticsMapper.selectSumByExample(example);
        int personUser = previousData.getPersonUser();
        int authenticatedUser = previousData.getAuthenticatedUser();
        int personBindInstitution = previousData.getPersonBindInstitution();
        int institution = previousData.getInstitution();
        int institutionAccount = previousData.getInstitutionAccount();
        int institutionAdmin = previousData.getInstitutionAdmin();


        int page = parameter.getPage() - 1;
        parameter.setPage(page);
        List<StatisticsModel> userStatisticsList = userStatisticsMapper.selectNewDate(parameter);
        //所有日期
        List<String> dateList = getDateList(parameter);

        //按周和按月时，有效机构账号显示当前时间单位的最后一天
        if (parameter.getTimeUnit() == 2 || parameter.getTimeUnit() == 3) {
            //每周/每月的最后一天
            List<String> lastDateList = lastDateOfWeekOrMonth(parameter.getTimeUnit(), parameter.getStartTime(), parameter.getEndTime());
            //设置有效时间
            for (int i = 0; i < lastDateList.size(); i++) {
                UserStatisticsExample vaildExample = new UserStatisticsExample();
                UserStatisticsExample.Criteria vaildCriteria = vaildExample.createCriteria();
                vaildCriteria.andDateEqualTo(lastDateList.get(i));
                List<UserStatistics> userStatistics = userStatisticsMapper.selectByExample(example);
                userStatisticsList.get(i).setValidInstitutionAccount(userStatistics.get(0).getValidInstitutionAccount());
            }
        }

        for (int i = 0; i < userStatisticsList.size(); i++) {
            StatisticsModel statisticsModel = new StatisticsModel();
            statisticsModel.setPersonUser(personUser + userStatisticsList.get(i).getPersonUser());
            statisticsModel.setAuthenticatedUser(authenticatedUser + userStatisticsList.get(i).getAuthenticatedUser());
            statisticsModel.setPersonBindInstitution(personBindInstitution + userStatisticsList.get(i).getPersonBindInstitution());
            statisticsModel.setInstitution(institution + userStatisticsList.get(i).getInstitution());
            statisticsModel.setInstitutionAccount(institutionAccount + userStatisticsList.get(i).getInstitutionAccount());
            statisticsModel.setInstitutionAdmin(institutionAdmin + userStatisticsList.get(i).getInstitutionAdmin());
            if (userStatisticsList.get(i).getValidInstitutionAccount()!=null){
                statisticsModel.setValidInstitutionAccount(userStatisticsList.get(i).getValidInstitutionAccount());
            }
            if (dateList.size()>=i+1){
                statisticsModel.setDate(dateList.get(i));
            }
            result.add(statisticsModel);

            personUser +=  userStatisticsList.get(i).getPersonUser();
            authenticatedUser += userStatisticsList.get(i).getAuthenticatedUser();
            personBindInstitution += userStatisticsList.get(i).getPersonBindInstitution();
            institution +=  userStatisticsList.get(i).getInstitution();
            institutionAccount +=  userStatisticsList.get(i).getInstitutionAccount();
            institutionAdmin += userStatisticsList.get(i).getInstitutionAdmin();
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
    public List<StatisticsModel> selectNewData(StatisticsParameter parameter) {
        int page = parameter.getPage() - 1;
        parameter.setPage(page);
        List<StatisticsModel> statisticsModel  =  userStatisticsMapper.selectNewDate(parameter);

        List<String> dateList = getDateList(parameter);
        for (int i = 0;i<=dateList.size();i++) {
            if (dateList.size()>=i+1){
                statisticsModel.get(i).setDate(dateList.get(i));
            }
        }
        return statisticsModel;
    }


    /**
     * 获取日期集合（按日/按周/按月）
     *
     * @param parameter 参数
     * @return
     */
    @Override
    public List<String> getDateList(StatisticsParameter parameter) {
        List<String> dateList = new ArrayList<>();
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = dayFormat.parse(parameter.getStartTime());
            endTime = dayFormat.parse(parameter.getEndTime());
        } catch (ParseException e) {
            log.error("时间转换失败", e);
        }

        if (parameter.getTimeUnit() != null) {
            switch (parameter.getTimeUnit()) {
                case 1: {
                    dateList.add(parameter.getStartTime());
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    start.setTime(startTime);
                    end.setTime(endTime);
                    start.add(Calendar.DATE, 1);

                    while (start.before(end)) {
                        dateList.add(dayFormat.format(start.getTime()));
                        start.add(Calendar.DATE, 1);
                    }
                    dateList.add(parameter.getEndTime());
                    break;
                }
                case 2: {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startTime);
                        String startDayOfWeek = dayFormat.format(startTime);
                        int whichDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        if (whichDay == 0) {
                            whichDay = 7;
                        }
                        calendar.add(Calendar.DATE, 7 - whichDay);
                        String endDayOfWeek = dayFormat.format(calendar.getTime());

                        while (dayFormat.parse(endDayOfWeek).compareTo(endTime) == -1) {
                            dateList.add(startDayOfWeek + "-" + endDayOfWeek);
                            calendar.add(Calendar.DATE, 1);
                            startDayOfWeek = dayFormat.format(calendar.getTime());
                            calendar.add(Calendar.DATE, 6);
                            endDayOfWeek = dayFormat.format(calendar.getTime());
                        }
                        dateList.add(startDayOfWeek + "-" + parameter.getEndTime());

                        break;
                    } catch (Exception e) {
                        log.error("时间转换失败", e);
                    }
                }
                case 3: {
                    Calendar min = Calendar.getInstance();
                    Calendar max = Calendar.getInstance();

                    try {
                        min.setTime(monthFormat.parse(parameter.getStartTime()));
                        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
                        max.setTime(monthFormat.parse(parameter.getEndTime()));
                        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
                        Calendar calendar = min;
                        while (calendar.before(max)) {
                            dateList.add(monthFormat.format(calendar.getTime()));
                            calendar.add(Calendar.MONTH, 1);
                        }
                        break;
                    } catch (ParseException e) {
                        log.error("时间转换失败", e);
                    }
                }
            }
        }
        return dateList;
    }


    @Override
    public List<String> lastDateOfWeekOrMonth(Integer timeUnit, String startTime, String endTime) {
        List<String> lastDateList = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
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
                    String endDayOfWeek = dayFormat.format(calendar.getTime());
                    while (dayFormat.parse(endDayOfWeek).compareTo(endDate) == -1) {
                        lastDateList.add(endDayOfWeek);
                    }
                    lastDateList.add(endDayOfWeek);

                    break;
                } catch (Exception e) {
                    log.error("时间转换失败", e);
                }
            }
            case 3: {
                Calendar min = Calendar.getInstance();
                Calendar max = Calendar.getInstance();

                try {
                    min.setTime(monthFormat.parse(startTime));
                    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
                    max.setTime(monthFormat.parse(endTime));
                    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
                    Calendar calendar = min;
                    while (calendar.before(max)) {
                        Calendar cal = Calendar.getInstance();
                        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        cal.set(Calendar.DAY_OF_MONTH, lastDay);
                        if (cal.getTime().compareTo(endDate) == -1) {
                            String lastDayOfMonth = dayFormat.format(cal.getTime());
                            lastDateList.add(lastDayOfMonth);
                        }
                        lastDateList.add(endTime);
                    }
                    break;
                } catch (ParseException e) {
                    log.error("时间转换失败", e);
                }

            }

        }
        return lastDateList;
    }
}
