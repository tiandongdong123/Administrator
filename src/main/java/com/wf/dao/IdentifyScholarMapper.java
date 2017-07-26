package com.wf.dao;

import org.apache.ibatis.annotations.Param;

public interface IdentifyScholarMapper {
	
	/**
	 * 根据userid获取发文单位
	 * @param userId 用户ID
	 * @return 发文单位
	 */
	String getPublishunitByUserId(@Param("userId")String userId);
}
