package com.wf.dao;

import java.util.Map;

import com.wf.bean.ProjectBalance;

/**
 *	资源余额 Mapper 
 */
public interface ProjectBalanceMapper {
	
    int deleteBalanceByUserId(String userId);

    int insert(ProjectBalance record);

    /** 查询资源余额 */
    Map<String,Object> selectBalanceById(String userId);
}