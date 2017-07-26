package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface LogMapper {
	/**
	 * 根据条件查询后台日志
	 * 
	 * @param map参数
	 * @return 查询结果
	 */
	List<Object> getLog(@Param("username") String username,
			@Param("ip") String ip, @Param("behavior") String behavior,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

	List<Object> getLogCount(@Param("username") String username, @Param("ip") String ip,
			@Param("behavior") String behavior,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);
	
	Integer deleteLogByID(@Param("ids")Integer[]ids);
}
