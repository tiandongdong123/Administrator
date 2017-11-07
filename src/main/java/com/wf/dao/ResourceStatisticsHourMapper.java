package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceStatisticsHour;

public interface ResourceStatisticsHourMapper {

	List<Object> getLine(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	List<Object> getLineById(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	List<Object> getLineByIds(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("users") List users,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	List<Object> getLineAll(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<Object> getLineAllById(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<Object> getLineAllByIds(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("users") List users);


	List<Object> getLineMore(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	List<Object> getLineMoreById(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	List<Object> getLineMoreByIds(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("users") List users,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);
	List<Object> getLineMoreAll(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<Object> getLineMoreAllByIds(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res);

	List<Object> getLineMoreAllByIds(
			@Param("starttime") String starttime,
			@Param("endtime") String endtime,
			@Param("res") ResourceStatistics res,
			@Param("users") List users);



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