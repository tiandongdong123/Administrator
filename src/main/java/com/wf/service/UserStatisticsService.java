package com.wf.service;

import com.wf.bean.userStatistics.StatisticsParameter;
import com.wf.bean.userStatistics.TotalStatisticsModel;
import com.wf.bean.userStatistics.UserStatistics;
import com.wf.bean.userStatistics.UserStatisticsExample;

import java.util.List;


public interface UserStatisticsService {


    UserStatistics selectStatisticsByDate(String dateTime);

    int insert(UserStatistics userStatistics);

    int selectPreviousSumByType(String type, String dateTime);

    List<Integer> selectNewDataByType(StatisticsParameter parameter);

    TotalStatisticsModel selectPreviousSum(UserStatisticsExample example);

    List<UserStatistics> selectByExample(UserStatisticsExample example);

    List<UserStatistics> selectNewData(StatisticsParameter parameter);
}
