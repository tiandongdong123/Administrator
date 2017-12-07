package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ecs.xhtml.map;
import org.apache.ibatis.annotations.Param;

public interface FunctionPageDailyMapper {
	
	
	List<String> getAllTopic(@Param("topic")String topic);
	
	List<Object> modelanalysis_table(Map<String, Object> map);
	List<Object> modelanalysis_count(Map<String,Object> map);
	List<Object> modelanalysis_view(Map<String,Object> map);
	
	List<Object> pageAnalysis_table(Map<String, Object> map);
	List<Object> pageAnalysis_count(Map<String, Object> map);
	List<Object> pageAnalysis_view(Map<String, Object> map);
	
	List<Object> functionProfile_table(Map<String, Object> map);
	List<Object> functionProfile_count(Map<String, Object> map);
	List<Object> functionProfile_view(Map<String, Object> map);
	
	
	/**
	 * 一个月内所有登录网站的独立访客数。
	 * @param date
	 * @return
	 */
	List<Object> getMAU(Map<String, Object> map);
}