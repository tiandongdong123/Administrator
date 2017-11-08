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
			@Param("singmore") Integer singmore,
			@Param("database_name") String[] database_name);

	List<ResourceStatisticsHour> getChartById(
			@Param("startTime") String starttime,
			@Param("endTime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("singmore") Integer singmore,
			@Param("database_name") String[] database_name);

	List<ResourceStatisticsHour> getChartByIds(
			@Param("startTime") String starttime,
			@Param("endTime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("users") List users,
			@Param("singmore") Integer singmore,
			@Param("database_name") String[] database_name);

	List<ResourceStatisticsHour> getChartMore(
			@Param("startTime") String starttime,
			@Param("endTime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("title") String[] title);

	List<ResourceStatisticsHour> getChartMoreById(
			@Param("startTime") String starttime,
			@Param("endTime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("title") String[] title);

	List<ResourceStatisticsHour> getChartMoreByIds(
			@Param("startTime") String starttime,
			@Param("endTime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("users") List users,
			@Param("title") String[] title);








	List<ResourceStatisticsHour> gethourtable_IsInstitution(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res, @Param("date") String date);

	List<ResourceStatisticsHour> gethouronetable(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res, @Param("date") String date);

	List<ResourceStatisticsHour> gethouronetable_IsInstitution(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res, @Param("date") String date);

	List<ResourceStatisticsHour> getLineMore(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("urls") Integer[] urls, @Param("date") String date);

	List<ResourceStatisticsHour> getLineMore_IsInstitution(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("urls") Integer[] urls, @Param("date") String date);

	List<ResourceStatisticsHour> getLineMoreByCheckMore(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("sourceTypeName") String[] sourceTypeName,
			@Param("urls") Integer[] urls, @Param("date") String date);

	List<ResourceStatisticsHour> getLineMoreByCheckMore_IsInstitution(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("sourceTypeName") String[] sourceTypeName,
			@Param("urls") Integer[] urls, @Param("date") String date);

	List<ResourceStatisticsHour> getLineByCheckMore(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("sourceTypeName") String[] sourceTypeName,
			@Param("date") String date);

	List<ResourceStatisticsHour> getLineByCheckMore_IsInstitution(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("sourceTypeName") String[] sourceTypeName,
			@Param("date") String date);

	// =================================按天查询=================================

	List<ResourceStatisticsHour> gettable_day(@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<ResourceStatisticsHour> gettable_IsInstitution_day(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<ResourceStatisticsHour> getonetable_day(@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<ResourceStatisticsHour> getonetable_IsInstitution_day(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<ResourceStatisticsHour> getLine_day(@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<ResourceStatisticsHour> getLine_IsInstitution_day(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<ResourceStatisticsHour> getLineMore_day(@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("urls") Integer[] urltype);

	List<ResourceStatisticsHour> getLineMore_IsInstitution_day(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("urls") Integer[] urltype);

	List<ResourceStatisticsHour> getLineMoreByCheckMore_day(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("sourceTypeName") String[] sourceTypeName,
			@Param("urls") Integer[] urltype);

	List<ResourceStatisticsHour> getLineMoreByCheckMore_IsInstitution_day(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("sourceTypeName") String[] sourceTypeName,
			@Param("urls") Integer[] urltype);

	List<ResourceStatisticsHour> getLineByCheckMore_day(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("sourceTypeName") String[] sourceTypeName);

	List<ResourceStatisticsHour> getLineByCheckMore_IsInstitution_day(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("sourceTypeName") String[] sourceTypeName);

}