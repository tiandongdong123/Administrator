package com.wf.dao;

import com.wf.bean.WfksUserSetting;
import com.wf.bean.WfksUserSettingKey;

public interface WfksUserSettingMapper {
    int deleteByUserId(WfksUserSettingKey key);

    int insert(WfksUserSetting record);

    int insertSelective(WfksUserSetting record);

    WfksUserSetting selectByPrimaryKey(WfksUserSettingKey key);

    int updateByPrimaryKeySelective(WfksUserSetting record);

    int updateByPrimaryKeyWithBLOBs(WfksUserSetting record);
}