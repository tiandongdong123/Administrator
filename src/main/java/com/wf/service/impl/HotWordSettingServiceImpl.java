package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
	public List<Map<String,Object>> getHotWordTongJi(String sql) {
		return hotWordSettingDao.getHotWordTongJi(sql);
	}

	@Override
	public Integer updateAllSetting() {
		return hotWordSettingDao.updateAllSetting();
	}

	@Override
	public Integer checkFirst() {
		return hotWordSettingDao.checkFirst();
	}

	@Override
	public String getNextPublishTime() {
		return hotWordSettingDao.getNextPublishTime();
	}

	@Override
	public Integer updateAllSettingTime() {
		int num=0;
		try{
			Calendar cal = Calendar.getInstance();
			String ntime=this.getNextPublishTime();
			String end_time = ntime.substring(0, 10);
			List<HotWordSetting> list=hotWordSettingDao.getHotWordSettingList();
			for(HotWordSetting set:list){
				if ("2".equals(set.getStatus())) {
					set.setNext_publish_time(end_time+" "+set.getPublish_date());
				}
				cal.setTime(df.parse(end_time+ " " + set.getGet_time()));
				cal.add(Calendar.DATE, -set.getTime_slot());
				String start_time=df.format(cal.getTime());
				StringBuffer sb=new StringBuffer("");
				sb.append(start_time.replaceFirst("-", "年").replaceFirst("-", "月").replaceFirst(" ", "日"));
				sb.append("───");
				sb.append(end_time.replaceFirst("-", "年").replaceFirst("-", "月").replaceFirst(" ", "日"));
				sb.append("日  " + set.getGet_time());
				set.setNext_publish_time_space(sb.toString());
				num+=hotWordSettingDao.updateHotWordSetting(set);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return num;
	}
}
