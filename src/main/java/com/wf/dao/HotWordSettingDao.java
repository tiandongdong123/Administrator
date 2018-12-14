package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.HotWordSetting;

public interface HotWordSettingDao {
	Integer addWordSetting(HotWordSetting hotWordSetting);
	List<Object> getHotWordSetting(Map map);//自动设置
	List<Object> getHotWordManualSetting(Map map);//手动设置
	List<Object> getHotWordSettingCount(Map map);//自动设置
	List<Object> getHotWordManualSettingCount(Map map);//手动设置
	HotWordSetting getOneHotWordSetting(Integer id);
	Integer updateHotWordSetting(HotWordSetting hotWordSetting);
	HotWordSetting getHotWordSettingTask();
	HotWordSetting getHotWordManualSettingTask();
	Integer updateAllSetting();
	Integer checkFirst();
	String getNextPublishTime();
	List<HotWordSetting> getHotWordSettingList();
	List<HotWordSetting> getHotWordManualSettingList();
	HotWordSetting getExecHotWordSetting(Integer status);
	HotWordSetting getOneHotWordManualSetting();
	String getNowWordSetting();
}
