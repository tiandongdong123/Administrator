package com.wf.service;

import com.wf.bean.PageList;

public interface PriceUnitService {

	PageList getUnit(Integer pagenum,Integer pagesize);
	
	boolean	deleteUnit(String[] ids);
	
	boolean checkUnit(String unitname,String unitcode);
	
	boolean doAddUnit(String unitname,String unitcode);
	
	boolean doUpdateUnit(String unitname,String unitcode,Integer id);
}
