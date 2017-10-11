package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.OperationLogs;

public interface OperationLogsMapper {
	
	/**
	 * 添加操作记录
	 * @param op OperationLogs类
	 * @return
	 */
	boolean addOperationLogs(OperationLogs op);
	
	/**
	 * 查询操作记录
	 * @return
	 */
	List<Object> selectOperationLogs(Map<String,Object> map);

	/**
	 * 查询操作记录总条数
	 * @return
	 */
	int selectOperationLogsNum(Map<String,Object> map);
	
	/**
	 * 根据userId统计项目数量
	 * @param userId
	 * @return
	 */
	List<Map<String,String>> getProjectByUserId(String userId);
}
