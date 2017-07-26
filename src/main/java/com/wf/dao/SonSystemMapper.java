package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.SonSystem;

public interface SonSystemMapper {

	List<Object> getSon(@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
	
	int getSonNum();

	
	int deleteSon(@Param("ids") String[] ids);
	
	List<Object> checkSon(SonSystem son);
	
	int doAddSon(SonSystem son);
	
	int doUpdateSon(SonSystem son);
	
	List<String> getRsonName();
	
	List<SonSystem> getAll();
	
	SonSystem getOneSon(@Param("sonCode") String sonCode);
}
