package com.wf.dao;

import java.util.List;

import com.wf.bean.AuthoritySetting;

public interface AuthoritySettingMapper {

	/**
	 * 获取服务权限列表
	 */
	List<AuthoritySetting> getAuthoritySettingList();
	
}
