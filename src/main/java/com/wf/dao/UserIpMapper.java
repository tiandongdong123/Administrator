package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.UserIp;

/**
 *	用户IP Mapper 
 */
public interface UserIpMapper {
    int insert(UserIp record);
    
    int deleteUserIp(String userId);
    
    /** 验证IP交集  */
    List<Map<String,Object>> validateIp(List<UserIp> list);

    /** 更新ip段 */
	int updateIp(UserIp userIp);
	
    /** 查询ip结果集 */
    List<Map<String,Object>> findIpByUserId(String userId);
    /** 查询结果集(无排序) */
    List<Map<String,Object>> listIpByUserId(String userId);
    /** 通过IP段来查询 */
    List<String> findUserIdByIp(UserIp userIp);
}