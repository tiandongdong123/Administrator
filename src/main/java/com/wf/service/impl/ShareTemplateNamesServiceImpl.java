package com.wf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.ShareTtemplateNames;
import com.wf.dao.ShareTemplateNamesMapper;
import com.wf.service.ShareTemplateNamesService;

@Service
public class ShareTemplateNamesServiceImpl implements ShareTemplateNamesService {

	@Autowired
	private ShareTemplateNamesMapper shareTemplateNamesMapper;

	@Override
	public List<ShareTtemplateNames> getAllShareTemplateNames() {
		return shareTemplateNamesMapper.getAllShareTemplateNames();
	}

}
