package com.wf.service.impl;

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
import com.wf.dao.ResourceStatisticsHourMapper;
import com.wf.dao.ResourceStatisticsMapper;
import com.wf.dao.ResourceTypeMapper;
import com.wf.service.ResourceTypeStatisticsService;

@Service
public class ResourceTypeStatisticsServiceImpl implements
		ResourceTypeStatisticsService {

	@Autowired
	private ResourceTypeMapper type;
	@Autowired
	private ResourceStatisticsMapper restatistics;
	@Autowired
	private ResourceStatisticsHourMapper hour;

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
	public Map<String,Object> getAllLine(String starttime, String endtime,
			ResourceStatistics res,Integer[] urls,Integer singmore){
		Map<String,Object> map = new HashMap<String, Object>();
		
			if(null!=starttime&&!"".equals(starttime)&&null!=endtime&&!"".equals(endtime)&&starttime.equals(endtime)){
				if(singmore==0){
					map =this.getHourLineMore(starttime, endtime,  res,urls);
				}else{
					map =this.getHourLine(starttime, endtime, res);
				}
			}else{
				if(singmore!=null && !singmore.equals("") && singmore==0){
					map = this.getLineMore(starttime, endtime,  res, urls);
				}else{
					map = this.getLine(starttime, endtime, res );
				}
			}
		return map;
		
	}

	
	
	
	
	public Map<String, Object> getLine(String starttime, String endtime,
			ResourceStatistics res) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> lidate = new ArrayList<Date>();
		List<String> liname = new ArrayList<String>();
		List<Date> lidates = new ArrayList<Date>();
		List<String> linames = new ArrayList<String>();
		
		List<ResourceStatistics> li = this.restatistics.getLine(starttime,
				endtime, res);
		
		for (ResourceStatistics da : li) {
			lidate.add(da.getDate());
		}

		for (ResourceStatistics da : li) {
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
		
		if(lidates.size()>0){
			Date sd = new Date();
			Date ed = new Date();
			if(starttime!=null&&!"".equals(starttime)&&endtime!=null&&!"".equals(endtime)){
				try {
					sd = format.parse(starttime);
					ed = format.parse(endtime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				sd = lidates.get(0);
				ed = lidates.get(lidates.size()-1);
			}
			
			alldate =this.getDate(sd, ed);
		}
		
		List<ResourceStatistics> obj = new ArrayList<ResourceStatistics>();
		boolean rt = true;
		for (String name : linames) {
			for (Date d : alldate) {
				for (ResourceStatistics l : li) {
					if(null!=l.getSourceName()){
						if (l.getSourceTypeName().equals(name)
								&& l.getDate().equals(d)) {
							rt = false;
						}
					}
				}
				if (rt) {
					ResourceStatistics rs = new ResourceStatistics();
					rs.setDate(d);
					rs.setSourceTypeName(name);
					rs.setNumbers("0");
					obj.add(rs);
				}
				rt = true;
			}
		}

		li.addAll(obj);

		Collections.sort(li, new Comparator<ResourceStatistics>() {
			@Override
			public int compare(ResourceStatistics arg0, ResourceStatistics arg1) {
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
		for (ResourceStatistics l : li) {
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

	
	public Map<String, Object> getHourLine(String starttime, String endtime,
			ResourceStatistics res) {
		List<String> lidate = new ArrayList<String>();
		List<String> liname = new ArrayList<String>();
		List<String> lidates = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		
		List<ResourceStatisticsHour> li = this.hour.getLine(starttime, endtime,  res,starttime);
		
		for (ResourceStatisticsHour da : li) {
			lidate.add(da.getHour());
		}

		for (ResourceStatisticsHour da : li) {
			if(StringUtils.isNotBlank(da.getSourceTypeName())){
				liname.add(da.getSourceTypeName());
			}
			
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
		for(int i=1;i<=24;i++){
			alldate.add(i+"");
		}
		List<ResourceStatisticsHour> obj = new ArrayList<ResourceStatisticsHour>();
		boolean rt = true;
		for (String name : linames) {
			for (String d : alldate) {
				for (ResourceStatisticsHour l : li) {
					if(StringUtils.isNotBlank(l.getSourceTypeName())){
						if (l.getSourceTypeName().equals(name)
								&& l.getHour().equals(d)) {
							rt = false;
						}
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
			public int compare(ResourceStatisticsHour arg0, ResourceStatisticsHour arg1) {
				String hits0 = arg0.getHour();
				String hits1 = arg1.getHour();
				if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
					return 1;
				} else if (hits1 .equals(hits0) ) {
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
	
	
	public Map<String, Object> getHourLineMore(String starttime, String endtime,
			ResourceStatistics res, Integer[] urls) {
		List<String> lidate = new ArrayList<String>();
		List<String> liname = new ArrayList<String>();
		List<String> lidates = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		
		List<ResourceStatisticsHour> li = this.hour.getLineMore(starttime, endtime, res, urls,starttime);
		
		for (ResourceStatisticsHour da : li) {
			lidate.add(da.getHour());
		}
		
		for(ResourceStatisticsHour da : li){
			if(da.getUrlType()==1){
				da.setCHURLTYPE("浏览数");
			}else if(da.getUrlType()==2){
				da.setCHURLTYPE("下载数");
			}else if(da.getUrlType()==3){
				da.setCHURLTYPE("检索数");		
			}
			else if(da.getUrlType()==4){
				da.setCHURLTYPE("分享数");
			}
			else if(da.getUrlType()==5){
				da.setCHURLTYPE("收藏数");
			}
			else if(da.getUrlType()==6){
				da.setCHURLTYPE("导出数");
			}
			else if(da.getUrlType()==7){
				da.setCHURLTYPE("笔记数");
			}
			else if(da.getUrlType()==8){
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
		for(int i=1;i<=24;i++){
			alldate.add(i+"");
		}

		List<ResourceStatisticsHour> obj = new ArrayList<ResourceStatisticsHour>();
		boolean rt = true;
		for (String name : linames) {
			for (String d : alldate) {
				for (ResourceStatisticsHour l : li) {
					if (l.getCHURLTYPE().equals(name)
							&& l.getDate().equals(d)) {
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
			public int compare(ResourceStatisticsHour arg0, ResourceStatisticsHour arg1) {
				String hits0 = arg0.getHour();
				String hits1 = arg1.getHour();
				if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
					return 1;
				} else if (hits1 .equals(hits0) ) {
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
	
	
	
	@Override
	public PageList gettable(Integer table, String starttime, String endtime,
			ResourceStatistics res,Integer pagenum,Integer pagesize) {
		PageList pl = new PageList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (starttime != null && !"".equals(starttime) && endtime != null
					&& !"".equals(endtime)) {
				Date start = sdf.parse(starttime);
				Date end = sdf.parse(endtime);
				
				int cha=end.getDate()-start.getDate();
				
				//long cha = end.getTime() - start.getTime();
				//double result = cha * 1.0 / (1000 * 60 * 60);
				if (cha<1) {
					pl = this.hourtable(table, starttime, endtime,res,pagenum,pagesize);
				} else {
					pl = this.daytable(table, starttime, endtime, res,pagenum,pagesize);
				}

			} else {
				pl = this.daytable(table, starttime, endtime, res,pagenum,pagesize);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pl;
	}

	public PageList daytable(Integer table, String starttime, String endtime,
			ResourceStatistics resouser,Integer pagenum,Integer pagesize) {
		List<ResourceStatistics> li = new ArrayList<ResourceStatistics>();
		List<String> liname = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		List<Object> rb = new ArrayList<Object>();

		if (table.equals(0)) {
			li = this.restatistics.gettable(starttime, endtime,resouser);
			for (ResourceStatistics res : li) {
				liname.add(res.getSourceTypeName());
			}

			for (String name : liname) {
				if (!linames.contains(name)) {
					linames.add(name);
				}
			}

			for (String name : linames) {
				if(null!=name){
					ResourceTableBean b = new ResourceTableBean();
					for (ResourceStatistics res : li) {
						if(null!=res.getSourceTypeName()){
							if (res.getSourceTypeName().equals(name)) {
								b.setSourceTypeName(name);
								b.setSourceName(res.getSourceName());
								int num = Integer.parseInt(res.getNumbers());
								
								if(null!=res.getUrlType()){
									if (res.getUrlType().equals(1)) {
										b.setBrowseNum(num);
									}
									if (res.getUrlType().equals(2)) {
										b.setDownloadNum(num);
									}
									if (res.getUrlType().equals(3)) {
										b.setSearchNum(num);
									}
									if (res.getUrlType().equals(4)) {
										b.setShareNum(num);
									}
									if (res.getUrlType().equals(5)) {
										b.setCollectNum(num);
									}
									if (res.getUrlType().equals(6)) {
										b.setExportNum(num);
									}
									if (res.getUrlType().equals(7)) {
										b.setNoteNum(num);
									}
									if (res.getUrlType().equals(8)) {
										b.setSubscibeNum(num);
									}
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
		} else {
			li = this.restatistics.getonetable(starttime, endtime, resouser);
			for (ResourceStatistics res : li) {
				liname.add(res.getSourceTypeName());
			}

			for (String name : liname) {
				if (!linames.contains(name)) {
					linames.add(name);
				}
			}
			for (String name : linames) {
				if(null!=name){
					ResourceTableBean b = new ResourceTableBean();
					for (ResourceStatistics res : li) {
						if(null!=res.getSourceTypeName()){
							if (res.getSourceTypeName().equals(name)) {
								b.setSourceName(res.getSourceName());
								b.setSourceTypeName(res.getSourceTypeName());
								int num = Integer.parseInt(res.getNumbers());
								if (res.getUrlType().equals(1)) {
									b.setBrowseNum(num);
								}
								if (res.getUrlType().equals(2)) {
									b.setDownloadNum(num);
								}
								if (res.getUrlType().equals(3)) {
									b.setSearchNum(num);
								}
								if (res.getUrlType().equals(4)) {
									b.setShareNum(num);
								}
								if (res.getUrlType().equals(5)) {
									b.setCollectNum(num);
								}
								if (res.getUrlType().equals(6)) {
									b.setExportNum(num);
								}
								if (res.getUrlType().equals(7)) {
									b.setNoteNum(num);
								}
								if (res.getUrlType().equals(8)) {
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
		}

		int s = (pagenum-1)*pagesize;
		int n = pagenum*pagesize-1;
		
		List<Object> pageobject = new ArrayList<Object>();
		
		if(n<=rb.size()){
			for (int i = s;i<n;i++){
				pageobject.add(rb.get(i));
			}
		}else{
			for (int i = s;i<rb.size();i++){
				pageobject.add(rb.get(i));
			}
		}
		
		
		PageList pl = new PageList();
			pl.setPageNum(pagenum);
			pl.setPageSize(pagesize);
			pl.setPageTotal(rb.size());
			pl.setPageRow(pageobject);
			
			
			return pl;
	}

	public PageList hourtable(Integer table, String starttime, String endtime,
			ResourceStatistics resour,Integer pagenum,Integer pagesize) {
		List<ResourceStatisticsHour> li = new ArrayList<ResourceStatisticsHour>();
		List<String> liname = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		List<Object> rb = new ArrayList<Object>();
		if (table.equals(0)) {
			li = this.hour.gethourtable(starttime, endtime, resour, starttime);
			for (ResourceStatisticsHour res : li) {
				liname.add(res.getSourceTypeName());
			}

			for (String name : liname) {
				if (!linames.contains(name)) {
					linames.add(name);
				}
			}

			for (String name : linames) {
				if(null!=name){
					ResourceTableBean b = new ResourceTableBean();
					for (ResourceStatisticsHour res : li) {
						if(null!=res.getSourceTypeName()){
							if (res.getSourceTypeName().equals(name)) {
								b.setSourceTypeName(name);
								b.setSourceName(res.getSourceName());
								int num = Integer.parseInt(res.getNumbers());
								if (res.getUrlType().equals(1)) {
									b.setBrowseNum(num);
								}
								if (res.getUrlType().equals(2)) {
									b.setDownloadNum(num);
								}
								if (res.getUrlType().equals(3)) {
									b.setSearchNum(num);
								}
								if (res.getUrlType().equals(4)) {
									b.setShareNum(num);
								}
								if (res.getUrlType().equals(5)) {
									b.setCollectNum(num);
								}
								if (res.getUrlType().equals(6)) {
									b.setExportNum(num);
								}
								if (res.getUrlType().equals(7)) {
									b.setNoteNum(num);
								}
								if (res.getUrlType().equals(8)) {
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
		} else {
			li = this.hour.gethouronetable(starttime, endtime, resour, starttime);
			for (ResourceStatisticsHour res : li) {
				liname.add(res.getSourceName());
			}

			for (String name : liname) {
				if (!linames.contains(name)) {
					linames.add(name);
				}
			}

			for (String name : linames) {
				if(null!=name){
					ResourceTableBean b = new ResourceTableBean();
					for (ResourceStatisticsHour res : li) {
						if(null!=res.getSourceName()){
							if (res.getSourceName().equals(name)) {
								b.setSourceName(name);
								b.setSourceTypeName(res.getSourceTypeName());
								int num = Integer.parseInt(res.getNumbers());
								if (res.getUrlType().equals(1)) {
									b.setBrowseNum(num);
								}
								if (res.getUrlType().equals(2)) {
									b.setDownloadNum(num);
								}
								if (res.getUrlType().equals(3)) {
									b.setSearchNum(num);
								}
								if (res.getUrlType().equals(4)) {
									b.setShareNum(num);
								}
								if (res.getUrlType().equals(5)) {
									b.setCollectNum(num);
								}
								if (res.getUrlType().equals(6)) {
									b.setExportNum(num);
								}
								if (res.getUrlType().equals(7)) {
									b.setNoteNum(num);
								}
								if (res.getUrlType().equals(8)) {
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
		}
		
		int s = (pagenum-1)*pagesize;
		int n = pagenum*pagesize-1;
		
		List<Object> pageobject = new ArrayList<Object>();
		
		if(n<=rb.size()){
			for (int i = s;i<n;i++){
				pageobject.add(rb.get(i));
			}
		}else{
			for (int i = s;i<rb.size();i++){
				pageobject.add(rb.get(i));
			}
		}

		
		PageList pl = new PageList();
			pl.setPageNum(pagenum);
			pl.setPageSize(pagesize);
			pl.setPageTotal(rb.size());
			pl.setPageRow(pageobject);
			
			
			return pl;
	}
	
	public Map<String, Object> getLineMore(String starttime, String endtime,
			ResourceStatistics res, Integer[] urls) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> lidate = new ArrayList<Date>();
		List<String> liname = new ArrayList<String>();
		List<Date> lidates = new ArrayList<Date>();
		List<String> linames = new ArrayList<String>();
		
		List<ResourceStatistics> li = this.restatistics.getLineMore(starttime,
				endtime ,res, urls);
		
		for (ResourceStatistics da : li) {
			lidate.add(da.getDate());
		}
		
		for(ResourceStatistics da :li){
			if(da.getUrlType()==1){
				da.setCHURLTYPE("浏览数");
			}else if(da.getUrlType()==2){
				da.setCHURLTYPE("下载数");
			}else if(da.getUrlType()==3){
				da.setCHURLTYPE("检索数");		
			}
			else if(da.getUrlType()==4){
				da.setCHURLTYPE("分享数");
			}
			else if(da.getUrlType()==5){
				da.setCHURLTYPE("收藏数");
			}
			else if(da.getUrlType()==6){
				da.setCHURLTYPE("导出数");
			}
			else if(da.getUrlType()==7){
				da.setCHURLTYPE("笔记数");
			}
			else if(da.getUrlType()==8){
				da.setCHURLTYPE("订阅数");
			}
		}
		
		

		for (ResourceStatistics da : li) {
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
		if(lidates.size()>0){
			
			Date sd = new Date();
			Date ed = new Date();
			if(starttime!=null&&!"".equals(starttime)&&endtime!=null&&!"".equals(endtime)){
				try {
					sd = format.parse(starttime);
					ed = format.parse(endtime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				sd = lidates.get(0);
				ed = lidates.get(lidates.size()-1);
			}
			
			alldate =this.getDate(sd, ed);
		}

		
		
		

		List<ResourceStatistics> obj = new ArrayList<ResourceStatistics>();
		boolean rt = true;
		for (String name : linames) {
			for (Date d : alldate) {
				for (ResourceStatistics l : li) {
					if (l.getCHURLTYPE().equals(name)
							&& l.getDate().equals(d)) {
						rt = false;
					}
				}
				if (rt) {
					ResourceStatistics rs = new ResourceStatistics();
					rs.setDate(d);
					rs.setCHURLTYPE(name);
					rs.setNumbers("0");
					obj.add(rs);
				}
				rt = true;
			}
		}

		li.addAll(obj);

		Collections.sort(li, new Comparator<ResourceStatistics>() {
			@Override
			public int compare(ResourceStatistics arg0, ResourceStatistics arg1) {
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
		for (ResourceStatistics l : li) {
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
	
	
	public List<Date> getDate(Date sd,Date ed){
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
	public List<Object> exportresourceType(Integer num, String starttime,
			String endtime, ResourceStatistics res) {
		
		List<Object> list=new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (starttime != null && !"".equals(starttime) && endtime != null
					&& !"".equals(endtime)) {
				Date start = sdf.parse(starttime);
				Date end = sdf.parse(endtime);
				long cha = end.getTime() - start.getTime();
				double result = cha * 1.0 / (1000 * 60 * 60);
				if (result <= 24) {
					list = this.exportByHour(num, starttime, endtime,res);
				} else {
					list = this.exportByDay(num, starttime, endtime,res);
				}

			} else {
				list = this.exportByDay(num, starttime, endtime, res);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public List<Object> exportByHour(Integer table, String starttime, String endtime,
			ResourceStatistics resour){
		
		List<ResourceStatisticsHour> li = new ArrayList<ResourceStatisticsHour>();
		List<String> liname = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		List<Object> rb = new ArrayList<Object>();
		if (table.equals(0)) {
			li = this.hour.gethourtable(starttime, endtime, resour, starttime);
			for (ResourceStatisticsHour res : li) {
				liname.add(res.getSourceTypeName());
			}

			for (String name : liname) {
				if (!linames.contains(name)) {
					linames.add(name);
				}
			}

			for (String name : linames) {
				if(null!=name){
					ResourceTableBean b = new ResourceTableBean();
					for (ResourceStatisticsHour res : li) {
						if(null!=res.getSourceTypeName()){
							if (res.getSourceTypeName().equals(name)) {
								b.setSourceTypeName(name);
								b.setSourceName(res.getSourceName());
								int num = Integer.parseInt(res.getNumbers());
								if (res.getUrlType().equals(1)) {
									b.setBrowseNum(num);
								}
								if (res.getUrlType().equals(2)) {
									b.setDownloadNum(num);
								}
								if (res.getUrlType().equals(3)) {
									b.setSearchNum(num);
								}
								if (res.getUrlType().equals(4)) {
									b.setShareNum(num);
								}
								if (res.getUrlType().equals(5)) {
									b.setCollectNum(num);
								}
								if (res.getUrlType().equals(6)) {
									b.setExportNum(num);
								}
								if (res.getUrlType().equals(7)) {
									b.setNoteNum(num);
								}
								if (res.getUrlType().equals(8)) {
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
		} else {
			li = this.hour.gethouronetable(starttime, endtime, resour, starttime);
			for (ResourceStatisticsHour res : li) {
				liname.add(res.getSourceName());
			}

			for (String name : liname) {
				if (!linames.contains(name)) {
					linames.add(name);
				}
			}

			for (String name : linames) {
				if(null!=name){
					ResourceTableBean b = new ResourceTableBean();
					for (ResourceStatisticsHour res : li) {
						if(null!=res.getSourceName()){
							if (res.getSourceName().equals(name)) {
								b.setSourceName(name);
								b.setSourceTypeName(res.getSourceTypeName());
								int num = Integer.parseInt(res.getNumbers());
								if (res.getUrlType().equals(1)) {
									b.setBrowseNum(num);
								}
								if (res.getUrlType().equals(2)) {
									b.setDownloadNum(num);
								}
								if (res.getUrlType().equals(3)) {
									b.setSearchNum(num);
								}
								if (res.getUrlType().equals(4)) {
									b.setShareNum(num);
								}
								if (res.getUrlType().equals(5)) {
									b.setCollectNum(num);
								}
								if (res.getUrlType().equals(6)) {
									b.setExportNum(num);
								}
								if (res.getUrlType().equals(7)) {
									b.setNoteNum(num);
								}
								if (res.getUrlType().equals(8)) {
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
		}
		
		return rb;
	}
	
	public List<Object> exportByDay(Integer table, String starttime, String endtime,
			ResourceStatistics resouser){
		
		List<ResourceStatistics> li = new ArrayList<ResourceStatistics>();
		List<String> liname = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		List<Object> rb = new ArrayList<Object>();

		if (table.equals(0)) {
			li = this.restatistics.gettable(starttime, endtime,resouser);
			for (ResourceStatistics res : li) {
				liname.add(res.getSourceTypeName());
			}

			for (String name : liname) {
				if (!linames.contains(name)) {
					linames.add(name);
				}
			}

			for (String name : linames) {
				if(null!=name){
					ResourceTableBean b = new ResourceTableBean();
					for (ResourceStatistics res : li) {
						if(null!=res.getSourceTypeName()){
							if (res.getSourceTypeName().equals(name)) {
								b.setSourceTypeName(name);
								b.setSourceName(res.getSourceName());
								int num = Integer.parseInt(res.getNumbers());
								if(null!=res.getUrlType()){
									if (res.getUrlType().equals(1)) {
										b.setBrowseNum(num);
									}
									if (res.getUrlType().equals(2)) {
										b.setDownloadNum(num);
									}
									if (res.getUrlType().equals(3)) {
										b.setSearchNum(num);
									}
									if (res.getUrlType().equals(4)) {
										b.setShareNum(num);
									}
									if (res.getUrlType().equals(5)) {
										b.setCollectNum(num);
									}
									if (res.getUrlType().equals(6)) {
										b.setExportNum(num);
									}
									if (res.getUrlType().equals(7)) {
										b.setNoteNum(num);
									}
									if (res.getUrlType().equals(8)) {
										b.setSubscibeNum(num);
									}
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
		} else {
			li = this.restatistics.getonetable(starttime, endtime, resouser);
			for (ResourceStatistics res : li) {
				liname.add(res.getSourceName());
			}

			for (String name : liname) {
				if (!linames.contains(name)) {
					linames.add(name);
				}
			}
			for (String name : linames) {
				if(null!=name){
					ResourceTableBean b = new ResourceTableBean();
					for (ResourceStatistics res : li) {
						if(null!=res.getSourceName()){
							if (res.getSourceName().equals(name)) {
								b.setSourceName(name);
								b.setSourceTypeName(res.getSourceTypeName());
								int num = Integer.parseInt(res.getNumbers());
								if (res.getUrlType().equals(1)) {
									b.setBrowseNum(num);
								}
								if (res.getUrlType().equals(2)) {
									b.setDownloadNum(num);
								}
								if (res.getUrlType().equals(3)) {
									b.setSearchNum(num);
								}
								if (res.getUrlType().equals(4)) {
									b.setShareNum(num);
								}
								if (res.getUrlType().equals(5)) {
									b.setCollectNum(num);
								}
								if (res.getUrlType().equals(6)) {
									b.setExportNum(num);
								}
								if (res.getUrlType().equals(7)) {
									b.setNoteNum(num);
								}
								if (res.getUrlType().equals(8)) {
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
		}

		return rb;
	}


	@Override
	public Map<String, Object> getAllLineByCheckMore(String starttime,
			String endtime,ResourceStatistics res, String[] sourceTypeName, Integer[] urls,
			Integer singmore) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		if(starttime.equals(endtime)&&starttime!=null&&!"".equals(starttime)&&endtime!=null&&!"".equals(endtime)){
			if(singmore==0){
				map =this.getHourLineMoreByCheckMore(starttime, endtime, res, sourceTypeName, urls);
			}else{
				map =this.getHourLineByCheckMore(starttime, endtime, res, sourceTypeName);
			}
		}else{
			if(singmore==0){
				map = this.getLineMoreByCheckMore(starttime, endtime, res, sourceTypeName, urls);
			}else{
				map = this.getLineByCheckMore(starttime, endtime, res, sourceTypeName);
			}
		}
	return map;
		
	}
	
	
	public Map<String, Object> getHourLineMoreByCheckMore(String starttime, String endtime,
			ResourceStatistics res,String[]sourceTypeName, Integer[] urls) {
		List<String> lidate = new ArrayList<String>();
		List<String> liname = new ArrayList<String>();
		List<String> lidates = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		
		List<ResourceStatisticsHour> li = this.hour.getLineMoreByCheckMore(starttime, endtime, res, sourceTypeName, urls,starttime);
		
		for (ResourceStatisticsHour da : li) {
			lidate.add(da.getHour());
		}
		
		for(ResourceStatisticsHour da : li){
			if(da.getUrlType()==1){
				da.setCHURLTYPE("浏览数");
			}else if(da.getUrlType()==2){
				da.setCHURLTYPE("下载数");
			}else if(da.getUrlType()==3){
				da.setCHURLTYPE("检索数");		
			}
			else if(da.getUrlType()==4){
				da.setCHURLTYPE("分享数");
			}
			else if(da.getUrlType()==5){
				da.setCHURLTYPE("收藏数");
			}
			else if(da.getUrlType()==6){
				da.setCHURLTYPE("导出数");
			}
			else if(da.getUrlType()==7){
				da.setCHURLTYPE("笔记数");
			}
			else if(da.getUrlType()==8){
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
		for(int i=1;i<=24;i++){
			alldate.add(i+"");
		}

		List<ResourceStatisticsHour> obj = new ArrayList<ResourceStatisticsHour>();
		boolean rt = true;
		for (String name : linames) {
			for (String d : alldate) {
				for (ResourceStatisticsHour l : li) {
					if (l.getCHURLTYPE().equals(name)
							&& l.getDate().equals(d)) {
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
			public int compare(ResourceStatisticsHour arg0, ResourceStatisticsHour arg1) {
				String hits0 = arg0.getHour();
				String hits1 = arg1.getHour();
				if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
					return 1;
				} else if (hits1 .equals(hits0) ) {
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
	
	
	public Map<String, Object> getHourLineByCheckMore(String starttime, String endtime,ResourceStatistics res,
			String[]sourceTypeName) {
		List<String> lidate = new ArrayList<String>();
		List<String> liname = new ArrayList<String>();
		List<String> lidates = new ArrayList<String>();
		List<String> linames = new ArrayList<String>();
		
		List<ResourceStatisticsHour> li = this.hour.getLineByCheckMore(starttime, endtime, res, sourceTypeName,starttime);
		
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
		for(int i=1;i<=24;i++){
			alldate.add(i+"");
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
			public int compare(ResourceStatisticsHour arg0, ResourceStatisticsHour arg1) {
				String hits0 = arg0.getHour();
				String hits1 = arg1.getHour();
				if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
					return 1;
				} else if (hits1 .equals(hits0) ) {
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
	
	
	public Map<String, Object> getLineMoreByCheckMore(String starttime, String endtime,ResourceStatistics res,
			String[]sourceTypeName, Integer[] urls) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> lidate = new ArrayList<Date>();
		List<String> liname = new ArrayList<String>();
		List<Date> lidates = new ArrayList<Date>();
		List<String> linames = new ArrayList<String>();
		
		List<ResourceStatistics> li = this.restatistics.getLineMoreByCheckMore(starttime, endtime, res, sourceTypeName, urls);
		
		for (ResourceStatistics da : li) {
			lidate.add(da.getDate());
		}
		
		for(ResourceStatistics da :li){
			if(da.getUrlType()==1){
				da.setCHURLTYPE("浏览数");
			}else if(da.getUrlType()==2){
				da.setCHURLTYPE("下载数");
			}else if(da.getUrlType()==3){
				da.setCHURLTYPE("检索数");		
			}
			else if(da.getUrlType()==4){
				da.setCHURLTYPE("分享数");
			}
			else if(da.getUrlType()==5){
				da.setCHURLTYPE("收藏数");
			}
			else if(da.getUrlType()==6){
				da.setCHURLTYPE("导出数");
			}
			else if(da.getUrlType()==7){
				da.setCHURLTYPE("笔记数");
			}
			else if(da.getUrlType()==8){
				da.setCHURLTYPE("订阅数");
			}
		}
		
		

		for (ResourceStatistics da : li) {
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
		if(lidates.size()>0){
			
			Date sd = new Date();
			Date ed = new Date();
			if(starttime!=null&&!"".equals(starttime)&&endtime!=null&&!"".equals(endtime)){
				try {
					sd = format.parse(starttime);
					ed = format.parse(endtime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				sd = lidates.get(0);
				ed = lidates.get(lidates.size()-1);
			}
			
			alldate =this.getDate(sd, ed);
		}

		
		
		

		List<ResourceStatistics> obj = new ArrayList<ResourceStatistics>();
		boolean rt = true;
		for (String name : linames) {
			for (Date d : alldate) {
				for (ResourceStatistics l : li) {
					if (l.getCHURLTYPE().equals(name)
							&& l.getDate().equals(d)) {
						rt = false;
					}
				}
				if (rt) {
					ResourceStatistics rs = new ResourceStatistics();
					rs.setDate(d);
					rs.setCHURLTYPE(name);
					rs.setNumbers("0");
					obj.add(rs);
				}
				rt = true;
			}
		}

		li.addAll(obj);

		Collections.sort(li, new Comparator<ResourceStatistics>() {
			@Override
			public int compare(ResourceStatistics arg0, ResourceStatistics arg1) {
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
		for (ResourceStatistics l : li) {
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
	
	
	public Map<String, Object> getLineByCheckMore(String starttime, String endtime,ResourceStatistics res,
			String[]sourceTypeName) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> lidate = new ArrayList<Date>();
		List<String> liname = new ArrayList<String>();
		List<Date> lidates = new ArrayList<Date>();
		List<String> linames = new ArrayList<String>();
		
		List<ResourceStatistics> li = this.restatistics.getLineByCheckMore(starttime, endtime, res, sourceTypeName);
		
		for (ResourceStatistics da : li) {
			lidate.add(da.getDate());
		}

		for (ResourceStatistics da : li) {
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
		
		if(lidates.size()>0){
			Date sd = new Date();
			Date ed = new Date();
			if(starttime!=null&&!"".equals(starttime)&&endtime!=null&&!"".equals(endtime)){
				try {
					sd = format.parse(starttime);
					ed = format.parse(endtime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				sd = lidates.get(0);
				ed = lidates.get(lidates.size()-1);
			}
			
			alldate =this.getDate(sd, ed);
		}
		
		

		
		
		

		List<ResourceStatistics> obj = new ArrayList<ResourceStatistics>();
		boolean rt = true;
		for (String name : linames) {
			for (Date d : alldate) {
				for (ResourceStatistics l : li) {
					if(null!=l.getSourceName()){
						if (l.getSourceTypeName().equals(name)
								&& l.getDate().equals(d)) {
							rt = false;
						}
					}
				}
				if (rt) {
					ResourceStatistics rs = new ResourceStatistics();
					rs.setDate(d);
					rs.setSourceTypeName(name);
					rs.setNumbers("0");
					obj.add(rs);
				}
				rt = true;
			}
		}

		li.addAll(obj);

		Collections.sort(li, new Comparator<ResourceStatistics>() {
			@Override
			public int compare(ResourceStatistics arg0, ResourceStatistics arg1) {
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
		for (ResourceStatistics l : li) {
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
	
}
