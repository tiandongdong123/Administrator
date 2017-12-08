package com.wf.service.impl;

import java.text.DecimalFormat;
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
import org.apache.ecs.xhtml.address;
import org.apache.ecs.xhtml.map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.KylinJDBC;
import com.wf.bean.ModelTable;
import com.wf.bean.PageList;
import com.wf.dao.FunctionPageDailyMapper;
import com.wf.dao.FunctionPageMapper;
import com.wf.dao.ModularMapper;
import com.wf.dao.PersonMapper;
import com.wf.service.ModelAnalysisService;
@Service
public class ModelAnalysisServiceImpl implements ModelAnalysisService {

	@Autowired
	private ModularMapper modular;
	@Autowired
	private PersonMapper personMapper;
	
	@Autowired
	private FunctionPageDailyMapper functionPageDailyMapper;
	@Autowired
	private FunctionPageMapper functionPageMapper;
	
	@Override
	public Map<String,Object> getline(String title,String age,String exlevel,String datetype,String model,Integer type,String starttime,String endtime,String domain,Integer property) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		
		String days=getDayByDateType(datetype, starttime, endtime);
		String agestr=getAges(age);
		String modelstr=getModels(model);
		type=type==5?3:type;
		
		Map<String , Object> map=new HashMap<String, Object>();
		map.put("days", days);
		map.put("title", title);
		map.put("education_level", exlevel);
		map.put("age", agestr);
		map.put("topic",domain);
		map.put("model",modelstr);
		map.put("property",property);
		map.put("type",type);

		List<Object> list=new ArrayList<>();
		
		List<String> reslistnames=new ArrayList<>();
		List<String> reslistname=new ArrayList<>();
		List<String> alldate = new ArrayList<String>();
		
		if(datetype.equals("1") ||
				(starttime.equals(endtime) 
						&&StringUtils.isNotBlank(starttime)
						&& StringUtils.isNotBlank(endtime))){
			list=functionPageMapper.modelanalysis_view(map);

			for (int i = 0; i <=23; i++) {
				if(i<10){
					alldate.add("0"+i);
				}else{
					alldate.add(i+"");
				}
				
			}
		}else{
			list=functionPageDailyMapper.modelanalysis_view(map);
			alldate=this.getMonthList(datetype, starttime, endtime,"yyyy-MM-dd");
		}
		
		
		if(list.size()>0){

			for(Object li :list){
				reslistnames.add(((Map<String, Object>)li).get("classify").toString());
			}
			
			for (String name : reslistnames) {
				if (!reslistname.contains(name)) {
					reslistname.add(name);
				}
			}
			
			List<Map<String,String>> obj = new ArrayList<Map<String,String>>();
			boolean rt = true;
			for (String name : reslistname) {
				for (String d : alldate) {
					for (Object l : list) {
						if (((Map<String, Object>)l).get("classify").equals(name)
								&& ((Map<String, Object>)l).get("date").equals(d)) {
							rt = false;
						}
					}
					if (rt) {
						Map<String,String> item = new HashMap<String, String>();
						item.put("classify", name);
						item.put("date", d);
						item.put("number", "0");
						obj.add(item);
					}
					rt = true;
				}
			}
			
			list.addAll(obj);
			
			if(datetype.equals("1") ||
					(starttime.equals(endtime) 
							&&StringUtils.isNotBlank(starttime)
							&& StringUtils.isNotBlank(endtime))){
				
				Collections.sort(list, new Comparator<Object>() {
					@Override
					public int compare(Object arg0, Object arg1) {
						String hits0 = ((Map<String, Object>)arg0).get("date").toString();
						String hits1 = ((Map<String, Object>)arg1).get("date").toString();
						if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
							return 1;
						} else if (hits1.equals(hits0) ) {
							return 0;
						} else {
							return -1;
						}
					}
				});

			}else{
				Collections.sort(list, new Comparator<Object>() {
					@Override
					public int compare(Object arg0, Object arg1) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Date hits0 = new Date();
						Date hits1 = new Date();
						try {
							
							hits0=format.parse(((Map<String, Object>)arg0).get("date").toString());
							hits1 = format.parse(((Map<String, Object>)arg1).get("date").toString());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						if ( hits1.getTime() < hits0.getTime()) {
							return 1;
						} else if (hits1 .equals(hits0) ) {
							return 0;
						} else {
							return -1;
						}
					}
				});

			}
			
			Map<String, List<String>> m = new HashMap<String, List<String>>();
			for (Object l : list) {
				String typename =((Map<String,Object>)l).get("classify").toString();
				if (m.get(typename) != null) {
					m.get(typename).add(((Map<String,Object>)l).get("number").toString());
				} else {
					List<String> num = new ArrayList<String>();
					num.add(((Map<String,Object>)l).get("number").toString());
					m.put(typename, num);
				}
			}
			
			datamap.put("content", m);
			datamap.put("title", reslistname);
			datamap.put("date", alldate);
		}		
		return datamap;
	}
	
	@Override
	public PageList gettable(Integer pagesize,Integer pagenum,String title,String age,String exlevel,String datetype,String model,String starttime,String endtime,Integer type,String domain,Integer property){
		
		PageList pageList=new PageList();
		List<Object> list=new ArrayList<>();
		List<Object> l=new ArrayList<>();
		int count=0;
		double COUNT_USER=personMapper.countUser();
		String modelstr=this.getModels(model);
		String agestr =this.getAges(age);
		
		String days = getDayByDateType(datetype,starttime,endtime);
		
		Map<String , Object> map=new HashMap<String, Object>();
		map.put("pageNum", (pagenum-1)*pagesize);
		map.put("pageSize",pagesize);
		map.put("days", days);
		map.put("title", title);
		map.put("education_level", exlevel);
		map.put("age", agestr);
		map.put("topic",domain);
		map.put("model",modelstr);
		map.put("property",property);
		map.put("date",this.getMonthList(datetype,starttime,endtime,"yyyy-MM"));
		
		if(datetype.equals("1") ||
				(starttime.equals(endtime) 
					&&StringUtils.isNotBlank(starttime)
					&& StringUtils.isNotBlank(endtime))){
			list=functionPageMapper.modelanalysis_table(map);
			count=functionPageMapper.modelanalysis_count(map).size();
			l=functionPageMapper.getMAU(map);
		}else{
			list=functionPageDailyMapper.modelanalysis_table(map);
			count=functionPageDailyMapper.modelanalysis_count(map).size();
			l=functionPageDailyMapper.getMAU(map);
		}
		
		for (Object object : list) {
		     Map<String, Object> item=(Map<String, Object>)object;
		     double UV=Double.valueOf(item.get("UV")+"");
		     item.put("AR",(((int)(UV/COUNT_USER))*100)+"%");
		     
		     for (Object object2 : l) {
		    	 Map<String, Object> item2=(Map<String, Object>)object2;
				if(item2.get("classify").toString().equals(item.get("classify").toString())){
					
					float num= (float)(UV/Double.valueOf(item2.get("UV")+""));   
					DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
					String s = df.format(num);
					item.put("VI",(Float.valueOf(s)*100)+"%");
					break;
				}
			}
		     
		}
		
		
		pageList.setPageNum(pagenum);
		pageList.setPageNum(pagesize);
		pageList.setTotalRow(count);
		pageList.setPageRow(list);
		
		return pageList;
	}
	
	
	@Override
	public List<String> getmodular() {
		List<String> namelist = new ArrayList<String>();
		try {
			namelist = this.modular.getModularNameList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return namelist;
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
	
	private String getDayByDateType(String datetype,String starttime,String endtime){
		
		String days="";
		
		if(datetype.equals("1")){
			Calendar   cal   =   Calendar.getInstance();
			cal.add(Calendar.DATE,   -1);
			days= "'"+new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime())+"'";
			
		}else if(datetype.equals("2")||datetype.equals("3")){
				
			int num =datetype.equals("2")?6:29;
			
			for(int k=num;k>0;k--){
				Calendar   cal   =   Calendar.getInstance();
				cal.add(Calendar.DATE,   -k);
				String day = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
				days = days+"'"+day+"'"+",";
			}
			days=days+"'"+new SimpleDateFormat( "yyyy-MM-dd").format(new Date())+"'"+",";
			days=days.substring(0,days.length()-1);
			
		}else{
			
			if(starttime.equals(endtime)){
				days="'"+endtime+"'";
			}else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date sd = format.parse(starttime);
					Date ed = format.parse(endtime);
					List<Date> date = this.getDate(sd, ed);
					for(Date d : date){
						days = days+"'"+format.format(d)+"'"+",";
					}
					days=days.substring(0,days.length()-1);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return days;
	}
	
	private List<String> getMonthList(String datetype,String starttime,String endtime,String formatstr){
		
		List<String> alldate=new ArrayList<>();
		
		if(datetype.equals("1")){
			Calendar   cal   =   Calendar.getInstance();
			cal.add(Calendar.DATE,   -1);
			alldate.add(new SimpleDateFormat(formatstr).format(cal.getTime()));
		}else if(datetype.equals("2")||datetype.equals("3")){
			int num =datetype.equals("2")?6:29;
			for(int k=num;k>0;k--){
				Calendar   cal   =   Calendar.getInstance();
				cal.add(Calendar.DATE,   -k);
				String day = new SimpleDateFormat(formatstr).format(cal.getTime());
				alldate.add(day);
			}
		}else{
			
			if(starttime.equals(endtime)){
				alldate.add(endtime);
			}else {
				SimpleDateFormat format = new SimpleDateFormat(formatstr);
				try {
					Date sd = format.parse(starttime);
					Date ed = format.parse(endtime);
					List<Date> date = this.getDate(sd, ed);
					for(Date d : date){
						alldate.add(format.format(d));
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return alldate;
	}

	public String getModels(String model){
		
		String modelstr="";		
		if(model!=null&&!"".equals(model)){
			String[] mo = model.split(",");
			for(String m : mo){
				modelstr=modelstr+"'"+m+"'"+",";
			}
			modelstr = modelstr.substring(0,modelstr.length()-1);
		}
		
		return modelstr;

	}
	
	public String getAges(String age){
		String agestr="";
		
		if(age!=agestr&&!"".equals(age)){
			String[] ages = age.split(",");
			for(String a : ages){
				agestr=agestr+"'"+a+"'"+",";
			}
			agestr = agestr.substring(0,agestr.length()-1);
		}
		
		return agestr;
	}

}
