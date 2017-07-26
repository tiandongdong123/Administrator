package com.wf.dao;

import com.wf.bean.ResourceDetailed;


public interface ResourceDetailedMapper {
	
    /** 添加资源详细信息 */
	int addResourceDetailed(ResourceDetailed rd);

	/** 更新资源详细信息 */
	int updateResourceDetailed(ResourceDetailed rd);
	
	/** 删除资源详细信息 */
	int deleteResourceDetailed(String accountId);
	
}