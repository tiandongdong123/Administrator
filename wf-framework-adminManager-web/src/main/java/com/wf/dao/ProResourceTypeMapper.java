package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ProResourceType;

public interface ProResourceTypeMapper {
	/**
	 * 根据资源提供商id 查询资源类型
	 * */
	List<ProResourceType> findResourceNamesById(@Param("providerId")Integer providerId);
	/**
	 * 根据id 查询资源类型 新增加方法
	 * */
	//通过id找资源类型
	ProResourceType getProResourceType(@Param("id")Integer id);
	//添加资源类型
	int addProResourceType(@Param("providerId")Integer providerId,@Param("resourceName")String resourceName);
	//修改资源类型
	boolean updateProResourceType(@Param("providerId")Integer providerId,@Param("resourceName")String resourceName,@Param("id")Integer id);
	//删除资源类型
	boolean deleteProResourceType(@Param("id")Integer id);
	
}
