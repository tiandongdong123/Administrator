package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ecs.xhtml.map;

public interface FunctionPageDailyMapper {
	
	List<Object> getTable(Map<String, Object> map);
	List<Object> getCount(Map<String,Object> map);
	List<Object> getView(Map<String,Object> map);
	
	List<Object> pageAnalysis_table(Map<String, Object> map);
	List<Object> pageAnalysis_count(Map<String, Object> map);
	List<Object> pageAnalysis_view(Map<String, Object> map);
	
	List<Object> functionProfile_table(Map<String, Object> map);
	List<Object> functionProfile_count(Map<String, Object> map);
	List<Object> functionProfile_view(Map<String, Object> map);
}
