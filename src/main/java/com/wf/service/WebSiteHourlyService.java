package com.wf.service;

import java.util.HashMap;
import com.wf.bean.PageList;

public interface WebSiteHourlyService {

	
	HashMap<String,Object> findPageViewHourly(Integer type,String dateType);

	HashMap<String, Object> contrastPageViewHourly(String dateType,String contrastDate, Integer type);
	PageList basicIndexNumHourly(String dateType, Integer pagenum, Integer pagesize);
}
