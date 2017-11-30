package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.PageList;

public interface PageAnalysisService {
	
	Map<String,Object> getline(String title,String age,String exlevel,String datetype,String reserchdomain,String pageName,Integer type,String starttime,String endtime);
	
	List<String> getmodular();
	
	List<Object> findAllPages();
	
	List<String> getAllTopic(String topic);
	
	Object foemat(String age,String title,String exlevel,String reserchdomain,String pageName,String datetype,String type,String starttime,String endtime,Integer property);
	
	PageList getdatasource(Integer pagesize,Integer pagenum,String title,String age,String exlevel,String datetype,String reserchdomain,String pageName,String starttime,String endtime,Integer property);
	
	Object getonedatasource(String title,String age,String exlevel,String datetype,String reserchdomain,String type,String pageName,String starttime,String endtime);
}
