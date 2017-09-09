package com.wf.dao;


import java.util.List;
import java.util.Map;

import com.wf.bean.ProjectResources;

/**
 *	项目资源 Mapper 
 */
public interface ProjectResourcesMapper {
    int deleteResources(ProjectResources pr);

    int insert(ProjectResources pr);
    
    Map<String,Object> selectProjectResourcesById(Map<String, Object> m);

    int update(ProjectResources pr);

	List<Map<String, String>> getReourceMappingByUserId(String userId);
}