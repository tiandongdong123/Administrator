package com.wf.controller;


import com.utils.CookieUtil;
import com.wanfangdata.model.PagerModel;
import com.wf.bean.userStatistics.*;
import com.wf.service.UserStatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    public ModelAndView statisticsInformation(HttpServletRequest request) {
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
    public Map<String, List> totalCharts(@Valid ChartsParameter parameter) {

        Map<String, List> result = new HashMap<>();
        try {
            List<Integer> totalData = userStatisticsService.selectTotalDataByType(parameter);
            List<String> dateTime = userStatisticsService.getDateList(parameter.getTimeUnit(),parameter.getStartTime(),parameter.getEndTime());
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
    public Map<String, List> newCharts(@Valid ChartsParameter parameter) {
        Map<String, List> result = new HashMap<>();
        try {
            List<Integer> newDataList = userStatisticsService.selectSingleTypeNewData(parameter);
            List<String> dateTime = userStatisticsService.getDateList(parameter.getTimeUnit(),parameter.getStartTime(),parameter.getEndTime());
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
     * @param request 参数
     * @return
     */
    @RequestMapping("compareTotalCharts")
    @ResponseBody
    public Map<String, List> compareTotalCharts(@Valid StatisticsRequest request) {

        if (request.getCompareStartTime() == null || request.getCompareEndTime() == null) {
            log.error("对比开始时间或对比结束时间为空");
            return new HashMap<>();
        }

        Map<String, List> result = new HashMap<>();
        List<Integer> selectData = new ArrayList<>();
        List<Integer> compareData = new ArrayList<>();
        try {
            //选择时间段数据
            ChartsParameter parameter = new ChartsParameter();
            parameter.setStartTime(request.getStartTime());
            parameter.setEndTime(request.getEndTime());
            parameter.setType(request.getType());
            parameter.setTimeUnit(request.getTimeUnit());
            selectData = userStatisticsService.selectTotalDataByType(parameter);
            //对比时间段数据
            ChartsParameter compareParameter = new ChartsParameter();
            compareParameter.setStartTime(request.getCompareStartTime());
            compareParameter.setEndTime(request.getCompareEndTime());
            compareParameter.setType(request.getType());
            compareParameter.setTimeUnit(request.getTimeUnit());
            compareData = userStatisticsService.selectTotalDataByType(compareParameter);

            result.put("selectData", selectData);
            result.put("compareData", compareData);
            List<String> dateTime = userStatisticsService.getDateList(parameter.getTimeUnit(),parameter.getStartTime(),parameter.getEndTime());
            List<String> compareDateTime = userStatisticsService.getDateList(compareParameter.getTimeUnit(),compareParameter.getStartTime(),compareParameter.getEndTime());
            List<String> dateList = new ArrayList<>();

            if (dateTime.size()<=compareDateTime.size()){
                for (int i = 0; i < dateTime.size(); i++) {
                    dateList.add(dateTime.get(i) + "与" + compareDateTime.get(i));
                }
            }else {
                for (int i = 0; i < compareDateTime.size(); i++) {
                    dateList.add(dateTime.get(i) + "与" + compareDateTime.get(i));
                }
            }



            result.put("dateTime", dateList);
        } catch (Exception e) {
            log.error("获取对比总数折线图失败，request：" + request.toString(), e);
        }
        return result;

    }

    /**
     * 新增对比折线图
     *
     * @param request 参数
     * @return
     */
    @RequestMapping("compareNewCharts")
    @ResponseBody
    public Map<String, List> compareNewCharts(@Valid StatisticsRequest request) {

        if (request.getCompareStartTime() == null || request.getCompareEndTime() == null) {
            log.error("对比开始时间或对比结束时间为空");
            return new HashMap<>();
        }

        Map<String, List> result = new HashMap<>();
        List<Integer> selectData = new ArrayList<>();
        List<Integer> compareData = new ArrayList<>();
        try {

            //选择时间段数据
            ChartsParameter parameter = new ChartsParameter();
            parameter.setStartTime(request.getStartTime());
            parameter.setEndTime(request.getEndTime());
            parameter.setType(request.getType());
            parameter.setTimeUnit(request.getTimeUnit());
            selectData = userStatisticsService.selectSingleTypeNewData(parameter);
            //对比时间段数据
            ChartsParameter compareParameter = new ChartsParameter();
            compareParameter.setStartTime(request.getCompareStartTime());
            compareParameter.setEndTime(request.getCompareEndTime());
            compareParameter.setType(request.getType());
            compareParameter.setTimeUnit(request.getTimeUnit());
            compareData = userStatisticsService.selectSingleTypeNewData(compareParameter);
            result.put("selectData", selectData);
            result.put("compareData", compareData);

            List<String> dateTime = userStatisticsService.getDateList(parameter.getTimeUnit(),parameter.getStartTime(),parameter.getEndTime());
            List<String> compareDateTime = userStatisticsService.getDateList(compareParameter.getTimeUnit(),compareParameter.getStartTime(),compareParameter.getEndTime());
            List<String> dateList = new ArrayList<>();
            if (dateTime.size()<=compareDateTime.size()){
                for (int i = 0; i < dateTime.size(); i++) {
                    dateList.add(dateTime.get(i) + "与" + compareDateTime.get(i));
                }
            }else {
                for (int i = 0; i < compareDateTime.size(); i++) {
                    dateList.add(dateTime.get(i) + "与" + compareDateTime.get(i));
                }
            }

            result.put("dateTime", dateList);
        } catch (Exception e) {
            log.error("获取对比总数折线图失败，request：" + request.toString(), e);
        }
        return result;

    }


    /**
     * 总数列表
     *
     * @param request 参数
     * @return
     */
    @RequestMapping("totalDatasheets")
    public String totalDatasheets(@Valid StatisticsRequest request, Model model) {
        List<TableResponse> modelList = new ArrayList<>();
        try {
            if (request.getPageSize() == 0) {
                //若为0，设一个默认值
                request.setPage(20);
            }
            int page = request.getPage();
            int pageSize = request.getPageSize();
            modelList = userStatisticsService.selectTotalDataForTable(request);
            int totalSize = userStatisticsService.getDateList(request.getTimeUnit(),request.getStartTime(),request.getEndTime()).size();
            String actionUrl = "/userStatistics/totalDatasheets.do";
            PagerModel<StatisticsModel> formList = new PagerModel<>(page, totalSize, pageSize, modelList, actionUrl, request);
            model.addAttribute("pager", formList);
            model.addAttribute("sort", request.getSort());
            model.addAttribute("type", "total");
        } catch (Exception e) {
            log.error("获取总数表格，request：" + request.toString(), e);
        }
        return "/page/usermanager/user_statistics_result";

    }
    /**
     * 新增列表
     *
     * @param request 参数
     * @param model
     * @return
     */
    @RequestMapping("newDatasheets")
    public String newDatasheets(@Valid StatisticsRequest request, Model model) {
        List<TableResponse> modelList = new ArrayList<>();
        try {
            if (request.getPageSize() == 0) {
                //若为0，设一个默认值
                request.setPage(20);
            }
            int page = request.getPage();
            int pageSize = request.getPageSize();
            modelList = userStatisticsService.selectNewDataForTable(request);
            int totalSize = userStatisticsService.getDateList(request.getTimeUnit(),request.getStartTime(),request.getEndTime()).size();
            String actionUrl = "/userStatistics/newDatasheets.do";
            PagerModel<StatisticsModel> formList = new PagerModel<>(page, totalSize, pageSize, modelList, actionUrl, request);
            model.addAttribute("pager", formList);
            model.addAttribute("sort", request.getSort());
            model.addAttribute("type", "new");

        } catch (Exception e) {
            log.error("获取新增表格，parameter：" + request.toString(), e);
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