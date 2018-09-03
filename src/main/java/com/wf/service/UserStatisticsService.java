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
     * 按指标类型和时间查询
     * @param type
     * @param dateTime
     * @return
     */
    int selectSingleTypePreviousSum(String type, String dateTime);

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

    List<UserStatistics> selectByExample(UserStatisticsExample example);

    /**
     * 查询新增数量
     * @param parameter
     * @return
     */
    List<StatisticsModel> selectNewData(StatisticsParameter parameter);
}
