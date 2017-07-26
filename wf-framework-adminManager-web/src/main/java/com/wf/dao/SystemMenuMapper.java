package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.SystemMenu;

public interface SystemMenuMapper {

	List<SystemMenu> getPurview();
	
	/**
	* @Title: findPurviewById
	* @Description: TODO(通过id找对象数据) 
	* @return SystemMenu 返回类型 
	* @author LiuYong 
	* @param id 
	* @date 11 Dis 2016 10:55:15 AM
	 */
	public SystemMenu findPurviewById(@Param("id")String id);
}