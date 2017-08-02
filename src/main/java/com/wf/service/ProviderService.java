package com.wf.service;

import java.util.HashMap;
import java.util.List;

import com.wf.bean.Provider;

public interface ProviderService {

	List<HashMap<String, Object>> getProviders();
	
	int addProvider(Provider provider);//添加总院提供商
	boolean deleteProvider(Integer id);//删除供应商
	
}
