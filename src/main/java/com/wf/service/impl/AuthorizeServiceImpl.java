package com.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.Authorize;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.Provider;
import com.wf.dao.AuthorizeMapper;
import com.wf.dao.PersonMapper;
import com.wf.dao.ProviderMapper;
import com.wf.service.AuthorizeService;
import com.wf.service.PersonService;
@Service
public class AuthorizeServiceImpl implements AuthorizeService {

	@Autowired
	private AuthorizeMapper authorize;
	/**
	 * 授权列表
	 */
	@Override
	public PageList findAuthorizePage(String institutionId,Integer pagenum,Integer pagesize) {
		if(StringUtils.isBlank(institutionId)) institutionId = null;
		Authorize auth = new Authorize();
		auth.setInstitutionId(institutionId);
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			int startnum = (pagenum-1)*pagesize;
			r = authorize.getAuthorize(auth,0,pagesize);
			int num = this.authorize.getAuthorizeNum(auth);
			p.setPageRow(r);
			p.setPageNum(pagenum);
			p.setPageTotal(num);
			p.setPageSize(pagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	@Override
	public List<HashMap<String, Object>> getAuthorizelist(String userId,Integer pagenum,Integer pagesize) {
		PageList pl = this.findAuthorizePage(userId,pagenum,pagesize);
		List<Object> list =pl.getPageRow();//授权列表
		List<HashMap<String, Object>> r = new ArrayList<HashMap<String, Object>>();
		LinkedHashMap<String, Object> mp = null;
		for(int i=0;i<list.size();i++){
			Authorize auth =(Authorize)list.get(i);
			mp = new LinkedHashMap<String, Object>();
			mp.put("pageList", pl);
			mp.put("listSize",list.size());
			mp.put("id",auth.getId());
//			Person person =personMapper.queryPersonInfo(auth.getInstitutionId());//根据授权表中的机构id获取机构信息
//			mp.put("InstitutionName", person.getInstitution());
			mp.put("InstitutionId", auth.getInstitutionId());
			Provider pro =providerdao.getProvider(auth.getProviderId());//根据授权表中的资源提供商id获取资源提供商id
			mp.put("providerId", auth.getProviderId());
			mp.put("nameZh", pro.getNameZh());
			mp.put("providerName", pro.getProviderName());
			mp.put("username", auth.getUsername());
			mp.put("password", auth.getPassword());
			r.add(mp);
		}
		return r;
	}
	
	
	@Override
	public Authorize addAuthorize(String institutionId, Integer providerId, String username,
			String password) {
		Authorize a =new Authorize();
		//a.setId(GetUuid.getId());
		a.setInstitutionId(institutionId);
		a.setProviderId(providerId);
		a.setUsername(username);
		a.setPassword(password);
		int num =authorize.addAuthorize(a);
		if(num>0){
			return a;
		}
		return null;
	}

	@Autowired
	private PersonMapper persondao;
	@Autowired
	private PersonService personService;
	@Autowired
	private ProviderMapper providerdao;
	@Autowired
	private PersonMapper personMapper;
	@Override
	public List<HashMap<String, Object>> getAuthorize(Authorize a) {
		List<Object> lists=authorize.getAuthorize(a,null,null);
		List<HashMap<String, Object>> r = new ArrayList<HashMap<String, Object>>();
		LinkedHashMap<String, Object> mp = null;
		for(int i=0;i<lists.size();i++){
			Authorize au =(Authorize)lists.get(i);
			mp = new LinkedHashMap<String, Object>();
			mp.put("listSize",lists.size());
			mp.put("id",au.getId());
			Person person =personMapper.queryPersonInfo(au.getInstitutionId());
			mp.put("InstitutionName", person.getInstitution());
			mp.put("InstitutionId", au.getInstitutionId());
			Provider pro =providerdao.getProvider(au.getProviderId());
			mp.put("providerId", au.getProviderId());
			mp.put("providerName", pro.getProviderName());
			mp.put("username", au.getUsername());
			mp.put("password", au.getPassword());
			r.add(mp);
		}		
		return r;
	}
	@Override
	public HashMap<String, Object> getAuthorize(Integer id) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<String, Object>();
		Authorize au =authorize.getAuthorize2(id);
		mp = new LinkedHashMap<String, Object>();
		mp.put("id",au.getId());
		Person person =personMapper.queryPersonInfo(au.getInstitutionId());
		mp.put("InstitutionUsername", person.getUserId());
		mp.put("InstitutionName", person.getInstitution());
		mp.put("InstitutionId", au.getInstitutionId());
		Provider pro =providerdao.getProvider(au.getProviderId());
		mp.put("providerId", au.getProviderId());
		mp.put("providerName", pro.getProviderName());
		mp.put("username", au.getUsername());
		mp.put("password", au.getPassword());
		return mp;
	}

	
}
