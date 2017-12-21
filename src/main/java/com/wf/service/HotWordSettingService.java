package com.wf.service;

import java.util.Map;

import com.wf.bean.HotWordSetting;
import com.wf.bean.PageList;

public interface HotWordSettingService {
	Integer addWordSetting(HotWordSetting hotWordSetting);
	PageList getHotWordSetting(Map map);
}
