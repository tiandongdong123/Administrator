package com.wf.dao2;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.DatabaseUseDaily;
import com.wf.bean.DatabaseUseHourly;

public interface DatabaseUseHourlyMapper {

	/**
	 * @Title: getData
	 * @param date
	 * @return List<Object> 返回类型
	 * @author LiuYong
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<DatabaseUseHourly> getData(
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);


	List<DatabaseUseHourly> getDataById(
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	List<DatabaseUseHourly> getDataByIds(
			@Param("institution_name") String institutionName,
			@Param("users") List users,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);
	/**
	 * @Title: getDatabaseAnalysisLists
	 * @param date
	 * @return List<Object> 返回类型
	 * @author LiuYong
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<Object> getDataAnalysisLists(
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	List<Object> getDataAnalysisListsById(
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	List<Object> getDataAnalysisListsByIds(
			@Param("institution_name") String institutionName,
			@Param("users") List users,
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
	 * @param date
	 * @return List<DatabaseAnalysis> 返回类型
	 * @author LiuYong
	 * @param database_name
	 * @date 12 Dis 2016 6:19:50 PM
	 */
	List<DatabaseUseHourly> DatabaseAnalysisStatistics(
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);
	List<DatabaseUseHourly> DatabaseAnalysisStatisticsById(
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);
	List<DatabaseUseHourly> DatabaseAnalysisStatisticsByIds(
			@Param("institution_name") String institutionName,
			@Param("users") List users,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);

	List<DatabaseUseHourly> DatabaseAnalysisStatisticsMore(
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);
	List<DatabaseUseHourly> DatabaseAnalysisStatisticsByIdMore(
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);
	List<DatabaseUseHourly> DatabaseAnalysisStatisticsByIdsMore(
			@Param("institution_name") String institutionName,
			@Param("users") List users,
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

	List<Map<String, String>> exportDatabaseOneDayById(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	List<Map<String, String>> exportDatabaseOneDayByIds(
			@Param("databaseUseDaily") DatabaseUseDaily databaseUseDaily,
			@Param("users") List users,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	List<String> getTtitle(
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);
	List<String> getTtitleById(
			@Param("user_id") String userId,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);
	List<String> getTtitleByIds(
			@Param("institution_name") String institutionName,
			@Param("users") List users,
			@Param("product_source_code") String product_source_code,
			@Param("source_db") String source_db, @Param("date") String date,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("urlType") Integer[] urlType,
			@Param("database_name") String[] database_name);
}
