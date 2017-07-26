package com.wf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.ShareTemplateNamesFileds;
import com.wf.dao.ShareTemplateNamesFiledsMapper;
import com.wf.service.ShareTemplateNamesFiledsService;

@Service
public class ShareTemplateNamesFiledsServiceImpl implements ShareTemplateNamesFiledsService{

	@Autowired
	private ShareTemplateNamesFiledsMapper filedsMapper;
	
	@Override
	public List<ShareTemplateNamesFileds> getFiledsByShareNameId(Integer id) {
		return filedsMapper.getFiledsByShareNameId(id);
	}

	@Override
	public List<ShareTemplateNamesFileds> getFiledsByShareNameType(String type) {
		return filedsMapper.getFiledsByShareNameType(type);
	}

}
