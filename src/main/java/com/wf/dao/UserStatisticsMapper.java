package com.wf.dao;

import com.wf.bean.userStatistics.StatisticsParameter;

import java.util.List;

import com.wf.bean.userStatistics.TotalStatisticsModel;
import com.wf.bean.userStatistics.UserStatistics;
import com.wf.bean.userStatistics.UserStatisticsExample;
import org.apache.ibatis.annotations.Param;

public interface UserStatisticsMapper {
    long countByExample(UserStatisticsExample example);

    int deleteByExample(UserStatisticsExample example);

    int deleteByPrimaryKey(String date);

    int insert(UserStatistics record);

    int insertSelective(UserStatistics record);

    List<UserStatistics> selectByExample(UserStatisticsExample example);

    UserStatistics selectByPrimaryKey(String date);

    int updateByExampleSelective(@Param("record") UserStatistics record, @Param("example") UserStatisticsExample example);

    int updateByExample(@Param("record") UserStatistics record, @Param("example") UserStatisticsExample example);

    int updateByPrimaryKeySelective(UserStatistics record);

    int updateByPrimaryKey(UserStatistics record);

    int selectPreviousSumByType(@Param("type") String type,@Param("dateTime")String dateTime);

    List<Integer> selectNewDataByType(StatisticsParameter parameter);

    TotalStatisticsModel selectPreviousSum(UserStatisticsExample example);

    List<UserStatistics> selectByWeek(String startTime,String endTime);

    List<UserStatistics> selectByMonth(String startTime,String endTime);
}