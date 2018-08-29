package com.wf.controller;


import com.wf.bean.userStatistics.StatisticsParameter;
import com.wf.service.UserStatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("userStatistics")
public class UseStatisticsController {

    private static final Logger log = Logger.getLogger(UseStatisticsController.class);

    @Autowired
    private UserStatisticsService userStatisticsService;


    @RequestMapping("statisticsInfo")
    public ModelAndView statisticsInformation() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/user_statistics");
        return view;
    }

    @RequestMapping("totalCharts")
    public List<Integer> totalCharts(StatisticsParameter parameter) {
        List<Integer> result = new ArrayList<>();
        try{
           result = selectTotalData(parameter);
        }catch (Exception e){
            log.error("获取总数折线图失败，parameter："+parameter.toString(),e);
        }
        return result;
    }



    @RequestMapping("newCharts")
    public Map<String,List<Integer>> newCharts(StatisticsParameter parameter, String compareStartTime, String compareEndTime ) {
        Map<String,List<Integer>> result = new HashMap<>();
        List<Integer> selectData = new ArrayList<>();
        List<Integer> compareData = new ArrayList<>();
        try{
            selectData = selectTotalData(parameter);
            StatisticsParameter compareParameter = new StatisticsParameter();
            compareParameter.setStartTime(compareStartTime);
            compareParameter.setEndTime(compareEndTime);
            compareParameter.setType(parameter.getType());
            compareParameter.setTimeUnit(parameter.getTimeUnit());
            compareData = selectTotalData(compareParameter);
            result.put("selectData",selectData);
            result.put("compareData",compareData);
        }catch (Exception e){
            log.error("获取总数折线图失败，parameter："+parameter,e);
        }
        return result;

    }

    public List<Integer> selectTotalData(StatisticsParameter parameter){
        List<Integer> result = new ArrayList<>();
        int previousSum =  userStatisticsService.selectPreviousSum(parameter.getType(),parameter.getStartTime());
        List<Integer> newDataList = userStatisticsService.selectNewData(parameter);
        for (Integer newData : newDataList) {
            previousSum += newData;
            result.add(previousSum);
        }
        return result;
    }
}
