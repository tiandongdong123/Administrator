package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.HotWordSetting;
import com.wf.bean.PageList;

public interface HotWordSettingService {
	Integer addWordSetting(HotWordSetting hotWordSetting);
	Integer updateWordSetting(HotWordSetting hotWordSetting);
	PageList getHotWordSetting(Map map);
	PageList getHotWordManualSetting(Map map);
	HotWordSetting getOneHotWordSetting(Integer id);
	Object getOneHotWordSettingShow(Map map);
	HotWordSetting getHotWordSettingTask();
	HotWordSetting getHotWordManualSettingTask();//获取手动的热搜词设置
	Integer updateAllSetting();
	Integer checkFirst();
	String getNextPublishTime();
	Integer updateAllSettingTime();
	HotWordSetting getExecHotWordSetting(Integer status);
	HotWordSetting getOneHotWordManualSetting();//获取状态为正在执行的手动设置
}
