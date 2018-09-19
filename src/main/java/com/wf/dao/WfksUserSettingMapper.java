package com.wf.dao;

import com.wf.bean.WfksUserSetting;
import com.wf.bean.WfksUserSettingKey;

public interface WfksUserSettingMapper {
	
    int deleteByUserId(WfksUserSettingKey key);
    int insert(WfksUserSetting record);
    WfksUserSetting[] selectByUserId(WfksUserSettingKey key);
    WfksUserSetting selectByPrimaryKey(WfksUserSettingKey key);
}