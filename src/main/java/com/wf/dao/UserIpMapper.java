package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.UserIp;

/**
 *	用户IP Mapper 
 */
public interface UserIpMapper {
    int insert(UserIp record);
    
    int deleteUserIp(String userId);
    
    /** 验证IP交集  */
    List<UserIp> validateIp(@Param("userId")String userId, @Param("beginIp")long beginIp, @Param("endIp")long endIp);

    /** 更新ip段 */
	int updateIp(UserIp userIp);
	
    /** 查询ip结果集 */
    List<Map<String,Object>> findIpByUserId(String userId);
    
    /** 通过IP查询userId */
    List<UserIp> findUserIdByIp(long l);
}