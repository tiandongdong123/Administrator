package com.wf.service;

import java.util.List;

import com.wf.bean.DB_Source;

public interface DB_SourceService {
	List<DB_Source> getInstitutionDB_Source(String institution);
}
