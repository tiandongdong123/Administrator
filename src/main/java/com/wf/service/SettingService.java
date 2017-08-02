package com.wf.service;

import com.wf.bean.PageList;
import com.wf.bean.Setting;

public interface SettingService {
	
	/**分页*/
	PageList getSetting(Setting setting,Integer pagenum,Integer pagesize);
	
	Setting addSetting(String settingName,String settingKey,String settingValue,String remark);
	
	
}
