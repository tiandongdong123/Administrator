package com.wf.service;

import java.util.List;
import java.util.Map;

public interface PageAnalysisService {
	
	Map<String,Object> getline(String title,String age,String exlevel,String datetype,String reserchdomain,String pageName,Integer type,String starttime,String endtime);
	
	List<String> getmodular();
	
	List<Object> findAllPages();
	
	Object foemat(String age,String title,String exlevel,String reserchdomain,String pageName,String datetype,String type,String starttime,String endtime );
	
	Object getdatasource(String title,String age,String exlevel,String datetype,String reserchdomain,String type,String pageName,String starttime,String endtime);
	
	Object getonedatasource(String title,String age,String exlevel,String datetype,String reserchdomain,String type,String pageName,String starttime,String endtime);
}
