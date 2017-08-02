package com.wf.service;

import java.util.Map;

import com.wf.bean.OperationLogs;
import com.wf.bean.PageList;

public interface OpreationLogsService {

	/**
	 * 添加操作记录
	 * @param op
	 * @return
	 */
	boolean addOperationLogs(OperationLogs op);

	/**
	 * 分页查询操作记录
	 * @param map
	 * @return
	 */
	PageList selectOperationLogs(Map<String, Object> map);

}
