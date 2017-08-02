package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.GetUuid;
import com.wf.bean.PageList;
import com.wf.bean.Setting;
import com.wf.dao.SettingMapper;
import com.wf.service.SettingService;
@Service
public class SettingServiceImpl implements SettingService {
	@Autowired
	private SettingMapper settingDao;
	@Override
	public PageList getSetting(Setting setting, Integer pagenum,
			Integer pagesize) {
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			int startnum = (pagenum-1)*pagesize;
			r = this.settingDao.getSetting(setting, startnum, pagesize);
			int num= this.settingDao.getSettingNum(setting);
			p.setPageRow(r);
			p.setPageNum(pagenum);
			p.setPageTotal(num);
			p.setPageSize(pagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	/**新增 Settring */
	@Override
	public Setting addSetting(String settingName, String settingKey,
			String settingValue, String remark) {
		Setting setting =new Setting();
		setting.setId(GetUuid.getId());
		setting.setSettingName(settingName);
		setting.setSettingKey(settingKey);
		setting.setSettingValue(settingValue);
		setting.setRemark(remark);
		int num =settingDao.addSetting(setting);
		if(num<=0){
			 setting=null;
		}
		return setting;
	}
	
}
