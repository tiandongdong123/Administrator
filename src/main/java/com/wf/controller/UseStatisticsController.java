package com.wf.controller;


import com.wanfangdata.model.PagerModel;
import com.wf.bean.userStatistics.*;
import com.wf.service.UserStatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("userStatistics")
public class UseStatisticsController {

    private static final Logger log = Logger.getLogger(UseStatisticsController.class);

    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

    @Autowired
    private UserStatisticsService userStatisticsService;

    /**
     * 跳转
     *
     * @return
     */
    @RequestMapping("statisticsInfo")
    public ModelAndView statisticsInformation() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/user_statistics");
        return view;
    }


    /**
     * 总数折线图
     *
     * @param parameter 参数
     * @return
     */
    @RequestMapping("totalCharts")
    @ResponseBody
    public Map<String, List> totalCharts(@Valid StatisticsParameter parameter) {

        Map<String, List> result = new HashMap<>();
        try {
            List<Integer> totalData = selectTotalDataByType(parameter);
            List<String> dateTime = getDateList(parameter);
            result.put("totalData", totalData);
            result.put("dateTime", dateTime);

        } catch (Exception e) {
            log.error("获取总数折线图失败，parameter：" + parameter.toString(), e);
        }
        return result;
    }

    /**
     * 新增折线图
     *
     * @param parameter 参数
     * @return
     */
    @RequestMapping("newCharts")
    @ResponseBody
    public Map<String, List> newCharts(@Valid StatisticsParameter parameter) {
        Map<String, List> result = new HashMap<>();
        try {
            List<Integer> newDataList = userStatisticsService.selectSingleTypeNewData(parameter);
            List<String> dateTime = getDateList(parameter);
            result.put("totalData", newDataList);
            result.put("dateTime", dateTime);

        } catch (Exception e) {
            log.error("获取总数折线图失败，parameter：" + parameter.toString(), e);
        }
        return result;
    }

    /**
     * 总数对比折线图
     *
     * @param parameter        参数
     * @param compareStartTime 对比开始时间
     * @param compareEndTime   对比结束时间
     * @return
     */
    @RequestMapping("compareTotalCharts")
    @ResponseBody
    public Map<String, List> compareTotalCharts(@Valid StatisticsParameter parameter,
                                                @RequestParam(value = "compareStartTime", required = true) String compareStartTime,
                                                @RequestParam(value = "compareEndTime", required = true) String compareEndTime) {
        Map<String, List> result = new HashMap<>();
        List<Integer> selectData = new ArrayList<>();
        List<Integer> compareData = new ArrayList<>();
        try {
            selectData = selectTotalDataByType(parameter);
            StatisticsParameter compareParameter = new StatisticsParameter();
            compareParameter.setStartTime(compareStartTime);
            compareParameter.setEndTime(compareEndTime);
            compareParameter.setType(parameter.getType());
            compareParameter.setTimeUnit(parameter.getTimeUnit());
            compareData = selectTotalDataByType(compareParameter);
            result.put("selectData", selectData);
            result.put("compareData", compareData);
            List<String> dateTime = getDateList(parameter);
            result.put("dateTime", dateTime);
        } catch (Exception e) {
            log.error("获取总数折线图失败，parameter：" + parameter.toString(), e);
        }
        return result;

    }

    /**
     * 新增对比折线图
     *
     * @param parameter        参数
     * @param compareStartTime 对比开始时间
     * @param compareEndTime   对比结束时间
     * @return
     */
    @RequestMapping("compareNewCharts")
    @ResponseBody
    public Map<String, List> compareNewCharts(@Valid StatisticsParameter parameter,
                                              @RequestParam(value = "compareStartTime", required = true) String compareStartTime,
                                              @RequestParam(value = "compareEndTime", required = true) String compareEndTime) {
        Map<String, List> result = new HashMap<>();
        List<Integer> selectData = new ArrayList<>();
        List<Integer> compareData = new ArrayList<>();
        try {
            selectData = userStatisticsService.selectSingleTypeNewData(parameter);
            StatisticsParameter compareParameter = new StatisticsParameter();
            compareParameter.setStartTime(compareStartTime);
            compareParameter.setEndTime(compareEndTime);
            compareParameter.setType(parameter.getType());
            compareParameter.setTimeUnit(parameter.getTimeUnit());
            compareData = userStatisticsService.selectSingleTypeNewData(compareParameter);
            List<String> dateTime = getDateList(parameter);
            result.put("selectData", selectData);
            result.put("compareData", compareData);
            result.put("dateTime", dateTime);
        } catch (Exception e) {
            log.error("获取总数折线图失败，parameter：" + parameter.toString(), e);
        }
        return result;

    }


    /**
     * 总数列表
     *
     * @param parameter 参数
     * @return
     */
    @RequestMapping("totalDatasheets")
    @ResponseBody
    public String totalDatasheets(@Valid StatisticsParameter parameter, Model model) {
        List<StatisticsModel> modelList = new ArrayList<>();
        try {
            if (parameter.getPageSize() == 0) {
                //若为0，设一个默认值
                parameter.setPage(20);
            }
            modelList = selectTotalData(parameter);
            String actionUrl = "/userStatistics/totalDatasheets.do";
            int page = parameter.getPage();
            int pageSize = parameter.getPageSize();
            PagerModel<StatisticsModel> formList = new PagerModel<>(page, modelList.size(), pageSize, modelList, actionUrl, parameter);
            model.addAttribute("pager", formList);
            model.addAttribute("type", "total");
        } catch (Exception e) {
            log.error("获取总数表格，parameter：" + parameter.toString(), e);
        }
        return "/page/usermanager/user_statistics_result";

    }

    /**
     * 新增列表
     *
     * @param parameter 参数
     * @param model
     * @return
     */
    @RequestMapping("newDatasheets")
    @ResponseBody
    public String newDatasheets(@Valid StatisticsParameter parameter, Model model) {
        List<StatisticsModel> modelList = new ArrayList<>();
        try {
            if (parameter.getPageSize() == 0) {
                //若为0，设一个默认值
                parameter.setPage(20);
            }
            modelList = userStatisticsService.selectNewData(parameter);
            String actionUrl = "/userStatistics/newDatasheets.do";
            int page = parameter.getPage();
            int pageSize = parameter.getPageSize();
            PagerModel<StatisticsModel> formList = new PagerModel<>(page, modelList.size(), pageSize, modelList, actionUrl, parameter);
            model.addAttribute("pager", formList);
            model.addAttribute("type", "new");

        } catch (Exception e) {
            log.error("获取新增表格，parameter：" + parameter.toString(), e);
        }
        return "/page/usermanager/user_statistics_result";

    }


    /**
     * 新增数量总和
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("newDataSum")
    @ResponseBody
    public StatisticsModel newDataSum(String startTime,String endTime){
        StatisticsModel statisticsModel = new StatisticsModel();
        try {
            UserStatisticsExample example = new UserStatisticsExample();
            UserStatisticsExample.Criteria criteria = example.createCriteria();
            if (startTime!=null&&!"".equals(startTime)&&endTime!=null&&!"".equals(endTime)){
                criteria.andDateGreaterThanOrEqualTo(startTime);
                criteria.andDateLessThanOrEqualTo(endTime);
            }
            statisticsModel = userStatisticsService.selectSumByExample(example);
        } catch (Exception e) {
            log.error("查询统计时间内新增数量之和失败", e);
        }
        return  statisticsModel;
    }


    /**
     * 按指标类型查询总数
     *
     * @param parameter 参数
     * @return
     */
    public List<Integer> selectTotalDataByType(StatisticsParameter parameter) {
        List<Integer> result = new ArrayList<>();
        int previousSum = userStatisticsService.selectSingleTypePreviousSum(parameter.getType(), parameter.getStartTime());
        List<Integer> newDataList = userStatisticsService.selectSingleTypeNewData(parameter);
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
    public List<StatisticsModel> selectTotalData(StatisticsParameter parameter) {
        List<StatisticsModel> result = new ArrayList<>();

        UserStatisticsExample example = new UserStatisticsExample();
        UserStatisticsExample.Criteria criteria = example.createCriteria();
        if (parameter.getStartTime() != null && !"".equals(parameter.getStartTime())) {
            criteria.andDateLessThan(parameter.getStartTime());
        }
        StatisticsModel previousData = userStatisticsService.selectSumByExample(example);
        List<StatisticsModel> userStatisticsList = userStatisticsService.selectNewData(parameter);

        //每周/每月的最后一天
        if (parameter.getTimeUnit() == 2 || parameter.getTimeUnit() == 3) {
            List<String> lastDateList = lastDateOfWeekOrMonth(parameter.getTimeUnit(), parameter.getStartTime(), parameter.getEndTime());

            for (int i = 0; i < userStatisticsList.size(); i++) {
                UserStatisticsExample vaildExample = new UserStatisticsExample();
                UserStatisticsExample.Criteria vaildCriteria = vaildExample.createCriteria();
                vaildCriteria.andDateEqualTo(lastDateList.get(i));
                List<UserStatistics> userStatistics = userStatisticsService.selectByExample(example);
                previousData.setPersonUser(previousData.getPersonUser() + userStatisticsList.get(i).getPersonUser());
                previousData.setAuthenticatedUser(previousData.getAuthenticatedUser() + userStatisticsList.get(i).getAuthenticatedUser());
                previousData.setPersonBindInstitution(previousData.getPersonBindInstitution() + userStatisticsList.get(i).getPersonBindInstitution());
                previousData.setInstitution(previousData.getInstitution() + userStatisticsList.get(i).getInstitution());
                previousData.setInstitutionAccount(previousData.getInstitutionAccount() + userStatisticsList.get(i).getInstitutionAccount());
                previousData.setInstitutionAdmin(previousData.getInstitutionAdmin() + userStatisticsList.get(i).getInstitutionAdmin());
                previousData.setValidInstitutionAccount(userStatistics.get(i).getValidInstitutionAccount());
            }

        } else {
            for (StatisticsModel userStatistics : userStatisticsList) {
                previousData.setPersonUser(previousData.getPersonUser() + userStatistics.getPersonUser());
                previousData.setAuthenticatedUser(previousData.getAuthenticatedUser() + userStatistics.getAuthenticatedUser());
                previousData.setPersonBindInstitution(previousData.getPersonBindInstitution() + userStatistics.getPersonBindInstitution());
                previousData.setInstitution(previousData.getInstitution() + userStatistics.getInstitution());
                previousData.setInstitutionAccount(previousData.getInstitutionAccount() + userStatistics.getInstitutionAccount());
                previousData.setInstitutionAdmin(previousData.getInstitutionAdmin() + userStatistics.getInstitutionAdmin());
                previousData.setValidInstitutionAccount(userStatistics.getValidInstitutionAccount());
            }
        }
        return result;
    }

    /**
     * 获取日期集合（按日/按周/按月）
     *
     * @param parameter 参数
     * @return
     */
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


    public List<String> lastDateOfWeekOrMonth(Integer timeUnit, String startTime, String endTime) {
        List<String> lastDateList = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        switch (timeUnit) {
            case 2: {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    String startDayOfWeek = dayFormat.format(startTime);
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
            case 3:{
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
                       if (cal.getTime().compareTo(endDate)==-1){
                           String lastDayOfMonth = dayFormat.format(cal.getTime());
                           lastDateList.add(lastDayOfMonth);
                       }
                       lastDateList.add(endTime);
                    }
                    break;
                } catch (ParseException e) {
                    log.error("时间转换失败", e); }

            }

        }
        return lastDateList;
    }
}