package com.wf.dao;

import java.util.List;

import com.wf.bean.Project;

public interface ProjectMapper {

    /** 查询所有项目 */
	List<Project> findProject();
}