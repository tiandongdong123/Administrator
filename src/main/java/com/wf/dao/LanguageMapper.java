package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Language;

public interface LanguageMapper {

	List<Language> getAllLanguage();
	
    List<Object> checkLName(@Param("name") String name, @Param("code") String code);
    
    int doAddLName(Language la);
    
    Language getOne( @Param("code") String code);
}