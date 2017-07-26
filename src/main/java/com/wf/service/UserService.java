package com.wf.service;


import java.util.List;
import java.util.Map;

import com.wf.bean.SystemMenu;
import com.wf.bean.Wfadmin;

public interface UserService {

	Wfadmin getuser(String userName,String passWord);
	
	/**
	* @Title: findPurview
	* @Description: TODO(通过menuId找对应菜单名称) 
	* @param id
	* @return String 返回类型 
	* @author LiuYong 
	* @date 11 Dis 2016 10:36:46 AM
	 */
	SystemMenu findPurviewById(String id);

	Map<String,String> getAdminPurview(String wfAdminId);
}
