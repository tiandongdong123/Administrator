package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.wf.bean.Language;
import com.wf.dao.LanguageMapper;
import com.wf.service.LanguageService;

public class LanguageServiceImpl implements LanguageService {

	@Autowired
	private LanguageMapper language;
	@Override
	public List<Language> getAllLanguage() {
		List<Language> li = new ArrayList<Language>();
		try {
		li =this.language.getAllLanguage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return li;
	}

}
