package com.wf.service;

import java.util.List;

import com.wf.bean.PageList;
import com.wf.bean.SonSystem;

public interface SonSystemService {

	PageList getSon(Integer pagenum,Integer pagesize);
	
	boolean	deleteSon(String[] ids);
	
	boolean checkSon(SonSystem son);
	
	boolean doAddSon(SonSystem son);
	
	boolean doUpdateSon(SonSystem son);
	
	List<SonSystem> getAll();
	
	SonSystem getOneSon(String sonCode);
}
