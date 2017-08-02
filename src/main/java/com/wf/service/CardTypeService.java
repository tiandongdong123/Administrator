package com.wf.service;

import java.util.List;

import com.wf.bean.CardType;

public interface CardTypeService {
	
	int checkcode(CardType card);
	
	int addcode(CardType card);
	
	List<Object> getlist();
	
	CardType getOne(String code);
}
