package com.wf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.DB_Source;
import com.wf.dao.DB_SourceMapper;
import com.wf.service.DB_SourceService;

@Service
public class DB_SourceServiceImpl implements DB_SourceService {

	@Autowired
	private DB_SourceMapper mapper;

	@Override
	public List<DB_Source> getInstitutionDB_Source(String institution) {
		return mapper.getInstitutionDB_Source(institution);
	}

	@Override
	public List<DB_Source> selectAll() {
		List<DB_Source> list= mapper.selectAll();
		return list;
	}

}
