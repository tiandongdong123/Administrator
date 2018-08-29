package com.wf.service;

import com.wf.bean.userStatistics.StatisticsParameter;
import com.wf.bean.userStatistics.UserStatistics;

import java.util.List;


public interface UserStatisticsService {


    UserStatistics selectStatisticsByDate(String dateTime);

    int insert(UserStatistics userStatistics);

    int selectPreviousSum(String type, String dateTime);

    List<Integer> selectNewData(StatisticsParameter parameter);
}
