package com.wf.dao2;


import java.util.List;

import org.apache.ibatis.annotations.Param;


public interface WebSiteAttributeMapper {
	
	
	
	List<Object> findWebsiteAttributeAge(@Param("startTime")String startTime,@Param("endTime")String endTime);//年龄
	List<Object> findWebsiteAttributeGender(@Param("startTime")String startTime,@Param("endTime")String endTime);//男女
	List<Object> findWebsiteAttributeEducation(@Param("startTime")String startTime,@Param("endTime")String endTime);//教育程度
	List<Object> findWebsiteAttributeJobTitle(@Param("startTime")String startTime,@Param("endTime")String endTime);//职称
	List<Object> findWebsiteAttributeInterest(@Param("startTime")String startTime,@Param("endTime")String endTime);//感兴趣
	
}
