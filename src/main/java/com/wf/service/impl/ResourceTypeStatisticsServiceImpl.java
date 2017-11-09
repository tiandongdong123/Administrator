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

		if(table==0){
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				list = this.hour.getChart(starttime,endtime,res,singmore,database_name);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				list = this.hour.getChartById(starttime,endtime,res,singmore,database_name);
			} else {
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				list=this.hour.getChartByIds(starttime, endtime,res,users,singmore,database_name);
			}
		}else {
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				list = this.hour.getChartMore(starttime,endtime,res,title);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				list = this.hour.getChartMoreById(starttime,endtime,res,title);
			} else {
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				list=this.hour.getChartMoreByIds(starttime, endtime,res,users,title);
			}
		}
		if(singmore==0){
			if(res.getDate()!=null&&!"".equals(res.getDate())){
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				if(database_name.length>0){
					for(int i =0;i<database_name.length;i++){
						List arrayList = new ArrayList<>();
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
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
							if(searchList.size()==j){
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
						arrayList.add(browseList);
						arrayList.add(downloadList);
						arrayList.add(searchList);
						arrayList.add(shareList);
						arrayList.add(collectionList);
						arrayList.add(exportList);
						arrayList.add(noteList);
						arrayList.add(jumpList);
						arrayList.add(subscriptionList);
						content.put(database_name[i],arrayList);
					}
				}else {
					List<String> resources = resourceTypeMapper.getAllName();
					for(int i =0;i<resources.size();i++){
						List arrayList = new ArrayList<>();
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
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
							if(searchList.size()==j){
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
						arrayList.add(browseList);
						arrayList.add(downloadList);
						arrayList.add(searchList);
						arrayList.add(shareList);
						arrayList.add(collectionList);
						arrayList.add(exportList);
						arrayList.add(noteList);
						arrayList.add(jumpList);
						arrayList.add(subscriptionList);
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
					for(int i =0;i<database_name.length;i++){
						List arrayList = new ArrayList<>();
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(database_name[i].equals(item.getSourceTypeName())&&timeList.get(i).equals(item.getDate())){
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
							if(searchList.size()==j){
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
						arrayList.add(browseList);
						arrayList.add(downloadList);
						arrayList.add(searchList);
						arrayList.add(shareList);
						arrayList.add(collectionList);
						arrayList.add(exportList);
						arrayList.add(noteList);
						arrayList.add(jumpList);
						arrayList.add(subscriptionList);
						content.put(database_name[i],arrayList);
					}
				}else {
					List<String> resources = resourceTypeMapper.getAllName();
					for(int i =0;i<resources.size();i++){
						List arrayList = new ArrayList<>();
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
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
							if(searchList.size()==j){
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
						arrayList.add(browseList);
						arrayList.add(downloadList);
						arrayList.add(searchList);
						arrayList.add(shareList);
						arrayList.add(collectionList);
						arrayList.add(exportList);
						arrayList.add(noteList);
						arrayList.add(jumpList);
						arrayList.add(subscriptionList);
						content.put(resources.get(i),arrayList);
					}
				}
			}
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

	@Override
	public Map<String, Object> getAllLineByCheckMore(String starttime,
			String endtime, ResourceStatistics res, String[] sourceTypeName,
			Integer[] urls, Integer singmore) {

		Map<String, Object> map = new HashMap<String, Object>();

		if (starttime.equals(endtime) && starttime != null
				&& !"".equals(starttime) && endtime != null
				&& !"".equals(endtime)) {
			if (singmore == 0) {
				map = this.getHourLineMoreByCheckMore(starttime, endtime, res,
						sourceTypeName, urls);
			} else {
				map = this.getHourLineByCheckMore(starttime, endtime, res,
						sourceTypeName);
			}
		} else {
			if (singmore == 0) {
				map = this.getLineMoreByCheckMore(starttime, endtime, res,
						sourceTypeName, urls);
			} else {
				map = this.getLineByCheckMore(starttime, endtime, res,
						sourceTypeName);
			}
		}
		return map;

	}

	public Map<String, Object> getHourLineMoreByCheckMore(String starttime,
			String endtime, ResourceStatistics res, String[] sourceTypeName,
			Integer[] urls) {
		List<String> lidate = new ArrayList<String>();
		List<String> liname = new ArrayList<String>();
		List<String> lidates = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();

		List<ResourceStatisticsHour> li = new ArrayList<ResourceStatisticsHour>();

		if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
			li = this.hour.getLineMoreByCheckMore(starttime, endtime, res, sourceTypeName, urls, starttime);
		} else if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isNotBlank(res.getUserId())) {
		String userType = personMapper.getUserTypeByUserId(res.getUserId());
			if (userType.equals("0")) {
				// userType等于0为个人用户
				li = this.hour.getLineMoreByCheckMore(starttime, endtime, res, sourceTypeName, urls, starttime);
			}else{
				 li = this.hour.getLineMoreByCheckMore_IsInstitution(starttime, endtime, res, sourceTypeName, urls, starttime);
			}
		} else {
			 li = this.hour.getLineMoreByCheckMore_IsInstitution(starttime, endtime, res, sourceTypeName, urls, starttime);
		}


		for (ResourceStatisticsHour da : li) {
			lidate.add(da.getHour());
		}

		for (ResourceStatisticsHour da : li) {
			if (da.getOperate_type() == 1) {
				da.setCHURLTYPE("浏览数");
			} else if (da.getOperate_type() == 2) {
				da.setCHURLTYPE("下载数");
			} else if (da.getOperate_type() == 3) {
				da.setCHURLTYPE("检索数");
			} else if (da.getOperate_type() == 4) {
				da.setCHURLTYPE("分享数");
			} else if (da.getOperate_type() == 5) {
				da.setCHURLTYPE("收藏数");
			} else if (da.getOperate_type() == 6) {
				da.setCHURLTYPE("导出数");
			} else if (da.getOperate_type() == 7) {
				da.setCHURLTYPE("笔记数");
			} else if (da.getOperate_type() == 8) {
				da.setCHURLTYPE("订阅数");
			}
		}

		for (ResourceStatisticsHour da : li) {
			liname.add(da.getCHURLTYPE());
		}

		for (String name : liname) {
			if (!linames.contains(name)) {
				linames.add(name);
			}
		}

		for (String date : lidate) {
			if (!lidates.contains(date)) {
				lidates.add(date);
			}
		}

		List<String> alldate = new ArrayList<String>();
		for (int i = 1; i <= 24; i++) {
			alldate.add(i + "");
		}

		List<ResourceStatisticsHour> obj = new ArrayList<ResourceStatisticsHour>();
		boolean rt = true;
		for (String name : linames) {
			for (String d : alldate) {
				for (ResourceStatisticsHour l : li) {
					if (l.getCHURLTYPE().equals(name) && l.getDate().equals(d)) {
						rt = false;
					}
				}
				if (rt) {
					ResourceStatisticsHour rs = new ResourceStatisticsHour();
					rs.setHour(d);
					rs.setCHURLTYPE(name);
					rs.setNumbers("0");
					obj.add(rs);
				}
				rt = true;
			}
		}

		li.addAll(obj);

		Collections.sort(li, new Comparator<ResourceStatisticsHour>() {
			@Override
			public int compare(ResourceStatisticsHour arg0,
					ResourceStatisticsHour arg1) {
				String hits0 = arg0.getHour();
				String hits1 = arg1.getHour();
				if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
					return 1;
				} else if (hits1.equals(hits0)) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		Map<String, List<String>> m = new HashMap<String, List<String>>();
		for (ResourceStatisticsHour l : li) {
			String typename = l.getCHURLTYPE();
			if (m.get(typename) != null) {
				m.get(typename).add(l.getNumbers());
			} else {
				List<String> num = new ArrayList<String>();
				num.add(l.getNumbers());
				m.put(typename, num);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", m);
		map.put("title", linames);
		map.put("date", alldate);
		return map;
	}

	public Map<String, Object> getHourLineByCheckMore(String starttime,
			String endtime, ResourceStatistics res, String[] sourceTypeName) {
		List<String> lidate = new ArrayList<String>();
		List<String> liname = new ArrayList<String>();
		List<String> lidates = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();


		List<ResourceStatisticsHour> li = new ArrayList<ResourceStatisticsHour>();

		if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
			li = this.hour.getLineByCheckMore(starttime, endtime, res, sourceTypeName, starttime);
		} else if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isNotBlank(res.getUserId())) {
		String userType = personMapper.getUserTypeByUserId(res.getUserId());
			if (userType.equals("0")) {
				// userType等于0为个人用户
				li = this.hour.getLineByCheckMore(starttime, endtime, res, sourceTypeName, starttime);
			}else{
				 li = this.hour.getLineByCheckMore_IsInstitution(starttime, endtime, res, sourceTypeName, starttime);
			}
		} else {
			li = this.hour.getLineByCheckMore_IsInstitution(starttime, endtime, res, sourceTypeName, starttime);
		}

		for (ResourceStatisticsHour da : li) {
			lidate.add(da.getHour());
		}

		for (ResourceStatisticsHour da : li) {
			liname.add(da.getSourceTypeName());
		}

		for (String name : liname) {
			if (!linames.contains(name)) {
				linames.add(name);
			}
		}

		for (String date : lidate) {
			if (!lidates.contains(date)) {
				lidates.add(date);
			}
		}

		List<String> alldate = new ArrayList<String>();
		for (int i = 1; i <= 24; i++) {
			alldate.add(i + "");
		}

		List<ResourceStatisticsHour> obj = new ArrayList<ResourceStatisticsHour>();
		boolean rt = true;
		for (String name : linames) {
			for (String d : alldate) {
				for (ResourceStatisticsHour l : li) {
					if (l.getSourceTypeName().equals(name)
							&& l.getHour().equals(d)) {
						rt = false;
					}
				}
				if (rt) {
					ResourceStatisticsHour rs = new ResourceStatisticsHour();
					rs.setHour(d);
					rs.setSourceTypeName(name);
					rs.setNumbers("0");
					obj.add(rs);
				}
				rt = true;
			}
		}

		li.addAll(obj);

		Collections.sort(li, new Comparator<ResourceStatisticsHour>() {
			@Override
			public int compare(ResourceStatisticsHour arg0,
					ResourceStatisticsHour arg1) {
				String hits0 = arg0.getHour();
				String hits1 = arg1.getHour();
				if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
					return 1;
				} else if (hits1.equals(hits0)) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		Map<String, List<String>> m = new HashMap<String, List<String>>();
		for (ResourceStatisticsHour l : li) {
			String typename = l.getSourceTypeName();
			if (m.get(typename) != null) {
				m.get(typename).add(l.getNumbers());
			} else {
				List<String> num = new ArrayList<String>();
				num.add(l.getNumbers());
				m.put(typename, num);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", m);
		map.put("title", linames);
		map.put("date", alldate);
		return map;
	}

	public Map<String, Object> getLineMoreByCheckMore(String starttime,
			String endtime, ResourceStatistics res, String[] sourceTypeName,
			Integer[] urls) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> lidate = new ArrayList<Date>();
		List<String> liname = new ArrayList<String>();
		List<Date> lidates = new ArrayList<Date>();
		List<String> linames = new ArrayList<String>();
		List<ResourceStatisticsHour> li =new ArrayList<ResourceStatisticsHour>();

		if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
			li = this.hour.getLineMoreByCheckMore_day(starttime, endtime, res, sourceTypeName, urls);
		} else if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isNotBlank(res.getUserId())) {
		String userType = personMapper.getUserTypeByUserId(res.getUserId());
			if (userType.equals("0")) {
				// userType等于0为个人用户
				li = this.hour.getLineMoreByCheckMore_day(starttime, endtime, res, sourceTypeName, urls);
			}else{
				li=this.hour.getLineMoreByCheckMore_IsInstitution_day(starttime, endtime, res, sourceTypeName, urls);
			}
		} else {
			li=this.hour.getLineMoreByCheckMore_IsInstitution_day(starttime, endtime, res, sourceTypeName, urls);
		}

		for (ResourceStatisticsHour da : li) {
			lidate.add(da.getDate());
		}

		for (ResourceStatisticsHour da : li) {
			if (da.getOperate_type() == 1) {
				da.setCHURLTYPE("浏览数");
			} else if (da.getOperate_type() == 2) {
				da.setCHURLTYPE("下载数");
			} else if (da.getOperate_type() == 3) {
				da.setCHURLTYPE("检索数");
			} else if (da.getOperate_type() == 4) {
				da.setCHURLTYPE("分享数");
			} else if (da.getOperate_type() == 5) {
				da.setCHURLTYPE("收藏数");
			} else if (da.getOperate_type() == 6) {
				da.setCHURLTYPE("导出数");
			} else if (da.getOperate_type() == 7) {
				da.setCHURLTYPE("笔记数");
			} else if (da.getOperate_type() == 8) {
				da.setCHURLTYPE("订阅数");
			}
		}

		for (ResourceStatisticsHour da : li) {
			liname.add(da.getCHURLTYPE());
		}

		for (String name : liname) {
			if (!linames.contains(name)) {
				linames.add(name);
			}
		}

		for (Date date : lidate) {
			if (!lidates.contains(date)) {
				lidates.add(date);
			}
		}
		List<Date> alldate = new ArrayList<Date>();
		if (lidates.size() > 0) {

			Date sd = new Date();
			Date ed = new Date();
			if (starttime != null && !"".equals(starttime) && endtime != null
					&& !"".equals(endtime)) {
				try {
					sd = format.parse(starttime);
					ed = format.parse(endtime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				sd = lidates.get(0);
				ed = lidates.get(lidates.size() - 1);
			}

			alldate = this.getDate(sd, ed);
		}

		List<ResourceStatisticsHour> obj = new ArrayList<ResourceStatisticsHour>();
		boolean rt = true;
		for (String name : linames) {
			for (Date d : alldate) {
				for (ResourceStatisticsHour l : li) {
					if (l.getCHURLTYPE().equals(name) && l.getDate().equals(d)) {
						rt = false;
					}
				}
				if (rt) {
					ResourceStatisticsHour rs = new ResourceStatisticsHour();
					rs.setDate(d);
					rs.setCHURLTYPE(name);
					rs.setNumbers("0");
					obj.add(rs);
				}
				rt = true;
			}
		}

		li.addAll(obj);

		Collections.sort(li, new Comparator<ResourceStatisticsHour>() {
			@Override
			public int compare(ResourceStatisticsHour arg0, ResourceStatisticsHour arg1) {
				Date hits0 = arg0.getDate();
				Date hits1 = arg1.getDate();
				if (hits1.getTime() < hits0.getTime()) {
					return 1;
				} else if (hits1.getTime() == hits0.getTime()) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		Map<String, List<String>> m = new HashMap<String, List<String>>();
		for (ResourceStatisticsHour l : li) {
			String typename = l.getCHURLTYPE();
			if (m.get(typename) != null) {
				m.get(typename).add(l.getNumbers());
			} else {
				List<String> num = new ArrayList<String>();
				num.add(l.getNumbers());
				m.put(typename, num);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> date = new ArrayList<String>();
		for (Date d : alldate) {
			date.add(format.format(d));
		}

		map.put("content", m);
		map.put("title", linames);
		map.put("date", date);
		return map;
	}

	public Map<String, Object> getLineByCheckMore(String starttime,
			String endtime, ResourceStatistics res, String[] sourceTypeName) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> lidate = new ArrayList<Date>();
		List<String> liname = new ArrayList<String>();
		List<Date> lidates = new ArrayList<Date>();
		List<String> linames = new ArrayList<String>();
		List<ResourceStatisticsHour> li=new ArrayList<ResourceStatisticsHour>();

		if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
			 li = this.hour.getLineByCheckMore_day(starttime, endtime, res, sourceTypeName);
		} else if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isNotBlank(res.getUserId())) {
		String userType = personMapper.getUserTypeByUserId(res.getUserId());
			if (userType.equals("0")) {
				// userType等于0为个人用户
				 li = this.hour.getLineByCheckMore_day(starttime, endtime, res, sourceTypeName);
			}else{
				li=this.hour.getLineByCheckMore_IsInstitution_day(starttime, endtime, res, sourceTypeName);
			}
		} else {
			li=this.hour.getLineByCheckMore_IsInstitution_day(starttime, endtime, res, sourceTypeName);
		}

		for (ResourceStatisticsHour da : li) {
			lidate.add(da.getDate());
		}

		for (ResourceStatisticsHour da : li) {
			liname.add(da.getSourceTypeName());
		}

		for (String name : liname) {
			if (!linames.contains(name)) {
				linames.add(name);
			}
		}

		for (Date date : lidate) {
			if (!lidates.contains(date)) {
				lidates.add(date);
			}
		}

		List<Date> alldate = new ArrayList<Date>();

		if (lidates.size() > 0) {
			Date sd = new Date();
			Date ed = new Date();
			if (starttime != null && !"".equals(starttime) && endtime != null
					&& !"".equals(endtime)) {
				try {
					sd = format.parse(starttime);
					ed = format.parse(endtime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				sd = lidates.get(0);
				ed = lidates.get(lidates.size() - 1);
			}

			alldate = this.getDate(sd, ed);
		}

		List<ResourceStatisticsHour> obj = new ArrayList<ResourceStatisticsHour>();
		boolean rt = true;
		for (String name : linames) {
			for (Date d : alldate) {
				for (ResourceStatisticsHour l : li) {
					if (null != l.getSourceName()) {
						if (l.getSourceTypeName().equals(name)
								&& l.getDate().equals(d)) {
							rt = false;
						}
					}
				}
				if (rt) {
					ResourceStatisticsHour rs = new ResourceStatisticsHour();
					rs.setDate(d);
					rs.setSourceTypeName(name);
					rs.setNumbers("0");
					obj.add(rs);
				}
				rt = true;
			}
		}

		li.addAll(obj);

		Collections.sort(li, new Comparator<ResourceStatisticsHour>() {
			@Override
			public int compare(ResourceStatisticsHour arg0, ResourceStatisticsHour arg1) {
				Date hits0 = arg0.getDate();
				Date hits1 = arg1.getDate();
				if (hits1.getTime() < hits0.getTime()) {
					return 1;
				} else if (hits1.getTime() == hits0.getTime()) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		Map<String, List<String>> m = new HashMap<String, List<String>>();
		for (ResourceStatisticsHour l : li) {
			String typename = l.getSourceTypeName();
			if (m.get(typename) != null) {
				m.get(typename).add(l.getNumbers());
			} else {
				List<String> num = new ArrayList<String>();
				num.add(l.getNumbers());
				m.put(typename, num);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> date = new ArrayList<String>();
		for (Date d : alldate) {
			date.add(format.format(d));
		}

		map.put("content", m);
		map.put("title", linames);
		map.put("date", date);
		return map;
	}


	public List<Object> gethourtable(List<ResourceStatisticsHour> li){
		List<String> liname = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		List<Object> rb = new ArrayList<Object>();

		for (ResourceStatisticsHour res : li) {
			liname.add(res.getSourceTypeName());
		}

		for (String name : liname) {
			if (!linames.contains(name)) {
				linames.add(name);
			}
		}

		for (String name : linames) {
			if (null != name) {
				ResourceTableBean b = new ResourceTableBean();
				for (ResourceStatisticsHour res : li) {
					if (null != res.getSourceTypeName()) {
						if (res.getSourceTypeName().equals(name)) {
							b.setSourceTypeName(name);
							b.setSourceName(res.getSourceName());
							int num = Integer.parseInt(res.getNumbers());
							if (res.getOperate_type().equals(1)) {
								b.setBrowseNum(num);
							}
							if (res.getOperate_type().equals(2)) {
								b.setDownloadNum(num);
							}
							if (res.getOperate_type().equals(3)) {
								b.setSearchNum(num);
							}
							if (res.getOperate_type().equals(4)) {
								b.setShareNum(num);
							}
							if (res.getOperate_type().equals(5)) {
								b.setCollectNum(num);
							}
							if (res.getOperate_type().equals(6)) {
								b.setExportNum(num);
							}
							if (res.getOperate_type().equals(7)) {
								b.setNoteNum(num);
							}
							if (res.getOperate_type().equals(8)) {
								b.setSubscibeNum(num);
							}

							if (b.getBrowseNum() == null) {
								b.setBrowseNum(0);
							}
							if (b.getDownloadNum() == null) {
								b.setDownloadNum(0);
							}
							if (b.getSearchNum() == null) {
								b.setSearchNum(0);
							}
							if (b.getShareNum() == null) {
								b.setShareNum(0);
							}
							if (b.getCollectNum() == null) {
								b.setCollectNum(0);
							}
							if (b.getExportNum() == null) {
								b.setExportNum(0);
							}
							if (b.getNoteNum() == null) {
								b.setNoteNum(0);
							}
							if (b.getSubscibeNum() == null) {
								b.setSubscibeNum(0);
							}
						}
					}
				}
				rb.add(b);
			}
		}

		return rb;
	}


	public List<Object> getonehourtable(List<ResourceStatisticsHour> li){
		List<String> liname = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		List<Object> rb = new ArrayList<Object>();

		for (ResourceStatisticsHour res : li) {
			liname.add(res.getTitle());
		}

		for (String name : liname) {
			if (!linames.contains(name)) {
				linames.add(name);
			}
		}

		for (String name : linames) {
			if (null != name) {
				ResourceTableBean b = new ResourceTableBean();
				for (ResourceStatisticsHour res : li) {
					if (null != res.getTitle()) {
						if (res.getTitle().equals(name)) {
							b.setSourceName(b.getSourceName());
							b.setSourceTypeName(res.getSourceTypeName());
							b.setTitle(name);
							int num = Integer.parseInt(res.getNumbers());
							if (res.getOperate_type().equals(1)) {
								b.setBrowseNum(num);
							}
							if (res.getOperate_type().equals(2)) {
								b.setDownloadNum(num);
							}
							if (res.getOperate_type().equals(3)) {
								b.setSearchNum(num);
							}
							if (res.getOperate_type().equals(4)) {
								b.setShareNum(num);
							}
							if (res.getOperate_type().equals(5)) {
								b.setCollectNum(num);
							}
							if (res.getOperate_type().equals(6)) {
								b.setExportNum(num);
							}
							if (res.getOperate_type().equals(7)) {
								b.setNoteNum(num);
							}
							if (res.getOperate_type().equals(8)) {
								b.setSubscibeNum(num);
							}

							if (b.getBrowseNum() == null) {
								b.setBrowseNum(0);
							}
							if (b.getDownloadNum() == null) {
								b.setDownloadNum(0);
							}
							if (b.getSearchNum() == null) {
								b.setSearchNum(0);
							}
							if (b.getShareNum() == null) {
								b.setShareNum(0);
							}
							if (b.getCollectNum() == null) {
								b.setCollectNum(0);
							}
							if (b.getExportNum() == null) {
								b.setExportNum(0);
							}
							if (b.getNoteNum() == null) {
								b.setNoteNum(0);
							}
							if (b.getSubscibeNum() == null) {
								b.setSubscibeNum(0);
							}
						}
					}
				}
				rb.add(b);
			}
		}

		return  rb;
	}

}
