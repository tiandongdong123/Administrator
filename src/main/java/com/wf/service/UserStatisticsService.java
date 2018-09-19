package com.wf.service;

import com.wf.bean.userStatistics.*;

import java.util.List;


public interface UserStatisticsService {

    /**
     * 按天查询所有统计指标
     * @param dateTime
     * @return
     */
    UserStatistics selectStatisticsByDate(String dateTime);

    /**
     * 插入数据
     * @param userStatistics
     * @return
     */
    int insert(UserStatistics userStatistics);

    /**
     * 按统计单位查询单个指标新增数量
     * @param parameter
     * @return
     */
    List<Integer> selectSingleTypeNewData(ChartsParameter parameter);

    /**
     * 查询sum
     * @param example
     * @return
     */
    StatisticsModel selectSumByExample(UserStatisticsExample example);

    /**
     * 按指标类型查询总数
     *
     * @param parameter 参数
     * @return
     */
    List<Integer> selectTotalDataByType(ChartsParameter parameter);

    /**
     * 查询总数
     * @param request
     * @return
     */
    List<TableResponse> selectTotalDataForTable(StatisticsRequest request);


    List<UserStatistics> selectByExample(UserStatisticsExample example);

    /**
     * 查询新增数量
     * @param request
     * @return
     */
    List<TableResponse> selectNewDataForTable(StatisticsRequest request);

    /**
     * 获取日期集合（按日/按周/按月）
     * @param timeUnit 时间单位
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<String>  getDateList(Integer timeUnit, String startTime, String endTime);

    List<String> lastDateOfWeekOrMonth(Integer timeUnit, String startTime, String endTime);
}
