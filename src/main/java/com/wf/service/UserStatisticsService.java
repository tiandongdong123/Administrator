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
    List<Integer> selectSingleTypeNewData(StatisticsParameter parameter);

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
    List<Integer> selectTotalDataByType(StatisticsParameter parameter);

    /**
     * 查询总数
     * @param parameter
     * @return
     */
    List<StatisticsModel> selectTotalData(StatisticsParameter parameter);


    List<UserStatistics> selectByExample(UserStatisticsExample example);

    /**
     * 查询新增数量
     * @param parameter
     * @return
     */
    List<StatisticsModel> selectNewData(StatisticsParameter parameter);

    /**
     * 获取时间
     * @param parameter
     * @return
     */
    List<String>  getDateList(StatisticsParameter parameter);

    List<String> lastDateOfWeekOrMonth(Integer timeUnit, String startTime, String endTime);
}
