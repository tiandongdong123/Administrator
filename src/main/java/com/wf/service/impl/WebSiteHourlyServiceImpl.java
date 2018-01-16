package com.wf.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.DateTools;
import com.wf.bean.PageList;
import com.wf.bean.WebSiteHourly;
import com.wf.dao2.WebSiteHourlyMapper;
import com.wf.service.WebSiteHourlyService;


@Service
public class WebSiteHourlyServiceImpl implements WebSiteHourlyService {

	
	@Autowired
	private WebSiteHourlyMapper mapper;

	@Override
	public HashMap<String,Object> findPageViewHourly(Integer type,String dateType) {
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
		HashMap<String,Object> map =new HashMap<String,Object>(); 
		List<Object> l1 = new ArrayList<Object>();
		List<Object> l2 = new ArrayList<Object>();
		if(type != null && !"".equals(type)){
		List<Object> list = mapper.findPageViewHourly(startTime, endTime, type);
		if(list.size() >0){
		for(int a = 0;a<list.size();a++){
			WebSiteHourly website = (WebSiteHourly) list.get(a);
			l1.add(website.getNumbers());
			l2.add(website.getHour());
		}
		map.put("type", (typeToString(((WebSiteHourly)list.get(0)).getType())));
		map.put("numbers",l1 );
		map.put("date", l2);
		map.put("time", startTime+"/"+endTime);
		}
		}
		return map;
		
	}
	
	// 指标详细列表
		@Override
		public PageList basicIndexNumHourly(String dateType, Integer pagenum,
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
				int startnum = (pagenum-1)*pagesize;
				List<Object> list = mapper.basicIndexNum(startTime, endTime, startnum,pagesize);
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
	
	@Override//对比折线图
	public HashMap<String, Object> contrastPageViewHourly(String dateType,
			String contrastDate, Integer type) {
		// TODO Auto-generated method stub

		HashMap<String, Object> map = new HashMap<String,Object>();
		HashMap<String, Object> map1 = findPageViewHourly(type,dateType);
		HashMap<String, Object> map2 = findPageViewHourly(type,contrastDate);
		
		List<String> dates=new ArrayList<String>();
		
		for (int i = 0; i <24; i++) {
			if(i<10){
				dates.add("0"+i);
			}else{
				dates.add(i+"");
			}
		}
		
		List<String> date1=map1.get("date")==null?new ArrayList<String>():((List<String>)map1.get("date"));
		List<String> date2=map2.get("date")==null?new ArrayList<String>():((List<String>)map2.get("date"));
		List<String> number1=map1.get("numbers")==null?new ArrayList<String>():((List<String>)map1.get("numbers"));
		List<String> number2=map2.get("numbers")==null?new ArrayList<String>():((List<String>)map2.get("numbers"));
		
		for (int i = 0; i < dates.size(); i++) {
			if(!date1.contains(dates.get(i))){
				date1.add(i, dates.get(i));
				number1.add(i,"0");
			}
		}
		
		for (int i = 0; i < dates.size(); i++) {
			if(!date2.contains(dates.get(i))){
				date2.add(i, dates.get(i));
				number2.add(i,"0");
			}
		}
		
		for (String date : dates) {
			if(!date2.contains(date)){
				date2.add(date);
				number2.add("0");
			}
		}
		
		map.put("dateLine", map1);
		map.put("contrastLine", map2);
		return map;
	}
	
	/* type类型 转换成字符串**/
	public static String typeToString(int type){
		String str ="";
		switch (type) {
		
		case 1:
			str ="网站浏览量";
			break;
		case 2:
			str ="访问次数";
			break;
		case 3:
			str ="独立访问次数";
			break;
		case 4:
			str ="新访客数";
			break;
		case 5:
			str ="独立IP数";
			break;
		case 6:
			str ="平均访问页数";
			break;
		case 7:
			str ="访问总时长";
			break;
		case 8:
			str ="平均访问时长";
			break;
		case 9:
			str ="跳出数";
			break;
		case 10:
			str ="跳出率";
			break;
		case 11:
			str ="活跃数";
			break;
		case 12:
			str ="活跃率";
			break;
		default:
			str = "没有此数据";
			break;
		}
		return str;
	}
	
}
