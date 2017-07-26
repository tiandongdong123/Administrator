package com.wf.service;

import java.util.List;

import com.wf.bean.Remind;

public interface RemindService {
	/**
	 * 添加提醒消息
	 * @param remind
	 * @return
	 */
	boolean insert(Remind remind);
	/**
	 * 查询提醒消息
	 * @return
	 */
	List<Remind> query();
	/**
     * 修改查看状态
     * @return
     */
	boolean update();
}
