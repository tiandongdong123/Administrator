package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.HotWordSetting;
import com.wf.bean.PageList;

public interface HotWordSettingService {
	Integer addWordSetting(HotWordSetting hotWordSetting);
	Integer updateWordSetting(HotWordSetting hotWordSetting);
	PageList getHotWordSetting(Map map);
	HotWordSetting getOneHotWordSetting(Integer id);
	Object getOneHotWordSettingShow(Map map);
	HotWordSetting getHotWordSettingTask();
	List<Map<String,Object>> getHotWordTongJi(String sql);
	Integer updateAllSetting();
	Integer checkFirst();
	String getNextPublishTime();
	Integer updateAllSettingTime();
	HotWordSetting getExecHotWordSetting(Integer status);
}
