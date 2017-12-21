package com.wf.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.HotWordSetting;
import com.wf.bean.PageList;
import com.wf.dao.HotWordSettingDao;
import com.wf.service.HotWordSettingService;

@Service
public class HotWordSettingServiceImpl implements HotWordSettingService {
	@Autowired
	HotWordSettingDao hotWordSettingDao;

	@Override
	public Integer addWordSetting(HotWordSetting hotWordSetting) {
		return hotWordSettingDao.addWordSetting(hotWordSetting);
	}

	@Override
	public PageList getHotWordSetting(Map map) {
		PageList list = new PageList();
		list.setPageNum(Integer.valueOf(map.get("pageNum").toString()));
		list.setPageSize(Integer.valueOf(map.get("pageSize").toString()));
		list.setPageRow(hotWordSettingDao.getHotWordSetting(map));
		list.setTotalRow(hotWordSettingDao.getHotWordSettingCount(map).size());

		return list;
	}

}
