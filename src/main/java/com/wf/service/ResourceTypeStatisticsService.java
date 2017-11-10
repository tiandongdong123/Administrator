package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.PageList;
import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceType;

public interface ResourceTypeStatisticsService {

	List<ResourceType> getResourceType();

	Map<String, Object> getAllLine(Integer num,String starttime,String endtime,
			ResourceStatistics res,Integer[] urls,Integer singmore,String[] title,String[] database_name);
	
	PageList gettable(Integer num,String starttime,String endtime,
			ResourceStatistics res,Integer pagenum,Integer pagesize);
	
	List<Object> exportresourceType(Integer num,String starttime,String endtime,
			ResourceStatistics res);
}
