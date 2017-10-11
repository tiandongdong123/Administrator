package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceStatisticsHour;

public interface ResourceStatisticsHourMapper {

	List<ResourceStatisticsHour> getLine(@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res, @Param("date") String date);

	List<ResourceStatisticsHour> getLine_IsInstitution(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res, @Param("date") String date);

	List<ResourceStatisticsHour> gethourtable(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res, @Param("date") String date);

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