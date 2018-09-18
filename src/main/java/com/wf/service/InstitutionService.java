package com.wf.service;

import java.util.Map;

public interface InstitutionService {
	/** 一键发布数据到solr中 */
	public Map<String,String> setSolrData();
}
