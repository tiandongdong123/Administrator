package com.wf.dao;

import com.wf.bean.WarningInfo;

public interface AheadUserMapper {
	
	/**
	 *	添加预警信息 
	 */
	int addWarning(WarningInfo winfo);
	
	/**
	 *	修改预警信息 
	 */
	int updateWarning(WarningInfo winfo);
	
	/**
	 *	查询阈值信息
	 */
	WarningInfo findWarning();

}
