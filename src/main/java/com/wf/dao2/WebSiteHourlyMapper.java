package com.wf.dao2;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface WebSiteHourlyMapper {

	/**
	 * 按小时查询  e-chart图
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	List<Object> findPageViewHourly(@Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("type") Integer type);

	/**
	 *按天查询  e-chart图
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Object> findPageView(@Param("type")Integer type,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	/**
	 * 查询基础指标列表 按小时 统计表 
	 * @param startTime
	 * @param endTime
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	List<Object> basicIndexNum(@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pagenum") Integer pagenum,
			@Param("pagesize") Integer pagesize);
	
	/**
	 * 查询基础指标列表 按天 统计表 
	 * @param startTime
	 * @param endTime
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	List<Object> basicIndexNum_day(@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pagenum") Integer pagenum,
			@Param("pagesize") Integer pagesize);

	/**
	 *  查询指标列表 按小时统计表
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int getBasicIndexNum(@Param("startTime") String startTime,
			@Param("endTime") String endTime);
	
	/**
	 * 查询浏览总条数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<Object> selectSumNumbers(@Param("startTime")String startTime,@Param("endTime")String endTime);
	
}
