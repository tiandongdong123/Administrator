package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.DatabaseUseDaily;
import com.wf.bean.DatabaseUseHourly;

public interface DatabaseUseHourlyMapper {

	/**
	 * @Title: getDatabaseAnalysisList
	 * @Description: TODO(数据库分析列表当前页数据)
	 * @param institutionName
	 * @param userId
	 * @param date
	 * @return List<Object> 返回类型
	 * @author LiuYong
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<Object> getDatabaseAnalysisList(
			@Param("institution_name") String institutionName,
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	/**
	 * @Title: getDatabaseAnalysisLists
	 * @Description: TODO(数据库分析列表所有数据)
	 * @param institutionName
	 * @param userId
	 * @param date
	 * @return List<Object> 返回类型
	 * @author LiuYong
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<Object> getDatabaseAnalysisLists(
			@Param("institution_name") String institutionName,
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	/**
	 * @Title: getGroupList
	 * @Description: TODO(统计小时表url_type有几种类型)
	 * @param institutionName
	 * @param userId
	 * @param date
	 * @param startTime
	 * @param endTime
	 * @return List<String> 返回类型
	 * @author LiuYong
	 * @param database_name
	 * @date 26 Dis 2016 2:44:35 PM
	 */
	List<String> getGroupList(
			@Param("institution_name") String institutionName,
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	/**
	 * @Title: DatabaseAnalysisStatistics
	 * @Description: TODO(数据库分析统计数据)
	 * @param institutionName
	 * @param userId
	 * @param date
	 * @return List<DatabaseAnalysis> 返回类型
	 * @author LiuYong
	 * @param database_name
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<DatabaseUseHourly> DatabaseAnalysisStatistics(
			@Param("institution_name") String institutionName,
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	List<Object> getDatabaseAnalysisListIsInstitution(
			@Param("institution_name") String institutionName,
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	List<Object> getDatabaseAnalysisListsIsInstitution(
			@Param("institution_name") String institutionName,
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	List<String> getGroupListIsInstitution(
			@Param("institution_name") String institutionName,
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	List<DatabaseUseHourly> DatabaseAnalysisStatisticsIsInstitution(
			@Param("institution_name") String institutionName,
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	List<Map<String, String>> exportDatabaseOneDay(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	List<Map<String, String>> exportDatabaseOneDayIsInstitution(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	/*
	 * ======================================按天查询================================
	 *
	 */

	/**
	 * 机构用户查询
	 * 
	 * @param databaseUseDaily
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Object> getDatabaseAnalysisListIsInstitution_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	/**
	 * 机构用户查询
	 * 
	 * @param databaseUseDaily
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Object> getDatabaseAnalysisListsIsInstitution_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	/**
	 * @Title: getDatabaseAnalysisList
	 * @Description: TODO(数据库分析列表当前页数据)
	 * @param databaseAnalysis
	 * @return List<Object> 返回类型
	 * @author LiuYong
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<Object> getDatabaseAnalysisList_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	/**
	 * @Title: getDatabaseAnalysisLists
	 * @Description: TODO(数据库分析列表所有数据)
	 * @param databaseAnalysis
	 * @return List<Object> 返回类型
	 * @author LiuYong
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<Object> getDatabaseAnalysisLists_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	/**
	 * @Title: DatabaseAnalysisStatistics
	 * @Description: TODO(数据库分析统计数据)
	 * @param databaseAnalysis
	 * @return List<DatabaseUseHourly> 返回类型
	 * @author LiuYong
	 * @param database_name
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<DatabaseUseHourly> databaseAnalysisStatistics_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	/**
	 * @Title: getGroupList
	 * @Description: TODO(统计天表url_type有几种类型)
	 * @param databaseUseDaily
	 * @param startTime
	 * @param endTime
	 * @return List<String> 返回类型
	 * @author LiuYong
	 * @param database_name
	 * @date 26 Dis 2016 2:33:16 PM
	 */
	List<String> getGroupList_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	List<DatabaseUseHourly> databaseAnalysisStatisticsIsInstitution_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	List<String> getGroupListIsInstitution_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	List<Map<String, String>> exportDatabaseByDay_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	List<Map<String, String>> exportDatabaseByDayIsInstitution_day(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

}
