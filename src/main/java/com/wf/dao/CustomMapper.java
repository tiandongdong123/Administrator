package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Custom;

public interface CustomMapper {

	int doAddCustom(List<Custom> cs);
	
	List<Custom> getCustomById(@Param("dbid") String id);
	
	int doDeleteCustom(@Param("dbid") String id);
	
}