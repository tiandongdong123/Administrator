package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.GetUuid;
import com.wf.bean.Remind;
import com.wf.dao.RemindMapper;
import com.wf.service.RemindService;
@Service
public class RemindServiceImpl implements RemindService{
	@Autowired
	private RemindMapper remindMapper;
	/**
	 * 添加提醒消息
	 * @param remind
	 * @return
	 */
	@Override
	public boolean insert(Remind remind) {
		boolean flag = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		remind.setId(GetUuid.getId());
		remind.setIsSeen(0);
		remind.setRemindTime(format.format(new Date()));
		int num = remindMapper.insert(remind);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	/**
	 * 查询提醒消息
	 * @return
	 */
	@Override
	public List<Remind> query() {
		List<Remind> list = remindMapper.query();
		return list;
	}
	/**
     * 修改查看状态
     * @return
     */
	@Override
	public boolean update() {
		boolean flag = false;
		//查看之后修改查看状态
		int num = remindMapper.update();
		if(num > 0){
			flag = true;
		}
		return flag;
	}

}
