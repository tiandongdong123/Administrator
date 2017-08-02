package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.ProjectNumber;

/**
 *	系统检测（次数） 
 */
public interface ProjectNumberMapper {
    int deleteNumberByUserId(String userId);

    int insert(ProjectNumber record);

    /** 查询资源检测*/
    List<Map<String,Object>> selectNumberById(String userId);
}