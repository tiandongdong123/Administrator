package com.wf.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

import com.utils.DateTools;
import com.wf.bean.PageList;
import com.wf.bean.WebSiteHourly;
import com.wf.dao2.WebSiteHourlyMapper;
import com.wf.service.WebSiteDailyService;

@Service
public class WebSiteDailyServiceImpl implements WebSiteDailyService {
	@Autowired
	private WebSiteHourlyMapper mapper;
	/** 查询昨天访问量 */
	@Override
	public HashMap<String, Object> findPageView(Integer type, String dateType) {
		// TODO Auto-generated method stub
		String[] time = null;
		String startTime = null;
		String endTime = null;

		int i = 0;
		if ("qitian".equals(dateType)) {
			i = -8;
			String beforeOrAfterNDay = DateTools.beforeOrAfterNDay(i);// 七天前或者30天前日期
			startTime = beforeOrAfterNDay.substring(0, 10);
			endTime = DateTools.beforeOrAfterNDay(-1);
			endTime = endTime.substring(0, 10);
		} else if ("yigeyue".equals(dateType)) {
			i = -31;
			String beforeOrAfterNDay = DateTools.beforeOrAfterNDay(i);// 七天前或者30天前日期
			startTime = beforeOrAfterNDay.substring(0, 10);
			endTime = DateTools.beforeOrAfterNDay(-1);
			endTime = endTime.substring(0, 10);
		} else {
			List<String> l = new ArrayList<String>();
			time = dateType.split("/");
			for (String x : time) {
				l.add(x);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (sdf.parse(l.get(0)).getTime() >= sdf.parse(l.get(1))
						.getTime()) {
					startTime = l.get(1);
					endTime = l.get(0);
				} else if (sdf.parse(l.get(0)).getTime() < sdf.parse(l.get(1))
						.getTime()) {
					startTime = l.get(0);
					endTime = l.get(1);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Object> l1 = new ArrayList<Object>();
		List<Object> l2 = new ArrayList<Object>();
		List<Object> list = mapper.findPageView(type, startTime, endTime);
		if (list.size() > 0) {
			for (int a = 0; a < list.size(); a++) {
				WebSiteHourly daily = (WebSiteHourly) list.get(a);
				l1.add(daily.getNumbers());
				l2.add(daily.getDate());
			}
			map.put("type",typeToString(((WebSiteHourly) list.get(0)).getType()));
			map.put("numbers", l1);
			map.put("date", l2);
			map.put("time", startTime + "/" + endTime);
		}else{
			map.put("time", startTime + "/" + endTime);
		}
		return map;
	}

	/* type类型 转换成字符串* */
	public static String typeToString(int type) {
		String str = "";
		switch (type) {

		case 1:
			str = "网站浏览量";
			break;
		case 2:
			str = "访问次数";
			break;
		case 3:
			str = "独立访问次数";
			break;
		case 4:
			str = "新访客数";
			break;
		case 5:
			str = "独立IP数";
			break;
		case 6:
			str = "平均访问页数";
			break;
		case 7:
			str = "访问总时长";
			break;
		case 8:
			str = "平均访问时长";
			break;
		case 9:
			str = "跳出数";
			break;
		case 10:
			str = "跳出率";
			break;
		case 11:
			str = "活跃数";
			break;
		case 12:
			str = "活跃率";
			break;
		default:
			str = "没有此数据";
			break;
		}
		return str;
	}

	@Override
	// 指标详细列表
	public PageList basicIndexNum(String dateType, Integer pagenum,
			Integer pagesize) {
		String[] time = null;
		String startTime = null;
		String endTime = null;

		Integer i = null;
		if ("0".equals(dateType)) {
			i = -1;
			String beforeOrAfterNDay = DateTools.beforeOrAfterNDay(i);// 昨天日期
			startTime = beforeOrAfterNDay.substring(0, 10);
			endTime = DateTools.beforeOrAfterNDay(-1);
			endTime = endTime.substring(0, 10);
		} else if ("qitian".equals(dateType)) {
			i = -7;
			String beforeOrAfterNDay = DateTools.beforeOrAfterNDay(i);// 七天前或者30天前日期
			startTime = beforeOrAfterNDay.substring(0, 10);
			endTime = DateTools.beforeOrAfterNDay(-1);
			endTime = endTime.substring(0, 10);
		} else if ("yigeyue".equals(dateType)) {
			i = -30;
			String beforeOrAfterNDay = DateTools.beforeOrAfterNDay(i);// 七天前或者30天前日期
			startTime = beforeOrAfterNDay.substring(0, 10);
			endTime = DateTools.beforeOrAfterNDay(-1);
			endTime = endTime.substring(0, 10);
		} else {
			List<String> l = new ArrayList<String>();
			time = dateType.split("/");
			for (String x : time) {
				l.add(x);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (sdf.parse(l.get(0)).getTime() >= sdf.parse(l.get(1))
						.getTime()) {
					startTime = l.get(1);
					endTime = l.get(0);
				} else if (sdf.parse(l.get(0)).getTime() < sdf.parse(l.get(1))
						.getTime()) {
					startTime = l.get(0);
					endTime = l.get(1);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		PageList p = new PageList();
		try {
			int startnum = (pagenum - 1) * pagesize;
			List<Object> list = mapper.basicIndexNum_day(startTime, endTime, startnum,pagesize);
			int num = mapper.getBasicIndexNum(startTime, endTime);
			// TODO Auto-generated method stub
			p.setPageRow(list);//查询结果列表
			p.setPageNum(pagenum);// 当前页
			p.setTotalRow(num);// 总条数
			p.setPageSize(pagesize);// 每页显示的数量
			p.setPageTotal(num);//总页数
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	// 查询统计列表总数
	@Override
	public Object selectSumNumbers(String dateType) {
		// TODO Auto-generated method stub
		String[] time = null;
		String startTime = null;
		String endTime = null;

		Integer i = null;
		if ("0".equals(dateType)) {
			i = -1;
			String beforeOrAfterNDay = DateTools.beforeOrAfterNDay(i);// 昨天日期
			startTime = beforeOrAfterNDay.substring(0, 10);
			endTime = DateTools.beforeOrAfterNDay(-1);
			endTime = endTime.substring(0, 10);
		} else if ("qitian".equals(dateType)) {
			i = -7;
			String beforeOrAfterNDay = DateTools.beforeOrAfterNDay(i);// 七天前或者30天前日期
			startTime = beforeOrAfterNDay.substring(0, 10);
			endTime = DateTools.beforeOrAfterNDay(-1);
			endTime = endTime.substring(0, 10);
		} else if ("yigeyue".equals(dateType)) {
			i = -30;
			String beforeOrAfterNDay = DateTools.beforeOrAfterNDay(i);// 七天前或者30天前日期
			startTime = beforeOrAfterNDay.substring(0, 10);
			endTime = DateTools.beforeOrAfterNDay(-1);
			endTime = endTime.substring(0, 10);
		} else {
			List<String> l = new ArrayList<String>();
			time = dateType.split("/");
			for (String x : time) {
				l.add(x);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (sdf.parse(l.get(0)).getTime() >= sdf.parse(l.get(1))
						.getTime()) {
					startTime = l.get(1);
					endTime = l.get(0);
				} else if (sdf.parse(l.get(0)).getTime() < sdf.parse(l.get(1))
						.getTime()) {
					startTime = l.get(0);
					endTime = l.get(1);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		long time1 = 0;
		long time2 = 0;
		try {
			cal.setTime(sdf.parse(startTime));
			time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(endTime));
			time2 = cal.getTimeInMillis();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long days = 0;
		if (time2 - time1 == 0) {
			days = 1;
		} else {
			days = (time2 - time1) / (1000 * 3600 * 24);
		}
		List<Object> list = mapper.selectSumNumbers(startTime, endTime);
		List<Map<String, Object>> json = new ArrayList<Map<String, Object>>();
		for (int a = 0; a < list.size(); a++) {
			WebSiteHourly web = (WebSiteHourly) list.get(a);
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (web.getType() == 8) {
				web.setNum(web.getNum() / days);
				map.put("type", 8);
				map.put("value", web.getNum());
				json.add(map);
			} else if (web.getType() == 10) {
				web.setNum(web.getNum() / days);
				map.put("type", 10);
				map.put("value", web.getNum() + "%");
				json.add(map);
			} else {
				map.put("type", a + 1);
				map.put("value", web.getNum());
				json.add(map);
			}
		}
		return json;
	}

	@Override//对比折线图
	public HashMap<String, Object> contrastPageView(String dateType,
			String contrastDate, Integer type) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String,Object>();
		HashMap<String, Object> map1 = findPageView(type,dateType);
		HashMap<String, Object> map2 = findPageView(type,contrastDate);
		
		int date1=map1.get("date")==null?0:((List<String>)map1.get("date")).size();
		int date2=map2.get("date")==null?0:((List<String>)map2.get("date")).size();
		
		int max=date1>date2?date1:date2;
		
		List<String> dates=null;
		List<String> numbers=null;
		
		if(date1<max){
			dates=new ArrayList<String>();
			numbers=new ArrayList<String>();
			for (int i = 0; i <max-date1; i++) {
				if(null==map1.get("date")){
					dates.add("无");
					numbers.add("0");
				}else{
					((List<String>)map1.get("date")).add("无");
					((List<String>)map1.get("numbers")).add("0");
				}
			}
			
			if(null==map1.get("date")){
				map1.put("date",dates);
				map1.put("numbers",numbers);
			}
			
		}
		
		if(date2<max){
			dates=new ArrayList<String>();
			numbers=new ArrayList<String>();
			for (int i = 0; i <max-date2; i++) {
				if(null==map2.get("date")){
					dates.add("无");
					numbers.add("0");
				}else{
					((List<String>)map2.get("date")).add("无");
					((List<String>)map2.get("numbers")).add("0");
				}
			}
			
			if(null==map2.get("date")){
				map2.put("date",dates);
				map2.put("numbers",numbers);
			}
		}
		
		map.put("dateLine", map1);
		map.put("contrastLine", map2);
		return map;
	}

}
