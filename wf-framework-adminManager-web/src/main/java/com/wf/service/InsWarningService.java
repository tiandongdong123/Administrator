package com.wf.service;

import com.wf.bean.WarningInfo;

public interface InsWarningService {
	
	/**
	 * 机构用户信息预警发送邮件
	 */
	boolean sendMail(WarningInfo war);
}
