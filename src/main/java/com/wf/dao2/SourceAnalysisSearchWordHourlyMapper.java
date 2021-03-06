package com.wf.dao2;

/**
 *	检索词---liuYong
 */
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.SourceAnalysisSearchWordHourly;

public interface SourceAnalysisSearchWordHourlyMapper {
	
	
	/**
	* @Title: getPage
	* @Description: TODO(检索词当前条件下查询信息，用于列表数据，小时为周期单位) 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return List<SourceAnalysisSearchWordHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSearchWordHourly> getPage(@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

	
	
	List<SourceAnalysisSearchWordHourly> getPage_day(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);
	
	
	/**
	* @Title: getPages
	* @Description: TODO(检索词当前条件下查询所有信息，用于列表数据)
	* @param date 
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSearchWordHourly> getPages(@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);

	
	
	/**
	* @Title: getPages
	* @Description: TODO(检索词当前条件下查询所有信息，用于列表数据)
	* @param date 
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSearchWordHourly> getPages_day(@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	
	/**
	* @Title: getChart
	* @Description: TODO(检索词当前条件下统计信息，用于饼图数据，小时为周期单位) 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSearchWordHourly> getChart(@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: getChart_day
	* @Description: TODO(检索词当前条件下统计信息，用于饼图数据，小时为周期单位) 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSearchWordHourly> getChart_day(@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	/**
	* @Title: indexAverageList
	* @Description: TODO(检索词当前条件下统计信息，用于条形图 数据) 
	* @param type1
	* @param type2
	* @param date
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSearchWordHourly> indexAverageList(@Param("type1")String type1,@Param("type2")String type2,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: indexAverageList_day
	* @Description: TODO(检索词当前条件下统计信息，用于条形图 数据) 
	* @param type1
	* @param type2
	* @param date
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSearchWordHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSearchWordHourly> indexAverageList_day(@Param("type1")String type1,@Param("type2")String type2,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
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
	String average(@Param("type1")String type1,@Param("type2")String type2,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: average_day
	* @Description: TODO(求平均指标总的平均值) 
	* @param type1
	* @param type2
	* @param startTime
	* @param endTime
	* @return String 返回类型 
	* @author LiuYong 
	* @date 28 Dis 2016 8:34:05 PM
	 */
	String average_day(@Param("type1")String type1,@Param("type2")String type2,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	/**
	* @Title: getGroupList
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据,小时为周期单位)
	* @param type
	* @param startTime
	* @param endTime 
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getGroupList(@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: getGroupList_day
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据,小时为周期单位)
	* @param type
	* @param startTime
	* @param endTime 
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getGroupList_day(@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	/**
	* @Title: getDateList
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据,小时为周期单位)
	* @param type
	* @param startTime
	* @param endTime 
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getDateList(@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: getDateList_day
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据,小时为周期单位)
	* @param type
	* @param startTime
	* @param endTime 
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getDateList_day(@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	/**
	* @Title: getLine
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据,小时为周期单位) 
	* @param keyWord
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getLine(@Param("keyWord")String keyWord,@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: getLine_day
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据,小时为周期单位) 
	* @param keyWord
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getLine_day(@Param("keyWord")String keyWord,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	
	/**
	* @Title: getLine
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据,小时为周期单位) 
	* @param keyWord
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<Map<String, Object>> getLine1(@Param("keyWord")String keyWord,@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);

	
	
	/**
	* @Title: getLine1_day
	* @Description: TODO(检索词当前条件下统计信息，用于折线图数据,小时为周期单位) 
	* @param keyWord
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<Map<String, Object>> getLine1_day(@Param("keyWord")String keyWord,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	

}
