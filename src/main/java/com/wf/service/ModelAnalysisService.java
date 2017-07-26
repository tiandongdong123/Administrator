package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.PageList;

public interface ModelAnalysisService {
	
	Map<String,Object> getline(String title,String age,String exlevel,String datetype,String model,Integer type,String starttime,String endtime,String domain,Integer property);
	
	List<String> getmodular();
	
	PageList gettable(Integer pagesize,Integer pagenum,String title,String age,String exlevel,String datetype,String model,String starttime,String endtime,Integer type,String domain,Integer property);

}
