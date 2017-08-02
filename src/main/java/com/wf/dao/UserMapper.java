package com.wf.dao;

import java.util.Map;

import com.wf.bean.Wfadmin;

public interface UserMapper {

	/**
	 *	查询后台登录用户信息 
	 */
	Wfadmin selectUserInfo(Map<String, String> map);

	Map<String,String> getAdminPurview(String wfAdminId);
}