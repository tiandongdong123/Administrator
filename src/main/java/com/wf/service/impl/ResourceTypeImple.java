package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.wf.Setting.ResourceTypeSetting;
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

	ResourceTypeSetting resourceTypeSetting = new ResourceTypeSetting();
	@Override
	public Boolean addResourceType(ResourceType resourceType) {

		Map<String , Object> map =new HashMap<String, Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		resourceType.setAddTime(df.format(new Date()));
		map.put("typeName", resourceType.getTypeName());
		map.put("typedescri",resourceType.getTypedescri());
		map.put("typeCode", resourceType.getTypeCode());
		//boolean bol =dao.findResourceTypeParam(map)==null?true:false;
		//在zookeeper中添加资源类型
		boolean bol = resourceTypeSetting.addResourceType(resourceType);
		boolean n=dao.insertResourceType(resourceType)>0?true:false;
		boolean b =bol && n;
		return b ;
	}

/*	@Override
	public PageList getResourceType(int pageNum, int pageSize) {

		PageList p=new PageList();
		Map<String,Object> mp=new HashMap<String, Object>();
		int pageN=(pageNum-1)*pageSize;
		mp.put("pageNum", pageN);
		mp.put("pageSize", pageSize);
		int pageTotal=2;
		List<Object>  pageRow=dao.selectResourceTypeInfor(mp);
		p.setPageNum(pageNum);
		p.setPageRow(pageRow);
		p.setPageSize(pageSize);
		p.setPageTotal(pageTotal);
		return p;
	}*/


	public PageList getResourceType(int pageNum, int pageSize){
		PageList p = new PageList();
		int pageN=(pageNum-1)*pageSize;
		int pageTotal=0;
		Map<String, ResourceType> mp = resourceTypeSetting.getResources();
		ArrayList<Object> list = new ArrayList<Object>();
		for (String key : mp.keySet()) {
			list.add(mp.get(key));
		}
		ArrayList<Object> pageRow = new ArrayList<>();
		if((list.size()-pageN)>=10){
			for(int i = 0 ; i< pageSize ; i++){
				pageRow.add(list.get(i+pageN));
			}
		}else{
			for(int i = 0 ; i<(list.size()-pageN);i++){
				pageRow.add(list.get(i+pageN));
			}
		}
		pageTotal=list.size()!=0 && list.size()%pageSize !=0?list.size()/pageSize+1:list.size()/pageSize;
		p.setPageNum(pageNum);
		p.setPageSize(pageSize);
		p.setPageRow(pageRow);
		p.setPageTotal(pageTotal);
		return p ;
	}


	@Override
	public ResourceType findResourceType(String id){
		ResourceType resourceType =dao.findResourceType(id);
		return resourceType;
	}
	@Override
	public PageList getResourceTypeByName(int pageNum,int pageSize,String typeName){
		PageList p = new PageList();
		Map<String,ResourceType> mp = resourceTypeSetting.findResourceTypeByName(typeName);
		ArrayList<Object> list = new ArrayList<Object>();
		for (String key : mp.keySet()) {
			list.add(mp.get(key));
		}
		p.setPageNum(pageNum);
		p.setPageSize(pageSize);
		p.setPageRow(list);
		p.setPageTotal(1);
		return p;
		}

	@Override
	public Boolean deleteResourceType(String ids) {
		int n=dao.deleteResourceType(ids);
		//删除zookeeper中的资源类
		resourceTypeSetting.deleResourceType(ids);
		boolean b=n>0?true:false;
		return b;
	}
	@Override
	public  Boolean moveUpResource(String id){
		boolean b = resourceTypeSetting.moveUpResource(id);
		return b;
	}
	@Override
	public  Boolean moveDownResource(String id){
		boolean b = resourceTypeSetting.moveDownResource(id);
		return b;
	}


	@Override
	public Boolean updateResourceType(ResourceType resourceType) {
		int n =dao.updateResourceType(resourceType);
		resourceTypeSetting.updateResouceType(resourceType);
		boolean b=n>0?true:false;
		return b;
	}
	@Override
	public boolean resourcePublish() {
		//清空redis中对应的key
		RedisUtil redis = new RedisUtil();
		redis.del(1,"resources");
		List<Object> list= dao.find();
		JSONArray jsonArr = JSONArray.fromObject(list);
		redis.set("resources", jsonArr.toString(),1);
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
		return resourceTypeSetting.getResources1();
		//return dao.getAll1();
	}

	@Override
	public int updateResourceTypeState(int typeState,String id){
		resourceTypeSetting.updateResourceTypeState(typeState, id);
		return dao.updateResourceTypeState(typeState, id);
	}

	@Override
	public List<Object> exportResource(String typeName) {

		List<Object> list=new ArrayList<Object>();
		try {

			if(StringUtils.isEmpty(typeName)) typeName=null;
			list=dao.selectResourceTypeInforAll(typeName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}
	@Override
	public Boolean checkRsTypeCode(String rsTypeCode) {
		boolean bol = resourceTypeSetting.checkTypeCode(rsTypeCode);
		String result = dao.checkRsTypeName(rsTypeCode);
		boolean b = bol && result==null?true:false;
		return b;
	}
	@Override
	public Boolean checkRsTypeName(String rsTypeName) {
		boolean bol = resourceTypeSetting.checkTypeName(rsTypeName);
		String result = dao.checkRsTypeName(rsTypeName);
		boolean b = bol && result==null?true:false;
		return b;
	}
	@Override
	public Boolean checkResourceForOne(String id){
		boolean result = resourceTypeSetting.checkResourceForOne(id);
		return result;
	}
}
