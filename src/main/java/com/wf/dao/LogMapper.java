package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Log;

public interface LogMapper {
	/**
	 * 根据条件查询后台日志
	 * 
	 * @param map参数
	 * @return 查询结果
	 */
	List<Object> getLog(@Param("username") String username,
			@Param("ip") String ip, @Param("module") String module,
			@Param("behavior") String behavior,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime,
			@Param("pageNum") Integer pageNum,
			@Param("pageSize") Integer pageSize);

	List<Object> getLogCount(@Param("username") String username,
			@Param("ip") String ip, @Param("module") String module,
			@Param("behavior") String behavior,
			@Param("startTime") String startTime,
			@Param("endTime") String endTime);

	Integer deleteLogByID(@Param("ids") Integer[] ids);

	/**
	 * 获取所有模块名称
	 * 
	 * @return
	 */
	List<String> getAllLogModel();

	List<String> getResTypeByModel(@Param("modelname") String modelname);

	/**
	 * 记录后台操作日志
	 * 
	 * @param log
	 * @return
	 */
	Integer addLog(@Param("log") Log log);
	
	Integer addLog_institution(@Param("log") Log log);

}
