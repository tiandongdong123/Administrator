package com.wf.service;

import java.util.List;

import com.wf.bean.ProResourceType;

public interface ProResourceTypeService {
	/**
	 * 根据资源提供商id 查询想、资源类型
	 * */
	List<ProResourceType> findResourceNamesById(Integer providerId);
	//添加资源类型
	int addProResourceType(Integer providerId,String resourceName);
	//修改资源类型
	boolean updateProResourceType(Integer providerId,String resourceName,Integer id);
	//删除资源类型
	boolean deleteProResourceType(Integer id);
}
