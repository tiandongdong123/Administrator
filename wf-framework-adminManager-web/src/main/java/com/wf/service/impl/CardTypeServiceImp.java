package com.wf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.CardType;
import com.wf.dao.CardTypeMapper;
import com.wf.service.CardTypeService;

@Service
public class CardTypeServiceImp  implements CardTypeService{
	
	@Autowired
	private CardTypeMapper cardmapper;
	
	@Override
	public int checkcode(CardType card) {
		// TODO Auto-generated method stub
		int i=cardmapper.checkcode(card);
		return i;
	}

	@Override
	public int addcode(CardType card) {
		// TODO Auto-generated method stub
		int i=cardmapper.addCode(card);
		return i;
	}

	@Override
	public List<Object> getlist() {
		// TODO Auto-generated method stub
		return cardmapper.getlist();
	}

	@Override
	public CardType getOne(String code) {
		// TODO Auto-generated method stub
		return cardmapper.getOne(code);
	}
	

}
