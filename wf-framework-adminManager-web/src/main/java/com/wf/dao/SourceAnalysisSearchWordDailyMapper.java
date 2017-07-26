package com.wf.dao;

/**
 *	检索词---liuYong
 */
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.SourceAnalysisSearchWordDaily;

public interface SourceAnalysisSearchWordDailyMapper {
	
	
	/**
	* @Title: getPage
	* @Description: TODO(检索词当前条件下查询信息，用于列表数据) 
	* @param type
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return List<SourceAnalysisSearchWordDaily> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSearchWordDaily> getPage(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

	/**
	* @Title: getPages
	* @Description: TODO(检索词当前条件下查询信息，用于列表数据) 
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordDaily> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSearchWordDaily> getPages(@Param("startTime")String startTime,@Param("endTime")String endTime);

	/**
	* @Title: getChart
	* @Description: TODO(检索词当前条件下统计信息，用于饼图数据) 
	* @param type
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordDaily> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSearchWordDaily> getChart(@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	/**
	* @Title: indexAverageList
	* @Description: TODO(检索词当前条件下统计信息，用于条形图数据) 
	* @param type1
	* @param type2
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordDaily> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSearchWordDaily> indexAverageList(@Param("type1")String type1,@Param("type2")String type2,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
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
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据)
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
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据)
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
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据) 
	* @param engine_name
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getLine(@Param("keyWord")String keyWord,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);



}
