package com.wf.dao;

import com.wf.bean.Resource;

public interface ResourceMapper {
    
	/** 添加资源购买信息 */
	int addResource(Resource res);

	/** 更新资源购买信息 */
	int updateResource(Resource res);
}