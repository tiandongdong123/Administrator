package com.wf.service.impl;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.DateTools;
import com.wf.dao.WebSiteAttributeMapper;
import com.wf.service.WebSiteAttributeService;



@Service
public class WebSiteAttributeServiceImpl implements WebSiteAttributeService {
	@Autowired
	private WebSiteAttributeMapper dao;

	@Override
	public HashMap<String, Object> findWebsiteAttribute(String dateType) {
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

		HashMap<String, Object> map = new HashMap<String,Object>();
		List<Object> ageList = dao.findWebsiteAttributeAge(startTime, endTime);//年龄
		List<Object> eduList = dao.findWebsiteAttributeEducation(startTime, endTime);//教育程度
		List<Object> genderList = dao.findWebsiteAttributeGender(startTime, endTime);//男女
		List<Object> titleList = dao.findWebsiteAttributeJobTitle(startTime, endTime);//职称
		List<Object> interestList = dao.findWebsiteAttributeInterest(startTime, endTime);//感兴趣
		map.put("age", ageList);
		map.put("education", eduList);
		map.put("gender", genderList);
		map.put("jobTitle",titleList);
		map.put("interest", interestList);
		return map;
	}

}
