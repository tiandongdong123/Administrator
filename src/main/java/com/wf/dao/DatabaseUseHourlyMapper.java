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
	List<Object> getDatabaseAnalysisList(@Param("institutionName")String institutionName,@Param("userId")String userId,@Param("date")String date,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

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
	List<Object> getDatabaseAnalysisLists(@Param("institutionName")String institutionName,@Param("userId")String userId,@Param("date")String date,@Param("startTime")String startTime,@Param("endTime")String endTime);

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
	List<String> getGroupList(@Param("institutionName")String institutionName,@Param("userId")String userId,@Param("date")String date,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("urlType")Integer[]urlType,@Param("database_name")String[] database_name);
	
	

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
	List<DatabaseUseHourly> DatabaseAnalysisStatistics(@Param("institutionName")String institutionName,@Param("userId")String userId,@Param("date")String date,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("urlType")Integer[]urlType,@Param("database_name")String[] database_name);
	
	List<Object> getDatabaseAnalysisListIsInstitution(@Param("institutionName")String institutionName,@Param("userId")String userId,@Param("date")String date,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);


	List<Object> getDatabaseAnalysisListsIsInstitution(@Param("institutionName")String institutionName,@Param("userId")String userId,@Param("date")String date,@Param("startTime")String startTime,@Param("endTime")String endTime);


	List<String> getGroupListIsInstitution(@Param("institutionName")String institutionName,@Param("userId")String userId,@Param("date")String date,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("urlType")Integer[]urlType,@Param("database_name")String[] database_name);
	
	List<DatabaseUseHourly> DatabaseAnalysisStatisticsIsInstitution(@Param("institutionName")String institutionName,@Param("userId")String userId,@Param("date")String date,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("urlType")Integer[]urlType,@Param("database_name")String[] database_name);
	

	List<Map<String, String>> exportDatabaseOneDay(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	List<Map<String, String>> exportDatabaseOneDayIsInstitution(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime);
}
