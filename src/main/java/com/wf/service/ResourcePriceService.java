package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.PageList;
import com.wf.bean.ResourcePrice;
import com.wf.bean.ResourceType;

public interface ResourcePriceService {

	PageList getPrice(String name,Integer pagesize,Integer pagenum);
	
	boolean delectPrice(String[] ids);
	
	List<ResourceType>  getRtype();
	
	Map<String,Object> getResource();
	
	boolean checkPriceRID(String name,String rid);
	
	boolean doAddPrice(ResourcePrice rp);
	
	Map<String,Object> getRP(String id);
	
	boolean doUpdatePrice(ResourcePrice rp);
}
