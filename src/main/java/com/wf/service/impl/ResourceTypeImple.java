package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.RedisUtil;
import com.wf.bean.PageList;
import com.wf.bean.ResourceType;
import com.wf.dao.ResourceTypeMapper;
import com.wf.service.ResourceTypeService;
@Service
public class ResourceTypeImple implements ResourceTypeService {
	@Autowired
	ResourceTypeMapper dao;
	RedisUtil redis = new RedisUtil();
	@Override
	public Boolean addResourceType(ResourceType resourceType) {
		Map<String , Object> map =new HashMap<String, Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		resourceType.setAddTime(df.format(new Date()));
		map.put("typeName", resourceType.getTypeName());
		map.put("typeCode", resourceType.getTypeCode());
		boolean bol =dao.findResourceTypeParam(map)==null?true:false;
		boolean n=false;
		if(bol==true){
			n=dao.insertResourceType(resourceType)>0?true:false;
		}
		boolean b=bol && n;
		return b;
	}
	@Override
	public PageList getResourceType(int pageNum, int pageSize, String typeName) {
		if(StringUtils.isEmpty(typeName)) typeName=null;
		PageList p=new PageList();
		Map<String,Object> mp=new HashMap<String, Object>();		
		int pageN=(pageNum-1)*pageSize;
		mp.put("pageNum", pageN);
		mp.put("pageSize", pageSize);
		mp.put("typeName", typeName);		
		List<Object>  pageRowAll=dao.selectResourceTypeInforAll(typeName);
		int pageTotal=0;
		int b =pageRowAll.size()%pageSize;
		pageTotal=pageRowAll.size()!=0 && b !=0?pageRowAll.size()/pageSize+1:pageRowAll.size()/pageSize;
		List<Object>  pageRow=dao.selectResourceTypeInfor(mp);
		p.setPageNum(pageNum);
		p.setPageRow(pageRow);
		p.setPageSize(pageSize);
		p.setPageTotal(pageTotal);
		return p;
	}
	
	
	@Override
	public ResourceType findResourceType(String id){
		ResourceType resourceType =dao.findResourceType(id);
		return resourceType;
	}
	@Override
	public Boolean deleteResourceType(String ids) {
		int n=dao.deleteResourceType(ids);
		boolean b=n>0?true:false;
		return b;
	}
	@Override
	public Boolean updateResourceType(ResourceType resourceType) {
		int n =dao.updateResourceType(resourceType);
		boolean b=n>0?true:false;
		return b;
	}
	@Override
	public boolean resourcePublish() {
		//清空redis中对应的key
		redis.del("resources");
		List<Object> list= dao.find();
		JSONArray jsonArr = JSONArray.fromObject(list);
		redis.set("resources", jsonArr.toString(),1);
//		for(int i = 0;i < list.size();i++){
//			ResourceType m = (ResourceType) list.get(i);
//			String object = JSONObject.fromObject(m).toString();
//			redis.zadd("pageResource", i, m.getId());//发布到redis
//			redis.hset("resources", m.getId(), object);
//		}
		
		
		return false;
	}
	@Override
	public List<ResourceType> getAll() {
		// TODO Auto-generated method stub
		return dao.getAll();
	}
	@Override
	public JSONArray getAll1() {
		// TODO Auto-generated method stub
		return dao.getAll1();
	}
	
	@Override
	public List<Object> exportResource(String typeName) {
		
		List<Object> list=new ArrayList<Object>();
		try {
			
			if(StringUtils.isEmpty(typeName)) typeName=null;
			Map<String,Object> mp=new HashMap<String, Object>();		
			mp.put("typeName", typeName);		
			list=dao.selectResourceTypeInforAll(typeName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	@Override
	public String checkRsTypeCode(String rsTypeCode) {
		return dao.checkRsTypeCode(rsTypeCode);
	}
	@Override
	public String checkRsTypeName(String rsTypeName) {
		return dao.checkRsTypeName(rsTypeName);
	}
}
