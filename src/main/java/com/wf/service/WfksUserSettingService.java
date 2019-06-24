package com.wf.service;

import com.wf.bean.WfksUserSetting;
import com.wf.bean.WfksUserSettingKey;

public interface WfksUserSettingService {

	boolean delectWfksUserSetting(WfksUserSettingKey wfks);
	
	int addWfksUserSetting(WfksUserSetting wfks);
	
}
