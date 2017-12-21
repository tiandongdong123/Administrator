package com.wf.service.impl;

import java.util.List;
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

	@Override
	public HotWordSetting getOneHotWordSetting(Integer id) {
		return hotWordSettingDao.getOneHotWordSetting(id);
	}

	@Override
	public Integer updateWordSetting(HotWordSetting hotWordSetting) {
		return hotWordSettingDao.updateHotWordSetting(hotWordSetting);
	}

	@Override
	public Object getOneHotWordSettingShow(Map map) {
		List<Object> list=hotWordSettingDao.getHotWordSettingCount(map);
		if(list.size()==0){
			return null;
		}else{
			return hotWordSettingDao.getHotWordSettingCount(map).get(0);	
		}
		
	}

	@Override
	public HotWordSetting getHotWordSettingTask() {
		return hotWordSettingDao.getHotWordSettingTask();
	}

	@Override
	public List<Object> getHotWordTongJi(String sql) {
		return hotWordSettingDao.getHotWordTongJi(sql);
	}
}
