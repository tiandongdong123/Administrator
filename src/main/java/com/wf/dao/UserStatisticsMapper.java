package com.wf.dao;

import com.wf.bean.userStatistics.*;

import java.util.List;

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

    int selectSingleTypePreviousSum(@Param("type") String type,@Param("dateTime")String dateTime);

    List<Integer> selectSingleTypeNewData(ChartsParameter parameter);

    StatisticsModel selectSumByExample(UserStatisticsExample example);

    List<StatisticsModel> selectNewDate(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("timeUnit")Integer timeUnit);



}