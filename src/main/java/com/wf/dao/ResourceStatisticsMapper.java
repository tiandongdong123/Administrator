package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ResourceStatistics;

public interface ResourceStatisticsMapper {
    
    List<ResourceStatistics> getLine(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res);

    List<ResourceStatistics> gettable(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res);
    
    List<ResourceStatistics> getonetable(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res);
    
    List<ResourceStatistics> getLineMore(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res,@Param("urls")Integer[] urltype);
    
    List<ResourceStatistics> getLineMoreByCheckMore(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res,@Param("sourceTypeName")String[]sourceTypeName,@Param("urls")Integer[] urltype);
    
    
    List<ResourceStatistics> getLineByCheckMore(@Param("starttime")String starttime,@Param("endtime")String endtime,
    		@Param("res")ResourceStatistics res,@Param("sourceTypeName")String[]sourceTypeName);
}