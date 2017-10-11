package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.Datamanager;
import com.wf.bean.PageList;

public interface DataManagerService {

	PageList getData(String dataname,Integer pagenum,Integer pagesize);
	
	boolean deleteData(String id);
	
	boolean closeData(String id);
	
	boolean openData(String id);
	
	Map<String,Object> getResource();
	
	Map<String,Object> getSubject();
	
	boolean checkLName(String name,String code);
	
	boolean doAddLName(String name,String code);
	
	boolean checkSName(String name,String code);
	
	boolean doAddSName(String name,String code);
	
	boolean checkDname(String name);
	
	boolean doAddData(String[] customs,Datamanager data);
	
	Map<String,Object> getCheck(String id);
	
	List<String> getCheckids(String id);
	
	boolean doUpdateData(String[] customs,Datamanager data);
	
	List<Datamanager> selectAll();
	
	Datamanager getDataManagerBySourceCode(String productSourceCode);

	void selectZY();
	
	List<Object> exportData(String dataname);
	
	List<Datamanager> getDatabaseByCode(String code);
}
