package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.CardType;

public interface CardTypeMapper {

	
	int checkcode(CardType card);
	
	int addCode(CardType card);
	
	List<Object>  getlist();
	
	CardType getOne(@Param("code")String code);
}
