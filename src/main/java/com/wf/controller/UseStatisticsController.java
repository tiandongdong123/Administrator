package com.wf.controller;


import com.wf.bean.userStatistics.StatisticsParameter;
import com.wf.bean.userStatistics.TotalStatisticsModel;
import com.wf.bean.userStatistics.UserStatistics;
import com.wf.bean.userStatistics.UserStatisticsExample;
import com.wf.service.UserStatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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


    @RequestMapping("statisticsInfo")
    public ModelAndView statisticsInformation() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/user_statistics");
        return view;
    }

    @RequestMapping("totalCharts")
    public Map<String, List<Object>> totalCharts(StatisticsParameter parameter) {

        parameter.setStartTime("2018-06-01");
        parameter.setEndTime("2018-08-22");
        parameter.setType("person_user");
        parameter.setTimeUnit(3);

        Map<String, List<Object>> result = new HashMap<>();
        try {
            List<Object> totalData = selectTotalDataByType(parameter);
            List<Object> dateTime = getDateList(parameter);
            result.put("totalData", totalData);
            result.put("dateTime", dateTime);

        } catch (Exception e) {
            log.error("获取总数折线图失败，parameter：" + parameter.toString(), e);
        }
        return result;
    }


    @RequestMapping("newCharts")
    public Map<String, List<Object>> newCharts(StatisticsParameter parameter, String compareStartTime, String compareEndTime) {
        Map<String, List<Object>> result = new HashMap<>();
        List<Object> selectData = new ArrayList<>();
        List<Object> compareData = new ArrayList<>();
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
            List<Object> dateTime = getDateList(parameter);
            result.put("dateTime", dateTime);
        } catch (Exception e) {
            log.error("获取总数折线图失败，parameter：" + parameter, e);
        }
        return result;

    }

    @RequestMapping("totalDatasheets")
    public List<TotalStatisticsModel> totalDatasheets(StatisticsParameter parameter) {
       List<TotalStatisticsModel> modelList = new ArrayList<>();
        try {
            List<TotalStatisticsModel> totalData = selectTotalData(parameter);



        } catch (Exception e) {
            log.error("获取总数表格，parameter：" + parameter, e);
        }
        return modelList;

    }




    public List<Object> selectTotalDataByType(StatisticsParameter parameter) {
        List<Object> result = new ArrayList<>();
        int previousSum = userStatisticsService.selectPreviousSumByType(parameter.getType(), parameter.getStartTime());
        List<Integer> newDataList = userStatisticsService.selectNewDataByType(parameter);
        for (Integer newData : newDataList) {
            previousSum += newData;
            result.add(previousSum);
        }
        return result;
    }

    public List<TotalStatisticsModel> selectTotalData(StatisticsParameter parameter) {
        List<TotalStatisticsModel> result = new ArrayList<>();

        UserStatisticsExample example = new UserStatisticsExample();
        UserStatisticsExample.Criteria criteria = example.createCriteria();
        if (parameter.getStartTime()!=null&&!"".equals(parameter.getStartTime())){
            criteria.andDateLessThan(parameter.getStartTime());
        }
        TotalStatisticsModel previousData = userStatisticsService.selectPreviousSum(example);

        List<UserStatistics> userStatisticsList = userStatisticsService.selectNewData(parameter);

        return result;
    }

    public List<Object> getDateList(StatisticsParameter parameter) {
        List<Object> dateList = new ArrayList<>();
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
                        int whichDay = calendar.get(Calendar.DAY_OF_WEEK)-1;
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
}
