package com.wf.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceStatisticsHour;
import com.wf.bean.ResourceTableBean;
import com.wf.bean.ResourceType;
import com.wf.dao.PersonMapper;
import com.wf.dao.ResourceStatisticsHourMapper;
import com.wf.dao.ResourceStatisticsMapper;
import com.wf.dao.ResourceTypeMapper;
import com.wf.service.ResourceTypeStatisticsService;

@Service
public class ResourceTypeStatisticsServiceImpl implements
		ResourceTypeStatisticsService {

	@Autowired
	private ResourceTypeMapper type;
	//@Autowired
	//private ResourceStatisticsMapper restatistics;
	@Autowired
	private ResourceStatisticsHourMapper hour;
	@Autowired
	private PersonMapper personMapper;
	@Autowired
	private ResourceTypeMapper resourceTypeMapper;
	@Override
	public List<ResourceType> getResourceType() {
		List<ResourceType> listr = new ArrayList<ResourceType>();
		try {
			listr = this.type.getRtypeName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listr;
	}

	@Override
	public Map<String, Object> getAllLine(Integer table,String starttime,String endtime,ResourceStatistics res,
										  Integer[] urls,Integer singmore,String[] title,String[] database_name) {
		Map<String,Object> map=new HashMap();
		List<ResourceStatisticsHour> list=new ArrayList<ResourceStatisticsHour>();
		Map<String, List<String>> content = new HashMap<String, List<String>>();
		List<String>timeList=new ArrayList();
		List<String>browseList=new ArrayList();
		List<String>downloadList=new ArrayList();
		List<String>searchList=new ArrayList();
		List<String>shareList=new ArrayList();
		List<String>collectionList=new ArrayList();
		List<String>exportList=new ArrayList();
		List<String>noteList=new ArrayList();
		List<String>jumpList=new ArrayList();
		List<String>subscriptionList=new ArrayList();
		List<String> resources = new ArrayList<>();

		if(table==0){
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				list = this.hour.getChart(starttime,endtime,res,urls,singmore,database_name);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				list = this.hour.getChartById(starttime,endtime,res,urls,singmore,database_name);
			} else {
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				list=this.hour.getChartByIds(starttime, endtime,res,users,urls,singmore,database_name);
			}
		}else {
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				list = this.hour.getChartMore(starttime,endtime,res,urls);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				list = this.hour.getChartMoreById(starttime,endtime,res,urls);
			} else {
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				list=this.hour.getChartMoreByIds(starttime, endtime,res,users,urls);
			}
		}
		if(singmore==0){
			if(res.getDate()!=null&&!"".equals(res.getDate())){
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				if(database_name.length>0){
					resources = resourceTypeMapper.getResourceByCode(database_name);
				}else {
					for(ResourceStatisticsHour item : list){
						resources.add(item.getSourceTypeName());
					}
				}
				for(int i =0;i<resources.size();i++){
					List arrayList = new ArrayList<>();
					if(urls[i]==1){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum1() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(database_name[i],arrayList);
					}
					if(urls[i]==2){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum2() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[i]==3){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum3() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[i]==4){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum4() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[i]==5){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum5() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[i]==6){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum6() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[i]==7){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum7() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[i]==8){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum8() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[i]==9){
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum9() );
									break;
								}
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
				}
			}else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date startTime = format.parse(starttime);
					Date endTime = format.parse(endtime);
					List<Date> days = this.getDate(startTime, endTime);
					for(Date d : days){
						timeList.add(format.format(d));
					}
				}catch (ParseException e) {
					e.printStackTrace();
				}
				if(database_name.length>0){
					resources = resourceTypeMapper.getResourceByCode(database_name);
				}else {
					for(ResourceStatisticsHour item : list){
						resources.add(item.getSourceTypeName());
					}
				}
				for(int i =0;i<resources.size();i++){
					List arrayList = new ArrayList<>();
					if(urls[0]==1){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum1() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==2){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum2() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==3){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum3() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==4){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum4() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==5){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum5() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==6){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum6() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==7){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum7() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==8){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum8() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==9){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
									searchList.add(item.getSum9() );
									break;
								}
							}
							if(searchList.size()==j){
								searchList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
				}
			}
			map.put("title",resources.toArray());
		}else {
			if(res.getDate()!=null&&!"".equals(res.getDate())){
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				for(int i = 0;i<24;i++){
					for (ResourceStatisticsHour item : list) {
						if(Integer.parseInt(item.getHour())==i+1){
							searchList.add(item.getSum1() );
							browseList.add(item.getSum2());
							downloadList.add(item.getSum3());
							shareList.add(item.getSum4());
							collectionList.add(item.getSum5());
							exportList.add(item.getSum6());
							noteList.add(item.getSum7());
							jumpList.add(item.getSum8());
							subscriptionList.add(item.getSum9());
							break;
						}
					}
					if(searchList.size()==i){
						searchList.add("0");
						browseList.add("0");
						downloadList.add("0");
						shareList.add("0");
						collectionList.add("0");
						exportList.add("0");
						noteList.add("0");
						jumpList.add("0");
						subscriptionList.add("0");
					}
				}
			}else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date startTime = format.parse(starttime);
					Date endTime = format.parse(endtime);
					List<Date> days = this.getDate(startTime, endTime);
					for(Date d : days){
						timeList.add(format.format(d));
					}
				}catch (ParseException e) {
					e.printStackTrace();
				}

				for(int i = 0;i<timeList.size();i++){
					for (ResourceStatisticsHour item : list) {
						if(timeList.get(i).equals(item.getDate())){
							searchList.add(item.getSum1() );
							browseList.add(item.getSum2());
							downloadList.add(item.getSum3());
							shareList.add(item.getSum4());
							collectionList.add(item.getSum5());
							exportList.add(item.getSum6());
							noteList.add(item.getSum7());
							jumpList.add(item.getSum8());
							subscriptionList.add(item.getSum9());
							break;
						}
					}
					if(searchList.size()==i){
						searchList.add("0");
						browseList.add("0");
						downloadList.add("0");
						shareList.add("0");
						collectionList.add("0");
						exportList.add("0");
						noteList.add("0");
						jumpList.add("0");
						subscriptionList.add("0");
					}
				}
			}
			content.put("浏览数",browseList);
			content.put("下载数",downloadList);
			content.put("检索数",searchList);
			content.put("分享数",shareList);
			content.put("收藏数",collectionList);
			content.put("导出数",exportList);
			content.put("笔记数",noteList);
			content.put("跳转数",jumpList);
			content.put("订阅数",subscriptionList);
		}
		map.put("timeArr", timeList.toArray());
		map.put("content", content);
		return map;
	}
	@Override
	public PageList gettable(Integer table, String starttime, String endtime,
			ResourceStatistics res, Integer pageNum, Integer pageSize) {
		PageList pageList=new PageList();
		List<Object> PageList=new ArrayList<Object>();
		List<Object> list=new ArrayList<Object>();
		int startNum = (pageNum-1)*pageSize;
		if(table==0){
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				PageList = this.hour.getLine(starttime,endtime,res,startNum,pageSize);
				list=this.hour.getLineAll(starttime,endtime,res);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				PageList = this.hour.getLineById(starttime,endtime,res,startNum,pageSize);
				list=this.hour.getLineAllById(starttime,endtime,res);
			} else {
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				PageList=this.hour.getLineByIds(starttime, endtime,res,users,startNum,pageSize);
				list=this.hour.getLineAllByIds(starttime, endtime,res,users);
			}
		}else {
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				PageList = this.hour.getLineMore(starttime,endtime,res,startNum,pageSize);
				list=this.hour.getLineMoreAll(starttime,endtime, res);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				PageList = this.hour.getLineMoreById(starttime,endtime,res,startNum,pageSize);
				list=this.hour.getLineMoreAllById(starttime,endtime,res);
			} else {
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				PageList=this.hour.getLineMoreByIds(starttime, endtime,res,users,startNum,pageSize);
				list=this.hour.getLineMoreAllByIds(starttime, endtime,res,users);
			}
		}
		pageList.setTotalRow(list.size());
		pageList.setPageRow(PageList);
		pageList.setPageNum(pageNum);
		pageList.setPageSize(pageSize);
		return pageList;
	}
	public List<Date> getDate(Date sd, Date ed) {
		List<Date> lDate = new ArrayList<Date>();
		lDate.add(sd);// 把开始时间加入集合
		Calendar cal = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(sd);
		boolean bContinue = true;
		while (bContinue) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
			// 测试此日期是否在指定日期之后
			if (ed.after(cal.getTime())) {
				lDate.add(cal.getTime());
			} else {
				break;
			}
		}
		lDate.add(ed);
		return lDate;
	}

	@Override
	public List<Object> exportresourceType(Integer table, String starttime,
			String endtime, ResourceStatistics res) {
		List<Object> list = new ArrayList<Object>();
		if(table==0){
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				list = this.hour.getLineAll(starttime,endtime,res);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				list = this.hour.getLineAllById(starttime,endtime,res);
			} else {
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				list=this.hour.getLineAllByIds(starttime, endtime,res,users);
			}
		}else {
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				list = this.hour.getLineMoreAll(starttime,endtime,res);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				list = this.hour.getLineAllById(starttime,endtime,res);
			} else {
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				list=this.hour.getLineMoreAllByIds(starttime, endtime,res,users);
			}
		}
		return list;
	}

}
