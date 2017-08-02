package com.wf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PSubjectCategory;
import com.wf.bean.ProResourceType;
import com.wf.dao.PSubjectCategoryMapper;
import com.wf.dao.ProResourceTypeMapper;
import com.wf.service.ProResourceTypeService;
@Service
public class ProResourceTypeServiceImpl implements ProResourceTypeService {
	@Autowired
	private ProResourceTypeMapper dao;
	@Autowired
	private PSubjectCategoryMapper subject;
	/**
	 * 根据资源提供商id 查询想、资源类型
	 * */
	@Override
	public List<ProResourceType> findResourceNamesById(Integer providerId) {
		// TODO Auto-generated method stub
		List<ProResourceType> list = dao.findResourceNamesById(providerId);
		return list;
	}
	@Override//添加资源类型
	public int addProResourceType(Integer providerId, String resourceName) {
		// TODO Auto-generated method stub
		int i =-1;
		i=dao.addProResourceType(providerId, resourceName);
		return i;
	}
	@Override//修改资源类型
	public boolean updateProResourceType(Integer providerId,
			String resourceName, Integer id) {
		// TODO Auto-generated method stub
		boolean b=dao.updateProResourceType(providerId, resourceName, id);
		return b;
	}
	@Override//删除资源类型
	public boolean deleteProResourceType(Integer id) {
		// TODO Auto-generated method stub
		Integer proResourceId =id;
		List<PSubjectCategory> list = subject.getPSubjectCategory(null,proResourceId);
		if(list.size() > 0){
			return false;
		}else{
		boolean bl = dao.deleteProResourceType(id);
		return bl;
		}
	}

}
