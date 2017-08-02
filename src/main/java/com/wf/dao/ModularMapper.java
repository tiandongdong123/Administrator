package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Modular;

public interface ModularMapper {

    
	List<Object> getModularList();

	int doAddModular(Modular md);

	int getPageNum(@Param("ids") String[] ids);

	List<Object> getPage(@Param("ids") String[] ids,@Param("pageNum") Integer startNum,@Param("pageSize") Integer pageSize);

	Modular getModularById(@Param("id") String id);

	int doUpdateModular(Modular md);

	int deleteModular(@Param("id") String id);
	
	List<String> getModularNameList();
	
	List<Object> getModularAll(@Param("ids")String[]ids);
	
    
}