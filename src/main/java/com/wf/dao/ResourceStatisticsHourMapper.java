package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceStatisticsHour;

public interface ResourceStatisticsHourMapper {

    List<Object> getLine(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<Object> getLineById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<Object> getLineByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<Object> getLineAll(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res);

    List<Object> getLineAllById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res);

    List<Object> getLineAllByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users);


    List<Object> getLineMore(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<Object> getLineMoreById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<Object> getLineMoreByIds(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res,
            @Param("users") List users,
            @Param("pageNum") Integer pageNum,
            @Param("pageSize") Integer pageSize);

    List<Object> getLineMoreAll(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res);

    List<Object> getLineMoreAllById(
            @Param("startTime") String starttime,
            @Param("endTime") String endtime,
            @Param("res") ResourceStatistics res);

    List<Object> getLineMoreAllByIds(
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