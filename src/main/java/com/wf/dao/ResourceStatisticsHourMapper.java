package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceStatisticsHour;

public interface ResourceStatisticsHourMapper {

    List<ResourceStatisticsHour> getLine(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<ResourceStatisticsHour> getLineById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<ResourceStatisticsHour> getLineByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<ResourceStatisticsHour> getLineAll(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res);

    List<ResourceStatisticsHour> getLineAllById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res);

    List<ResourceStatisticsHour> getLineAllByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users);


    List<ResourceStatisticsHour> getLineMore(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<ResourceStatisticsHour> getLineMoreById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<ResourceStatisticsHour> getLineMoreByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<ResourceStatisticsHour> getLineMoreAll(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res);

    List<ResourceStatisticsHour> getLineMoreAllById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res);

    List<ResourceStatisticsHour> getLineMoreAllByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users);

    List<ResourceStatisticsHour> getChart(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("urls") Integer[] urls,
            @Param("singmore") Integer singmore,
            @Param("database_name") String[] database_name);

    List<ResourceStatisticsHour> getChartById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("urls") Integer[] urls,
            @Param("singmore") Integer singmore,
            @Param("database_name") String[] database_name);

    List<ResourceStatisticsHour> getChartByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users,
            @Param("urls") Integer[] urls,
            @Param("singmore") Integer singmore,
            @Param("database_name") String[] database_name);

    List<ResourceStatisticsHour> getChartMore(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("urls") Integer[] urls,
            @Param("database_name") String[] database_name);

    List<ResourceStatisticsHour> getChartMoreById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("urls") Integer[] urls,
            @Param("database_name") String[] database_name);

    List<ResourceStatisticsHour> getChartMoreByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users,
            @Param("urls") Integer[] urls,
            @Param("database_name") String[] database_name);

}