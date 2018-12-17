package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
		List<Object> count=hotWordSettingDao.getHotWordSettingCount(map);
		PageList list = new PageList();
		list.setPageNum(Integer.valueOf(map.get("pageNum").toString()));
		list.setPageSize(Integer.valueOf(map.get("pageSize").toString()));
		list.setPageRow(hotWordSettingDao.getHotWordSetting(map));
		list.setTotalRow(count.size());
		list.setPageTotal(count.size()%list.getPageSize()==0?count.size()/list.getPageSize():count.size()/list.getPageSize()+1);
		return list;
	}
	
	@Override
	public PageList getHotWordManualSetting(Map map) {
		List<Object> count=hotWordSettingDao.getHotWordManualSettingCount(map);
		PageList list = new PageList();
		list.setPageNum(Integer.valueOf(map.get("pageNum").toString()));
		list.setPageSize(Integer.valueOf(map.get("pageSize").toString()));
		list.setPageRow(hotWordSettingDao.getHotWordManualSetting(map));
		list.setTotalRow(count.size());
		list.setPageTotal(count.size()%list.getPageSize()==0?count.size()/list.getPageSize():count.size()/list.getPageSize()+1);
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
	public Object getOneHotWordSettingNowShow(Map map) {
		List<Object> list=hotWordSettingDao.getHotWordSettingNowCount(map);
		if(list.size()==0){
			return null;
		}else{
			return hotWordSettingDao.getHotWordSettingNowCount(map).get(0);	
		}
	}

	@Override
	public HotWordSetting getHotWordSettingTask() {
		return hotWordSettingDao.getHotWordSettingTask();
	}

	@Override
	public HotWordSetting getHotWordManualSettingTask() {
		return hotWordSettingDao.getHotWordManualSettingTask();
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
			String isfirst="";
			for(HotWordSetting set:list){
				if(set.getIs_first()!=null&&"1".equals(set.getIs_first())){
					isfirst="1";
					break;
				}
			}
			for(HotWordSetting set:list){
				if("1".equals(isfirst)){
					if(set.getIs_first()==null||"0".equals(set.getIs_first())){
						set.setFirst_publish_time(null);
					}
				}
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

	@Override
	public HotWordSetting getExecHotWordSetting(Integer status) {
		return hotWordSettingDao.getExecHotWordSetting(status);
	}

	@Override
	public HotWordSetting getOneHotWordManualSetting() {
		return hotWordSettingDao.getOneHotWordManualSetting();
	}

	/*
	 * 更新所有手动设置的本次抓取数据时间段和下次抓取时间段
	 * 
	 */
	@Override
	public Integer updateAllManualNowGetTimeApace(HotWordSetting hotWordSetting) {
		int num=0;
		try {
			//获取所有手动设置
			List<HotWordSetting> list=hotWordSettingDao.getHotWordManualSettingList();
			//遍历更新设置手动设置属性
			for (HotWordSetting set : list) {
				set.setNow_get_time_space(hotWordSetting.getNow_get_time_space());
				set.setIs_first(hotWordSetting.getIs_first());
				num+=hotWordSettingDao.updateHotWordSetting(set);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	@Override
	public List<HotWordSetting> getHotWordManualSettingList() {
	
		return hotWordSettingDao.getHotWordManualSettingList();
	}

	@Override
	public String getNowWordSetting() {
		return hotWordSettingDao.getNowWordSetting();
	}


}
