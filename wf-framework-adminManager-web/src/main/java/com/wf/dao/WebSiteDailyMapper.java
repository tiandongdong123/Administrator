package com.wf.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;


public interface WebSiteDailyMapper {
	
/**
*查询昨天浏览量 echarts 图
 */
	List<Object> findPageView(@Param("type")Integer type,@Param("startTime")String startTime,@Param("endTime")String endTime);
/**查询基础指标列表  按日 统计表*/
	List<Object> basicIndexNum(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
/**  查询指标列表  **/
	int getBasicIndexNum(@Param("startTime")String startTime,@Param("endTime")String endTime);

/** 查询浏览总条数 */
	List<Object> selectSumNumbers(@Param("startTime")String startTime,@Param("endTime")String endTime);
}
