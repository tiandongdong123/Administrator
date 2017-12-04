package com.wf.dao;

import com.wf.bean.UserInstitution;


public interface UserInstitutionMapper {
	
	/**
	 * 添加用户机构权限
	 * @param com
	 */
	void addUserIns(UserInstitution user);
	
	/**
	 * 删除用户机构权限
	 * @param com
	 * @return
	 */
	int deleteUserIns(String userId);
	/**
	 * 获取
	 * @param userId
	 * @return
	 */
	UserInstitution getUserIns(String userId);
}