package com.wf.service;

import java.util.Map;

import com.wf.bean.PageList;

public interface FunctionProfileService {
	
	Map<String,Object> getline(String title,String age,String exlevel,String datetype,String type,String starttime,String endtime,String domain,Integer property);
	Map<String,Object> indexanalysis(String title,String age,String exlevel,String datetype,String type,String starttime,String endtime,String domain,Integer property);
	PageList gettable(Integer pagesize,Integer pagenum,String title,String age,String exlevel,String datetype,Integer type,String starttime,String endtime,String domain,Integer property);
	
}
