package com.wf.dao;

import com.wf.bean.GroupInfo;

public interface GroupInfoMapper {
	
	/**
	 * 新增机构信息表
	 * @param list
	 * @return
	 */
	int insertGroupInfo(GroupInfo info);
	
	/**
	 * 修改机构信息表
	 * @param list
	 * @return
	 */
	int updateGroupInfo(GroupInfo info);
	/**
	 * 查询机构信息表
	 * @param userId
	 * @return
	 */
	GroupInfo getGroupInfo(String UserId);
	
	/**
	 * 删除机构信息表
	 * @param userId
	 * @return
	 */
	int deleteGroupInfo(String UserId);
}
