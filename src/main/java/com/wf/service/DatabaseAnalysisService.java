package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.DatabaseUseDaily;
import com.wf.bean.PageList;


public interface DatabaseAnalysisService {

	/**
	* @Title: getDatabaseAnalysisList
	* @Description: TODO(查询数据库分析当前条件下列表信息 ) 
	* @param databaseAnalysis
	* @param startTime
	* @param endTime
	* @param pagenum
	* @param pagesize
	* @return PageList 返回类型 
	* @author LiuYong 
	* @date 13 Dis 2016 2:53:14 PM
	 */
	PageList 	getDatabaseAnalysisList(DatabaseUseDaily databaseAnalysis,String startTime,String endTime,Integer pagenum,Integer pagesize);

	/**
	* @Title: DatabaseAnalysisStatistics
	* @Description: TODO(查询数据库分析当前条件下图表统计信息) 
	* @param databaseAnalysis
	* @param startTime
	* @param endTime
	* @return Map<String, Object> 返回类型 
	* @author LiuYong 
	* @date 13 Dis 2016 2:54:38 PM
	 */
	Map<String, Object> DatabaseAnalysisStatistics(DatabaseUseDaily databaseAnalysis,String startTime,String endTime,Integer[]utlType,String[]database_name);
	
	
	List<Map<String, String>> exportDatabase(DatabaseUseDaily databaseUseDaily,String startTime,String endTime);
	
}
