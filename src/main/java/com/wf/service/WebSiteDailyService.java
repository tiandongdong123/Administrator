package com.wf.service;

import java.util.HashMap;
import com.wf.bean.PageList;





public interface WebSiteDailyService {

	/**
	* 查询昨天访问量echarts
	 */
	HashMap<String,Object> findPageView(Integer type,String dateType);
	
//对比时间查询e-charts  contrastPageView
	HashMap<String,Object> contrastPageView(String dateType,String contrastDate,Integer type);
	
	//List<Object> findPageViewNum(String dateType);
	//查询基础指标列表  按日
	PageList basicIndexNum(String dateType, Integer pagenum, Integer pagesize);
	// 查询访问总量
	Object selectSumNumbers(String dateType);
	}
