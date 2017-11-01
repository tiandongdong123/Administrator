package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.StandardUnit;

public interface StandardUnitMapper {

	/**
	 * 根据userId获取标准机构
	 * 
	 * @param userId
	 * @return
	 */
	StandardUnit getStandardUnitById(@Param("userId") String userId);

	/**
	 * 根据条件查询标准机构
	 * 
	 * @param userId
	 * @param orgCode
	 * @param companySimp
	 * @return
	 */
	List<StandardUnit> findStandardUnit(@Param("userId") String userId, @Param("orgCode") String orgCode,
			@Param("companySimp") String companySimp);

	/**
	 * 插入一个机构
	 * 
	 * @param unit
	 * @return
	 */
	int insertStandardUnit(StandardUnit unit);
	
	/**
	 * 更新一个机构
	 * @param unit
	 * @return
	 */
	int updateStandardUnit(StandardUnit unit);

	/**
	 * 删除一个机构
	 * 
	 * @param userId
	 * @return
	 */
	int deleteStandardUnit(@Param("userId") String userId);
	
	/**
	 * 获取最大的org_code
	 * @return
	 */
	String findMaxOrgCode();
}
