package com.wf.dao;

import java.util.List;

import com.wf.bean.Remind;

public interface RemindMapper {
	/**
	 * 添加提醒消息
	 * @param remind
	 * @return
	 */
    int insert(Remind remind);
    /**
	 * 查询提醒消息
	 * @return
	 */
    List<Remind> query();
    /**
     * 修改查看状态
     * @return
     */
    int update();
}