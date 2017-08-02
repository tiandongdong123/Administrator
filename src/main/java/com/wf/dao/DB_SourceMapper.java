package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.DB_Source;

public interface DB_SourceMapper {
	
	List<DB_Source> getAllDB();
	
    List<Object> checkSName(@Param("name") String name,@Param("code") String code);
    
    int doAddSName(DB_Source db);
    
    DB_Source getOneSource(@Param("code") String code);
}