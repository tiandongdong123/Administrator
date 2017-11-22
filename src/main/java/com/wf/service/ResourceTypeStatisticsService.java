package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.PageList;
import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceStatisticsHour;
import com.wf.bean.ResourceType;

public interface ResourceTypeStatisticsService {

	List<ResourceType> getResourceType();

	Map<String, Object> getAllLine(String starttime,String endtime,
			ResourceStatistics res,Integer[] urls,Integer singmore,String[] database_name);
	
	Map gettable(Integer num,String starttime,String endtime,
			ResourceStatistics res,Integer pagenum,Integer pagesize);
	
	List<ResourceStatisticsHour> exportresourceType(Integer num, String starttime, String endtime,
													ResourceStatistics res);
}
