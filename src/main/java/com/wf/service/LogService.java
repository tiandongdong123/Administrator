package com.wf.service;

import java.util.List;

import com.wf.bean.Log;
import com.wf.bean.PageList;

public interface LogService {
	/**
	 * 根据条件查询后台日志
	 * 
	 * @param map参数
	 * @return 查询结果
	 */
	PageList getLog(String username, String ip, String module,String behavior,
			String startTime, String endTime, Integer pageNum);

	List<Object> exportLog(String username, String ip, String module,String behavior,
			String startTime, String endTime);
	
	Integer deleteLogByID(Integer[]ids);
	
	List<String> getAllLogModel();
	
	List<String> getResTypeByModel(String modelname);
	
	Integer addLog(Log log);
	
	Integer addLog_Institution(Log log);
}
