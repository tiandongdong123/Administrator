package com.wf.dao;

import java.util.List;
import java.util.Map;

public interface FunctionPageMapper {
	
	List<Object> modelanalysis_table(Map<String, Object> map);
	List<Object> modelanalysis_count(Map<String,Object> map);
	List<Object> modelanalysis_view(Map<String,Object> map);
	
	List<Object> pageAnalysis_table(Map<String, Object> map);
	List<Object> pageAnalysis_count(Map<String, Object> map);
	List<Object> pageAnalysis_view(Map<String, Object> map);
	
	List<Object> functionProfile_table(Map<String, Object> map);
	List<Object> functionProfile_count(Map<String, Object> map);
	List<Object> functionProfile_view(Map<String, Object> map);

	
}
