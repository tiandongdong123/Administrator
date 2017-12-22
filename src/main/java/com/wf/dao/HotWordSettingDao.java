package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.HotWordSetting;

public interface HotWordSettingDao {
	Integer addWordSetting(HotWordSetting hotWordSetting);
	List<Object> getHotWordSetting(Map map);
	List<Object> getHotWordSettingCount(Map map);
	HotWordSetting getOneHotWordSetting(Integer id);
	Integer updateHotWordSetting(HotWordSetting hotWordSetting);
	HotWordSetting getHotWordSettingTask();
	List<Object> getHotWordTongJi(String sql);
	Integer updateAllSetting();
}
