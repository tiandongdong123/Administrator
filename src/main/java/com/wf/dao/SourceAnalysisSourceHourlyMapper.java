package com.wf.dao;

/**
 *	全部来源---liuYong(小时为统计单位)
 */
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.SourceAnalysisSourceHourly;

public interface SourceAnalysisSourceHourlyMapper {
	
	/**
	* @Title: getPage
	* @Description: TODO(全部来源当前条件下查询信息，用于列表数据，小时为周期单位) 
	* @param date 
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return List<SourceAnalysisSourceHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSourceHourly> getPage(@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

	
	/**
	* @Title: getPage
	* @Description: TODO(全部来源当前条件下查询信息，用于列表数据，小时为周期单位) 
	* @param date 
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return List<SourceAnalysisSourceHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSourceHourly> getPage_day(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

	
	/**
	* @Title: getPages
	* @Description: TODO(全部来源当前条件下查询信息，用于列表数据) 
	* @param date
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSourceHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSourceHourly> getPages(@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);

	/**
	* @Title: getPages
	* @Description: TODO(全部来源当前条件下查询信息，用于列表数据) 
	* @param date
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSourceHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:04 PM
	 */
	List<SourceAnalysisSourceHourly> getPages_day(@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	/**
	* @Title: getChart
	* @Description: TODO(全部来源当前条件下统计信息，用于饼图数据,小时为周期单位) 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSourceHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSourceHourly> getChart(@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: getChart
	* @Description: TODO(全部来源当前条件下统计信息，用于饼图数据,小时为周期单位) 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSourceHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSourceHourly> getChart_day(@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);
	/**
	* @Title: indexAverageList
	* @Description: TODO(全部来源当前条件下统计信息，用于条形图 数据) 
	* @param type1
	* @param type2
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSourceHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSourceHourly> indexAverageList(@Param("type1")String type1,@Param("type2")String type2,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: indexAverageList
	* @Description: TODO(全部来源当前条件下统计信息，用于条形图 数据) 
	* @param type1
	* @param type2
	* @param startTime
	* @param endTime
	* @return List<SourceAnalysisSourceHourly> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<SourceAnalysisSourceHourly> indexAverageList_day(@Param("type1")String type1,@Param("type2")String type2,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
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
	String average_day(@Param("type1")String type1,@Param("type2")String type2,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	
	/**
	* @Title: getGroupList
	* @Description: TODO(全部来源当前条件下统计信息，用于折线图数据,小时为周期单位)
	* @param type
	* @param startTime
	* @param endTime 
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getGroupList(@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: getGroupList
	* @Description: TODO(全部来源当前条件下统计信息，用于折线图数据,小时为周期单位)
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
	* @Description: TODO(全部来源当前条件下统计信息，用于折线图数据,小时为周期单位)
	* @param type
	* @param startTime
	* @param endTime 
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getDateList(@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: getDateList
	* @Description: TODO(全部来源当前条件下统计信息，用于折线图数据,小时为周期单位)
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
	* @Description: TODO(全部来源当前条件下统计信息，用于折线图数据,小时为周期单位) 
	* @param access_type
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getLine(@Param("access_type")String access_type,@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);
	
	
	/**
	* @Title: getLine
	* @Description: TODO(全部来源当前条件下统计信息，用于折线图数据,小时为周期单位) 
	* @param access_type
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<String> getLine_day(@Param("access_type")String access_type,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	
	
	/**
	* @Title: getLine
	* @Description: TODO(全部来源当前条件下统计信息，用于折线图数据,小时为周期单位) 
	* @param access_type
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<Map<String, Object>> getLine1(@Param("access_type")String access_type,@Param("type")String type,@Param("date")String date,@Param("startTime")Integer startTime,@Param("endTime")Integer endTime);

	
	/**
	* @Title: getLine
	* @Description: TODO(全部来源当前条件下统计信息，用于折线图数据,小时为周期单位) 
	* @param access_type
	* @param type
	* @param startTime
	* @param endTime
	* @return List<String> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 4:18:29 PM
	 */
	List<Map<String, Object>> getLine1_day(@Param("access_type")String access_type,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);

	
	

}
