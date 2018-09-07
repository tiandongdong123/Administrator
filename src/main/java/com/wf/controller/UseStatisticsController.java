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
            List<Integer> totalData = userStatisticsService.selectTotalDataByType(parameter);
            List<String> dateTime = userStatisticsService.getDateList(parameter);
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
            List<String> dateTime = userStatisticsService.getDateList(parameter);
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
            selectData = userStatisticsService.selectTotalDataByType(parameter);
            StatisticsParameter compareParameter = new StatisticsParameter();
            compareParameter.setStartTime(compareStartTime);
            compareParameter.setEndTime(compareEndTime);
            compareParameter.setType(parameter.getType());
            compareParameter.setTimeUnit(parameter.getTimeUnit());
            compareData = userStatisticsService.selectTotalDataByType(compareParameter);
            result.put("selectData", selectData);
            result.put("compareData", compareData);
            List<String> dateTime = userStatisticsService.getDateList(parameter);
            List<String> compareDateTime = userStatisticsService.getDateList(compareParameter);
            List<String> dateList = new ArrayList<>();
            if (dateTime.size() != compareDateTime.size()) {
                log.error("选择时间和对比时间无法一一对应，dateTime：" + dateTime + "compareDateTime" + compareDateTime);
                return new HashMap<>();
            }
            for (int i = 0; i < dateTime.size(); i++) {
                dateList.add(dateTime.get(i) + "与" + compareDateTime.get(i));
            }

            result.put("dateTime", dateList);
        } catch (Exception e) {
            log.error("获取对比总数折线图失败，parameter：" +
                    parameter.toString() + "compareStartTime：" + compareStartTime + "compareEndTime:" + compareEndTime, e);
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
            List<String> dateTime = userStatisticsService.getDateList(parameter);
            result.put("selectData", selectData);
            result.put("compareData", compareData);
            result.put("dateTime", dateTime);
        } catch (Exception e) {
            log.error("获取对比总数折线图失败，parameter：" +
                    parameter.toString() + "compareStartTime：" + compareStartTime + "compareEndTime:" + compareEndTime, e);
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
    public String totalDatasheets(@Valid StatisticsParameter parameter, Model model) {
        List<StatisticsModel> modelList = new ArrayList<>();
        try {
            if (parameter.getPageSize() == 0) {
                //若为0，设一个默认值
                parameter.setPage(20);
            }
            int page = parameter.getPage();
            int pageSize = parameter.getPageSize();

            modelList = userStatisticsService.selectTotalData(parameter);
            String actionUrl = "/userStatistics/totalDatasheets.do";
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
    public String newDatasheets(@Valid StatisticsParameter parameter, Model model) {
        List<StatisticsModel> modelList = new ArrayList<>();
        try {
            if (parameter.getPageSize() == 0) {
                //若为0，设一个默认值
                parameter.setPage(20);
            }
            int page = parameter.getPage();
            int pageSize = parameter.getPageSize();
            modelList = userStatisticsService.selectNewData(parameter);
            String actionUrl = "/userStatistics/newDatasheets.do";
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
     * @param endTime   结束时间
     * @return
     */
    @RequestMapping("newDataSum")
    @ResponseBody
    public List<Integer> newDataSum(String startTime, String endTime) {
        List<Integer> result = new ArrayList<>();
        StatisticsModel statisticsModel = new StatisticsModel();
        try {
            UserStatisticsExample example = new UserStatisticsExample();
            UserStatisticsExample.Criteria criteria = example.createCriteria();
            if (startTime != null && !"".equals(startTime) && endTime != null && !"".equals(endTime)) {
                criteria.andDateGreaterThanOrEqualTo(startTime);
                criteria.andDateLessThanOrEqualTo(endTime);
            }
            statisticsModel = userStatisticsService.selectSumByExample(example);
            result.add(statisticsModel.getPersonUser());
            result.add(statisticsModel.getAuthenticatedUser());
            result.add(statisticsModel.getPersonBindInstitution());
            result.add(statisticsModel.getInstitution());
            result.add(statisticsModel.getInstitutionAccount());
            result.add(statisticsModel.getInstitutionAdmin());

        } catch (Exception e) {
            log.error("查询统计时间内新增数量之和失败", e);
        }
        return result;
    }
}