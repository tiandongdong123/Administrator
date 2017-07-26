package com.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.ProResourceType;
import com.wf.bean.Provider;
import com.wf.dao.ProResourceTypeMapper;
import com.wf.dao.ProviderMapper;
import com.wf.service.ProviderService;


@Service
public class ProviderServiceImpl implements ProviderService{

	@Autowired
	private ProviderMapper provider;   //资源提供商 接口
	@Autowired
	private ProResourceTypeMapper resource;//资源类型接口
	
	@Override
	public List<HashMap<String, Object>> getProviders() {
		List<Provider> plist =provider.getProviders();
		List<HashMap<String, Object>> r = new ArrayList<HashMap<String, Object>>();
		LinkedHashMap<String, Object> mp = null;
		for(int i=0;i<plist.size();i++){
			mp = new LinkedHashMap<String, Object>();
			Provider p =plist.get(i);
			mp.put("id",p.getId());
			mp.put("nameZh",p.getNameZh());
			mp.put("providerName",p.getProviderName());
			mp.put("providerURL",p.getProviderURL());
			mp.put("size",plist.size());
			r.add(mp);
		}
		return r;
	}
	@Override//增加提供商
	public int addProvider(Provider provider1) {
		// TODO Auto-generated method stub
		return provider.addProvider(provider1);
	}
	@Override//删除提供商
	public boolean deleteProvider(Integer id) {
		// TODO Auto-generated method stub
		Integer providerId = id;
		System.out.println(providerId);
		List<ProResourceType> list = resource.findResourceNamesById(providerId);
		if(list.size() > 0){
			return false;
		}else{
		 boolean b = provider.deleteProvider(id);
		return b;
		}
	}
	
}
