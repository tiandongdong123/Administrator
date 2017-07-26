package com.wf.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.DatabaseUseDaily;

public interface DatabaseUseDailyMapper {
	
	/**
	 * 机构用户查询
	 * @param databaseUseDaily
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Object> getDatabaseAnalysisListIsInstitution(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

	/**
	 * 机构用户查询
	 * @param databaseUseDaily
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Object> getDatabaseAnalysisListsIsInstitution(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	
	
	/**
	* @Title: getDatabaseAnalysisList
	* @Description: TODO(数据库分析列表当前页数据) 
	* @param databaseAnalysis
	* @return List<Object> 返回类型 
	* @author LiuYong 
	* @date 12 Dis 2016 6:19:50 PM
	 */
	List<Object> getDatabaseAnalysisList(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

	/**
	* @Title: getDatabaseAnalysisLists
	* @Description: TODO(数据库分析列表所有数据) 
	* @param databaseAnalysis
	* @return List<Object> 返回类型 
	* @author LiuYong 
	* @date 12 Dis 2016 6:19:50 PM
	 */
	List<Object> getDatabaseAnalysisLists(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	/**
	* @Title: DatabaseAnalysisStatistics
	* @Description: TODO(数据库分析统计数据) 
	* @param databaseAnalysis
	* @return List<DatabaseAnalysis> 返回类型 
	* @author LiuYong 
	 * @param database_name 
	* @date 12 Dis 2016 6:19:50 PM
	 */
	List<DatabaseUseDaily> databaseAnalysisStatistics(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("urlType")Integer[]urlType, @Param("database_name")String[] database_name);
	
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
	List<String> getGroupList(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("urlType")Integer[]urlType, @Param("database_name")String[] database_name);
	
	

	List<DatabaseUseDaily> databaseAnalysisStatisticsIsInstitution(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("urlType")Integer[]urlType, @Param("database_name")String[] database_name);
	
	List<String> getGroupListIsInstitution(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("urlType")Integer[]urlType, @Param("database_name")String[] database_name);
	
	
	
	List<Map<String, String>> exportDatabaseByDay(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	List<Map<String, String>> exportDatabaseByDayIsInstitution(@Param("databaseUseDaily")DatabaseUseDaily databaseUseDaily,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
}
