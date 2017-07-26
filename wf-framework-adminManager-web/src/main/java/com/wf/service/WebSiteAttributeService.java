package com.wf.service;

import java.util.HashMap;





public interface WebSiteAttributeService {

	/**
	* 根据时间查询 访客属性 echarts图
	 */
	HashMap<String,Object> findWebsiteAttribute(String dateType);



	}
