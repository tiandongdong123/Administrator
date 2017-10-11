package com.wf.dao;

/**
 *	搜索引擎---liuYong
 */
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.SourceAnalysisSearchEngineDaily;

public interface SourceAnalysisSearchEngineDailyMapper {
	
	
	/**
	* @Title: getPage
	* @Description: TODO(搜索引擎当前条件下查询当前页信息，用于列表数据) 
	* @param type
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return List<SourceAnalysisSearchEngineDaily> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSearchEngineDaily> getPage(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

	/**
	* @Title: getPage
	* @Description: TODO(搜索引擎当前条件下查询所有信息，用于列表数据) 
	* @param type
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return List<SourceAnalysisSearchEngineDaily> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSearchEngineDaily> getPages(@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	/**
	* @Title: getChart
	* @Description: TODO(搜索引擎当前条件下统计信息，用于饼图数据) 
	* @param type
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchEngineDaily> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSearchEngineDaily> getChart(@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	/**
	* @Title: indexAverageList
	* @Description: TODO(搜索引擎当前条件下统计信息，用于条形图 数据) 
	* @param type1
	* @param type2
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchEngineDaily> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSearchEngineDaily> indexAverageList(@Param("type1")String type1,@Param("type2")String type2,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	/**
	* @Title: average
	* @Description: TODO(求平均指标总的平均值) 
	* @param type1
	* @param type2
	* @param startTime
	* @param endTime
	* @return String 返回类型 
	* @author LiuYong 
	* @date 28 Dis 2016 8:34:05 PM
	 */
	String average(@Param("type1")String type1,@Param("type2")String type2,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	/**
	* @Title: getGroupList
	* @Description: TODO(搜索引擎当前条件下统计信息，用于折线图数据)
	* @param type
	* @param startTime
	* @param endTime 
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getGroupList(@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	/**
	* @Title: getDateList
	* @Description: TODO(搜索引擎当前条件下统计信息，用于折线图数据)
	* @param type
	* @param startTime
	* @param endTime 
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getDateList(@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);
		
	/**
	* @Title: getLine
	* @Description: TODO(搜索引擎当前条件下统计信息，用于折线图数据) 
	* @param engine_name
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getLine(@Param("engine_name")String engine_name,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	
	
	/**
	* @Title: getLine
	* @Description: TODO(搜索引擎当前条件下统计信息，用于折线图数据) 
	* @param engine_name
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<Map<String,Object>> getLine1(@Param("engine_name")String engine_name,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	


}
