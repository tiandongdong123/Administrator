package com.wf.dao;

import java.util.Map;

import com.wf.bean.ProjectDeadline;

/**
 *	资源限时Mapper 
 */
public interface ProjectDeadlineMapper {
    int deleteDeadlineByUserId(String userId);

    int insert(ProjectDeadline record);

    /** 查询资源限时 */
    Map<String,Object> selectDeadlineById(String userId);
}