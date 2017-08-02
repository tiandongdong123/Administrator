package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceStatisticsHour;


public interface ResourceStatisticsHourMapper {

	
	 List<ResourceStatisticsHour> getLine(@Param("starttime")String starttime,@Param("endtime")String endtime,
			 @Param("res")ResourceStatistics res,@Param("date") String date);
	
	
    List<ResourceStatisticsHour> gethourtable(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res,@Param("date") String date);

    List<ResourceStatisticsHour> gethouronetable(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res,@Param("date") String date);

    List<ResourceStatisticsHour> getLineMore(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res,@Param("urls")Integer[] urls,@Param("date") String date);
    
    
    List<ResourceStatisticsHour> getLineMoreByCheckMore(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res,@Param("sourceTypeName")String[]sourceTypeName,@Param("urls")Integer[] urls,@Param("date") String date);
    
    List<ResourceStatisticsHour> getLineByCheckMore(@Param("starttime")String starttime,@Param("endtime")String endtime,
			 @Param("res")ResourceStatistics res,@Param("sourceTypeName")String[]sourceTypeName,@Param("date") String date);
    
}