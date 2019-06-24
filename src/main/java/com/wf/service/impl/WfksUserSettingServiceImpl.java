package com.wf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.WfksUserSetting;
import com.wf.bean.WfksUserSettingKey;
import com.wf.dao.WfksUserSettingMapper;
import com.wf.service.WfksUserSettingService;

@Service
public class WfksUserSettingServiceImpl implements WfksUserSettingService {
	
	@Autowired
	private WfksUserSettingMapper wfksUserSettingMapper;

	@Override
	public boolean delectWfksUserSetting(WfksUserSettingKey wfks) {
		
		int delect=wfksUserSettingMapper.deleteByUserId(wfks);
		
		return delect>0?true:false;
	}

	@Override
	public int addWfksUserSetting(WfksUserSetting wfks) {
		
		return wfksUserSettingMapper.insert(wfks);
	}

	
	
}
