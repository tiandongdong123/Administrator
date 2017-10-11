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

import com.utils.KylinJDBC;
import com.wf.bean.ModelTable;
import com.wf.bean.PageList;
import com.wf.dao.ModularMapper;
import com.wf.dao.PersonMapper;
import com.wf.service.ModelAnalysisService;
@Service
public class ModelAnalysisServiceImpl implements ModelAnalysisService {

	@Autowired
	private ModularMapper modular;
	@Autowired
	private PersonMapper personMapper;
	
	@Override
	public Map<String,Object> getline(String title,String age,String exlevel,String datetype,String model,Integer type,String starttime,String endtime,String domain,Integer property) {
		Map<String,Object> map = new HashMap<String, Object>();
		map = this.getViews(title, age, exlevel, datetype, model,starttime,endtime,type,domain,property);
		return map;
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
	
	public Map<String,Object> getViews(String title,String age,String exlevel,String datetype,String model,String starttime,String endtime,Integer type,String domain,Integer property){
		List<String> reslistnames = new ArrayList<String>();
		List<String> reslistname = new ArrayList<String>();
		List<String> alldate = new ArrayList<String>();
		KylinJDBC  kylin = new KylinJDBC();
		List<Map<String, String>> list= new ArrayList<Map<String,String>>();
		List<String> listrearch= new ArrayList<String>();
		String titlestr="";
		String agestr="";
		String exlevelstr="";
		String modelstr="";
		String domainstr = "";
		String sqlpv="";
		String sqlvv="";
		String sqluv="";
		if(title!=null&&!"".equals(title)){
			titlestr=" and title in("+title+")";
		}
		if(exlevel!=null&&!"".equals(exlevel)){
			exlevelstr=" and education_level in("+exlevel+")";
		}
		if(domain!=null&&!"".equals(domain)){
			domainstr=" and reserch_domain ='"+domain+"' ";
		}
		if(model!=null&&!"".equals(model)){
			String str = "";
			String[] mo = model.split(",");
			for(String m : mo){
				str=str+"'"+m+"'"+",";
			}
			str = str.substring(0,str.length()-1);
			modelstr=" and mokuai in("+str+")";
		}
		
		if(age!=null&&!"".equals(age)){
			String[] ages = age.split(",");
			
			for(int i = 0 ;i<ages.length;i++){
				String[] a = ages[i].split("-");
				if(a.length==2){
					agestr=agestr+"  (age>="+a[0]+" and  age<="+a[1]+") or";
				}else{
					if(ages[i].equals("60")){
						agestr=agestr+"(age>=60) or";
					}else{
						agestr=agestr+"(age is null) or";
					}
				}
			}
			agestr = " and ("+agestr;
			agestr = agestr.substring(0,agestr.length()-2)+")";
		}
		
		String reserch_domain = "";
		
		if(datetype.equals("1")){
			Calendar   cal   =   Calendar.getInstance();
			cal.add(Calendar.DATE,   -1);
			String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
			
			switch (property) {
				case 0:
					if(type==1){
						sqlpv = "select MOKUAI as label,h,count(user_id) as numbers from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,h";
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						sqlvv="select MOKUAI  as label,h,count(user_id) as numbers from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,h";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						sqluv="select MOKUAI  as label,h,count(distinct user_id) as numbers from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,h";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 1:
					if(type==1){
						sqlpv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,h";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+exlevelstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,h";							
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqlvv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,h,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1 and  d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,h";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 2:
					if(type==1){
						sqlpv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end,h";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ " end,h";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end as label,h,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end,h";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 3:
					if(type==1){
						sqlpv="select case "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "when title=4 then '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "when title=4 then '其他' "
								+ "end,h";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "when title=4 then '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "when title=4 then '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "end,h";							
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select case "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "when title=4 then '其他' "
								+ "end as label,h,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "when title=4 then '其他' "
								+ "end,h";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 4:
					
					if(type==1){
						sqlpv="select reserch_domain as label,h,count(user_id) as numbers"
								+ " from kylin_analysis "
								+ " where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,h";
						
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select reserch_domain as label,h,count(user_id) as numbers "
								+ " from kylin_analysis "
								+ " where  is_online=1  and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,h";
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select reserch_domain as label,h,count(distinct user_id) as numbers "
								+ " from kylin_analysis "
								+ " where  is_online=1  and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,h";
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					
					break;
			}
			
			
			//listrearch = kylin.findToList(reserch_domain);
			if(list.size()>0){
				for(Map<String,String> li :list){
					reslistnames.add(li.get("LABEL"));
				}
				
				for (String name : reslistnames) {
					if (!reslistname.contains(name)) {
						reslistname.add(name);
					}
				}
				for(int i=0;i<24;i++){
					alldate.add(i+"");
				}
				
				
				List<Map<String,String>> obj = new ArrayList<Map<String,String>>();
				boolean rt = true;
				for (String name : reslistname) {
					for (String d : alldate) {
						for (Map<String, String> l : list) {
							if (l.get("LABEL").equals(name)
									&& l.get("H").equals(d)) {
								rt = false;
							}
						}
						if (rt) {
							Map<String,String> map = new HashMap<String, String>();
							map.put("LABEL", name);
							map.put("H", d);
							map.put("NUMBERS", "0");
							obj.add(map);
						}
						rt = true;
					}
				}
	
				list.addAll(obj);
			
			
			
			Collections.sort(list, new Comparator<Map<String,String>>() {
				@Override
				public int compare(Map<String,String> arg0, Map<String,String> arg1) {
					String hits0 = arg0.get("H");
					String hits1 = arg1.get("H");
					if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
						return 1;
					} else if (hits1.equals(hits0) ) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			
		}
	}else if(datetype.equals("2")||datetype.equals("3")){
		int num = 0;
		if(datetype.equals("2")){
			num = 6;
		}else if(datetype.equals("3")){
			num = 29;
		}
		String days = "";
		for(int k=num;k>=0;k--){
			Calendar   cal   =   Calendar.getInstance();
			cal.add(Calendar.DATE,   -k);
			String day = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
			alldate.add(day);
			days = days+"'"+day+"'"+",";
		}
		
		days=days+"'"+new SimpleDateFormat( "yyyy-MM-dd").format(new Date())+"'"+",";
		days=days.substring(0,days.length()-1);
		
		
		switch (property) {
			case 0:
				if(type==1){
					sqlpv = "select MOKUAI as label,d,count(user_id) as numbers from kylin_analysis where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,d";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqlpv);
				}else if(type==2){
					sqlvv="select MOKUAI  as label,d,count(user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,d";
					reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqlvv);
				}else if(type==3||type==5){
					sqluv="select MOKUAI  as label,d,count(distinct user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,d";
					reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqluv);
				}
				break;
			case 1:
				if(type==1){
					sqlpv="select case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end,d";							
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+") "+exlevelstr;
					list=kylin.findToListMap(sqlpv);
				}else if(type==2){
					
					sqlvv="select case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end,d";							
					reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqlvv);
				}else if(type==3||type==5){
					
					sqluv="select case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end as label,d,count(distinct user_id) as numbers "
							+ "from kylin_analysis "
							+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end,d";			
					
					reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqluv);
				}
				break;
			case 2:
				if(type==1){
					sqlpv="select case "
							+ "when age>=0 and age<=19 then '0-19岁' "
							+ "when age>=20 and age<=29 then '20-29岁' "
							+ "when age>=30 and age<=39 then '30-39岁' "
							+ "when age>=40 and age<=49 then '40-49岁' "
							+ "when age>=50 and age<=59 then '50-59岁' "
							+ "when age>=60 then '60岁以上' "
							+ "else '其他' "
							+ "end "
							+ "as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d  in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when age>=0 and age<=19 then '0-19岁' "
							+ "when age>=20 and age<=29 then '20-29岁' "
							+ "when age>=30 and age<=39 then '30-39岁' "
							+ "when age>=40 and age<=49 then '40-49岁' "
							+ "when age>=50 and age<=59 then '50-59岁' "
							+ "when age>=60 then '60岁以上' "
							+ "else '其他' "
							+ "end,d";							
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr;
					list=kylin.findToListMap(sqlpv);
				}else if(type==2){
					
					sqlvv="select case "
							+ "when age>=0 and age<=19 then '0-19岁' "
							+ "when age>=20 and age<=29 then '20-29岁' "
							+ "when age>=30 and age<=39 then '30-39岁' "
							+ "when age>=40 and age<=49 then '40-49岁' "
							+ "when age>=50 and age<=59 then '50-59岁' "
							+ "when age>=60 then '60岁以上' "
							+ "else '其他' "
							+ "end "
							+ "as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when age>=0 and age<=19 then '0-19岁' "
							+ "when age>=20 and age<=29 then '20-29岁' "
							+ "when age>=30 and age<=39 then '30-39岁' "
							+ "when age>=40 and age<=49 then '40-49岁' "
							+ "when age>=50 and age<=59 then '50-59岁' "
							+ "when age>=60 then '60岁以上' "
							+ " else '其他' "
							+ " end,d";
					reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqlvv);
				}else if(type==3||type==5){
					
					sqluv="select case "
							+ "when age>=0 and age<=19 then '0-19岁' "
							+ "when age>=20 and age<=29 then '20-29岁' "
							+ "when age>=30 and age<=39 then '30-39岁' "
							+ "when age>=40 and age<=49 then '40-49岁' "
							+ "when age>=50 and age<=59 then '50-59岁' "
							+ "when age>=60 then '60岁以上' "
							+ "else '其他' "
							+ "end as label,d,count(distinct user_id) as numbers "
							+ "from kylin_analysis "
							+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when age>=0 and age<=19 then '0-19岁' "
							+ "when age>=20 and age<=29 then '20-29岁' "
							+ "when age>=30 and age<=39 then '30-39岁' "
							+ "when age>=40 and age<=49 then '40-49岁' "
							+ "when age>=50 and age<=59 then '50-59岁' "
							+ "when age>=60 then '60岁以上' "
							+ "else '其他' "
							+ "end,d";			
					
					reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqluv);
				}
				break;
			case 3:
				if(type==1){
					sqlpv="select case "
							+ "when title=0 then '初级' "
							+ "when title=1 then '中级' "
							+ "when title=2 then '副高级' "
							+ "when title=3 then '正高级' "
							+ "when title=4 then '其他' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when title=0 then '初级' "
							+ "when title=1 then '中级' "
							+ "when title=2 then '副高级' "
							+ "when title=3 then '正高级' "
							+ "when title=4 then '其他' "
							+ "end,d";							
					reserch_domain = "select distinct reserch_domain from kylin_analysis where  d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr;
					list=kylin.findToListMap(sqlpv);
				}else if(type==2){
					
					sqlvv="select case "
							+ "when title=0 then '初级' "
							+ "when title=1 then '中级' "
							+ "when title=2 then '副高级' "
							+ "when title=3 then '正高级' "
							+ "when title=4 then '其他' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when title=0 then '初级' "
							+ "when title=1 then '中级' "
							+ "when title=2 then '副高级' "
							+ "when title=3 then '正高级' "
							+ "when title=4 then '其他' "
							+ "end,d";							
					reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqlvv);
				}else if(type==3||type==5){
					
					sqluv="select case "
							+ "when title=4 then '其他' "
							+ "when title=0 then '初级' "
							+ "when title=1 then '中级' "
							+ "when title=2 then '副高级' "
							+ "when title=3 then '正高级' "
							+ "end as label,d,count(distinct user_id) as numbers "
							+ "from kylin_analysis "
							+ "where  is_online=1  and  d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ "group by case "
							+ "when title=4 then '其他' "
							+ "when title=0 then '初级' "
							+ "when title=1 then '中级' "
							+ "when title=2 then '副高级' "
							+ "when title=3 then '正高级' "
							+ "end,d";			
					
					reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
					list=kylin.findToListMap(sqluv);
				}
				break;
			case 4:
				
				if(type==1){
					sqlpv="select reserch_domain as label,d,count(user_id) as numbers"
							+ " from kylin_analysis "
							+ " where d  in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ " group by reserch_domain,d";
					list=kylin.findToListMap(sqlpv);
				}else if(type==2){
					
					sqlvv="select reserch_domain as label,d,count(user_id) as numbers "
							+ " from kylin_analysis "
							+ " where  is_online=1  and d in ("+days+" ) "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ " group by reserch_domain,d";
					list=kylin.findToListMap(sqlvv);
				}else if(type==3||type==5){
					
					sqluv="select reserch_domain as label,d,count(distinct user_id) as numbers "
							+ " from kylin_analysis "
							+ " where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
							+ " group by reserch_domain,d";
					list=kylin.findToListMap(sqluv);
				}
				
				break;
		}
		
		
		//listrearch = kylin.findToList(reserch_domain);
		
		if(list.size()>0){
			for(Map<String,String> li :list){
				reslistnames.add(li.get("LABEL"));
			}
			
			for (String name : reslistnames) {
				if (!reslistname.contains(name)) {
					reslistname.add(name);
				}
			}
			
//			alldate.add(new SimpleDateFormat( "yyyy-MM-dd").format(new Date()));
			
			List<Map<String,String>> obj = new ArrayList<Map<String,String>>();
			boolean rt = true;
			for (String name : reslistname) {
				for (String d : alldate) {
					for (Map<String, String> l : list) {
						if (l.get("LABEL").equals(name)
								&& l.get("D").equals(d)) {
							rt = false;
						}
					}
					if (rt) {
						Map<String,String> map = new HashMap<String, String>();
						map.put("LABEL", name);
						map.put("D", d);
						map.put("NUMBERS", "0");
						obj.add(map);
					}
					rt = true;
				}
			}

			list.addAll(obj);
			
			Collections.sort(list, new Comparator<Map<String,String>>() {
				@Override
				public int compare(Map<String,String> arg0, Map<String,String> arg1) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date hits0 = new Date();
					Date hits1 = new Date();
					try {
						hits0=format.parse(arg0.get("D"));
						hits1 = format.parse(arg1.get("D"));
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
	}else{
			if(starttime.equals(endtime)){
				
				switch (property) {
				case 0:
					if(type==1){
						sqlpv = "select MOKUAI as label,h,count(user_id) as numbers from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,h";
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						sqlvv="select MOKUAI  as label,h,count(user_id) as numbers from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,h";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						sqluv="select MOKUAI  as label,h,count(distinct user_id) as numbers from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,h";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 1:
					if(type==1){
						sqlpv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,h";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+exlevelstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,h";							
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,h,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,h";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 2:
					if(type==1){
						sqlpv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ " end"
								+ "as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end,h";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end "
								+ "as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1 and  d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end,h";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end as label,h,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end,h";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 3:
					if(type==1){
						sqlpv="select case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end,h";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1 and  d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,h,count(user_id) as numbers "
								+ "end,h";							
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,h,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1 and  d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end,h";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 4:
					
					if(type==1){
						sqlpv="select reserch_domain as label,h,count(user_id) as numbers"
								+ " from kylin_analysis "
								+ " where d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,h";
						
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select reserch_domain as label,h,count(user_id) as numbers "
								+ " from kylin_analysis "
								+ " where  is_online=1  and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,h";
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select reserch_domain as label,h,count(distinct user_id) as numbers "
								+ " from kylin_analysis "
								+ " where  is_online=1  and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,h";
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					
					break;
			}
				
				//listrearch = kylin.findToList(reserch_domain);
				if(list.size()>0){
					for(Map<String,String> li :list){
						reslistnames.add(li.get("LABEL"));
					}
					
					for (String name : reslistnames) {
						if (!reslistname.contains(name)) {
							reslistname.add(name);
						}
					}
					for(int i=1;i<=24;i++){
						alldate.add(i+"");
					}
					
					
					List<Map<String,String>> obj = new ArrayList<Map<String,String>>();
					boolean rt = true;
					for (String name : reslistname) {
						for (String d : alldate) {
							for (Map<String, String> l : list) {
								if (l.get("LABEL").equals(name)
										&& l.get("H").equals(d)) {
									rt = false;
								}
							}
							if (rt) {
								Map<String,String> map = new HashMap<String, String>();
								map.put("LABEL", name);
								map.put("H", d);
								map.put("NUMBERS", "0");
								obj.add(map);
							}
							rt = true;
						}
					}
		
					list.addAll(obj);
				
				
				
				Collections.sort(list, new Comparator<Map<String,String>>() {
					@Override
					public int compare(Map<String,String> arg0, Map<String,String> arg1) {
						String hits0 = arg0.get("H");
						String hits1 = arg1.get("H");
						if (Integer.parseInt(hits1) < Integer.parseInt(hits0)) {
							return 1;
						} else if (hits1 .equals(hits0) ) {
							return 0;
						} else {
							return -1;
						}
					}
				});
			}
		}else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date sd = format.parse(starttime);
				Date ed = format.parse(endtime);
				List<Date> date = this.getDate(sd, ed);
				String days  = "";
				for(Date d : date){
					alldate.add(format.format(d));
					days = days+"'"+format.format(d)+"'"+",";
				}
				days=days.substring(0,days.length()-1);
				
				switch (property) {
				case 0:
					if(type==1){
						sqlpv = "select MOKUAI as label,d,count(user_id) as numbers from kylin_analysis where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,d";
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						sqlvv="select MOKUAI  as label,d,count(user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,d";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						sqluv="select MOKUAI  as label,d,count(distinct user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI,d";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 1:
					if(type==1){
						sqlpv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,d,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,d";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+") "+exlevelstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,d,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,d";							
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end as label,d,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when education_level=0 then '大专' "
								+ "when education_level=1 then '本科' "
								+ "when education_level=2 then '硕士' "
								+ "when education_level=3 then '博士' "
								+ "when education_level=4 then '其他' "
								+ "end,d";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 2:
					if(type==1){
						sqlpv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ " end "
								+ "as label,d,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d  in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end,d";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end "
								+ "as label,d,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ " end,d";
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end as label,d,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' "
								+ "end,d";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 3:
					if(type==1){
						sqlpv="select case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,d,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end,d";							
						reserch_domain = "select distinct reserch_domain from kylin_analysis where  d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr;
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,d,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,h,count(user_id) as numbers "
								+ "end,d";							
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,d,count(distinct user_id) as numbers "
								+ "from kylin_analysis "
								+ "where  is_online=1   and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ "group by case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end,d";			
						
						reserch_domain="select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr;
						list=kylin.findToListMap(sqluv);
					}
					break;
				case 4:
					
					if(type==1){
						sqlpv="select reserch_domain as label,d,count(user_id) as numbers"
								+ " from kylin_analysis "
								+ " where d  in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,d";
						list=kylin.findToListMap(sqlpv);
					}else if(type==2){
						
						sqlvv="select reserch_domain as label,d,count(user_id) as numbers "
								+ " from kylin_analysis "
								+ " where  is_online=1  and d in ("+days+" ) "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,h";
						list=kylin.findToListMap(sqlvv);
					}else if(type==3||type==5){
						
						sqluv="select reserch_domain as label,d,count(distinct user_id) as numbers "
								+ " from kylin_analysis "
								+ " where  is_online=1  and d in ("+days+") "+titlestr+exlevelstr+agestr+modelstr+domainstr
								+ " group by reserch_domain,d";
						list=kylin.findToListMap(sqluv);
					}
					
					break;
			}
				
				
				//listrearch = kylin.findToList(reserch_domain);

				if(list.size()>0){
					for(Map<String,String> li :list){
						reslistnames.add(li.get("LABEL"));
					}
					
					for (String name : reslistnames) {
						if (!reslistname.contains(name)) {
							reslistname.add(name);
						}
					}
					
					alldate.add(new SimpleDateFormat( "yyyy-MM-dd").format(new Date()));
					
					List<Map<String,String>> obj = new ArrayList<Map<String,String>>();
					boolean rt = true;
					for (String name : reslistname) {
						for (String d : alldate) {
							for (Map<String, String> l : list) {
								if (l.get("LABEL").equals(name)
										&& l.get("D").equals(d)) {
									rt = false;
								}
							}
							if (rt) {
								Map<String,String> map = new HashMap<String, String>();
								map.put("LABEL", name);
								map.put("D", d);
								map.put("NUMBERS", "0");
								obj.add(map);
							}
							rt = true;
						}
					}

					list.addAll(obj);
					
					Collections.sort(list, new Comparator<Map<String,String>>() {
						@Override
						public int compare(Map<String,String> arg0, Map<String,String> arg1) {
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							Date hits0 = new Date();
							Date hits1 = new Date();
							try {
								hits0=format.parse(arg0.get("D"));
								hits1 = format.parse(arg1.get("D"));
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
	}
		
		
		Map<String, List<String>> m = new HashMap<String, List<String>>();
		for (Map<String,String> l : list) {
			String typename = l.get("LABEL");
			if (m.get(typename) != null) {
				m.get(typename).add(l.get("NUMBERS"));
			} else {
				List<String> num = new ArrayList<String>();
				num.add(l.get("NUMBERS"));
				m.put(typename, num);
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", m);
		map.put("title", reslistname);
		map.put("date", alldate);
		map.put("reserch_domain", listrearch);
		return map;
	}
	
	
	@Override
	public PageList gettable(Integer pagesize,Integer pagenum,String title,String age,String exlevel,String datetype,String model,String starttime,String endtime,Integer type,String domain,Integer property){
		List<String> alldate = new ArrayList<String>();
		KylinJDBC  kylin = new KylinJDBC();
		List<Map<String, String>> listpv= new ArrayList<Map<String,String>>();
		List<Map<String, String>> listvv= new ArrayList<Map<String,String>>();
		List<Map<String, String>> listuv= new ArrayList<Map<String,String>>();
		String titlestr="";
		String agestr="";
		String exlevelstr="";
		String modelstr="";
		String domainstr = "";
		String sqlpv="";
		String sqlvv="";
		String sqluv="";
		String days = "";
		
		Integer COUNT_USER=personMapper.countUser();
		
		if(title!=null&&!"".equals(title)){
			titlestr=" and title in("+title+")";
		}
		if(exlevel!=null&&!"".equals(exlevel)){
			exlevelstr=" and education_level in("+exlevel+")";
		}
		if(domain!=null&&!"".equals(domain)){
			domainstr=" and reserch_domain ='"+domain+"' ";
		}
		if(model!=null&&!"".equals(model)){
			String str = "";
			String[] mo = model.split(",");
			for(String m : mo){
				str=str+"'"+m+"'"+",";
			}
			str = str.substring(0,str.length()-1);
			modelstr=" and mokuai in("+str+")";
		}
		
		if(age!=null&&!"".equals(age)){
			String[] ages = age.split(",");
			
			for(int i = 0 ;i<ages.length;i++){
				String[] a = ages[i].split("-");
				if(a.length==2){
					agestr=agestr+"  (age>="+a[0]+" and  age<="+a[1]+") or";
				}else{
					if(ages[i].equals("60")){
						agestr=agestr+"(age>=60) or";
					}else{
						agestr=agestr+"(age is null) or";
					}
				}
			}
			agestr = " and ("+agestr;
			agestr = agestr.substring(0,agestr.length()-2)+")";
		}
		
		
		if(datetype.equals("1")){
			Calendar   cal   =   Calendar.getInstance();
			cal.add(Calendar.DATE,   -1);
			days= "'"+new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime())+"'";
			
		}else if(datetype.equals("2")||datetype.equals("3")){
				
			int num = 0;
			
			if(datetype.equals("2")){
				num = 6;
			}else if(datetype.equals("3")){
				num = 29;
			}
			
			for(int k=num;k>0;k--){
			Calendar   cal   =   Calendar.getInstance();
			cal.add(Calendar.DATE,   -k);
			String day = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
			alldate.add(day);
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
						alldate.add(format.format(d));
						days = days+"'"+format.format(d)+"'"+",";
					}
					days=days.substring(0,days.length()-1);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		switch(property){
		case 0:
			sqlpv = "select MOKUAI as label,count(user_id) as numbers from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI";
			sqlvv = "select MOKUAI as label,count(user_id) as numbers from kylin_analysis where is_online=1 and d in("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI";
			sqluv = "select MOKUAI as label,count(distinct user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr+" group by MOKUAI";
			break;
		case 1:
			sqlpv="select case "
					+ "when education_level=0 then '大专' "
					+ "when education_level=1 then '本科' "
					+ "when education_level=2 then '硕士' "
					+ "when education_level=3 then '博士' "
					+ "when education_level=4 then '其他' "
					+ "end as label,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ "where d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when education_level=0 then '大专' "
					+ "when education_level=1 then '本科' "
					+ "when education_level=2 then '硕士' "
					+ "when education_level=3 then '博士' "
					+ "when education_level=4 then '其他' "
					+ "end";
			sqlvv="select case "
					+ "when education_level=0 then '大专' "
					+ "when education_level=1 then '本科' "
					+ "when education_level=2 then '硕士' "
					+ "when education_level=3 then '博士' "
					+ "when education_level=4 then '其他' "
					+ "end as label,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ "where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when education_level=0 then '大专' "
					+ "when education_level=1 then '本科' "
					+ "when education_level=2 then '硕士' "
					+ "when education_level=3 then '博士' "
					+ "when education_level=4 then '其他' "
					+ "end";
			sqluv="select case "
					+ "when education_level=0 then '大专' "
					+ "when education_level=1 then '本科' "
					+ "when education_level=2 then '硕士' "
					+ "when education_level=3 then '博士' "
					+ "when education_level=4 then '其他' "
					+ "end as label,count(distinct user_id) as numbers "
					+ "from kylin_analysis "
					+ "where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when education_level=0 then '大专' "
					+ "when education_level=1 then '本科' "
					+ "when education_level=2 then '硕士' "
					+ "when education_level=3 then '博士' "
					+ "when education_level=4 then '其他' "
					+ "end";
			break;
		case 2:
			sqlpv="select case "
					+ "when age>=0 and age<=19 then '0-19岁' "
					+ "when age>=20 and age<=29 then '20-29岁' "
					+ "when age>=30 and age<=39 then '30-39岁' "
					+ "when age>=40 and age<=49 then '40-49岁' "
					+ "when age>=50 and age<=59 then '50-59岁' "
					+ "when age>=60 then '60岁以上' "
					+ "else '其他' "
					+ "end as label,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ "where d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when age>=0 and age<=19 then '0-19岁' "
					+ "when age>=20 and age<=29 then '20-29岁' "
					+ "when age>=30 and age<=39 then '30-39岁' "
					+ "when age>=40 and age<=49 then '40-49岁' "
					+ "when age>=50 and age<=59 then '50-59岁' "
					+ "when age>=60 then '60岁以上' "
					+ "else '其他' "
					+ "end";
			sqlvv="select case "
					+ "when age>=0 and age<=19 then '0-19岁' "
					+ "when age>=20 and age<=29 then '20-29岁' "
					+ "when age>=30 and age<=39 then '30-39岁' "
					+ "when age>=40 and age<=49 then '40-49岁' "
					+ "when age>=50 and age<=59 then '50-59岁' "
					+ "when age>=60 then '60岁以上' "
					+ "else '其他' "
					+ "end as label,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ "where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when age>=0 and age<=19 then '0-19岁' "
					+ "when age>=20 and age<=29 then '20-29岁' "
					+ "when age>=30 and age<=39 then '30-39岁' "
					+ "when age>=40 and age<=49 then '40-49岁' "
					+ "when age>=50 and age<=59 then '50-59岁' "
					+ "when age>=60 then '60岁以上' "
					+ "else '其他' "
					+ "end";
			sqluv="select case "
					+ "when age>=0 and age<=19 then '0-19岁' "
					+ "when age>=20 and age<=29 then '20-29岁' "
					+ "when age>=30 and age<=39 then '30-39岁' "
					+ "when age>=40 and age<=49 then '40-49岁' "
					+ "when age>=50 and age<=59 then '50-59岁' "
					+ "when age>=60 then '60岁以上' "
					+ "else '其他' "
					+ "end as label,count(distinct user_id) as numbers "
					+ "from kylin_analysis "
					+ "where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when age>=0 and age<=19 then '0-19岁' "
					+ "when age>=20 and age<=29 then '20-29岁' "
					+ "when age>=30 and age<=39 then '30-39岁' "
					+ "when age>=40 and age<=49 then '40-49岁' "
					+ "when age>=50 and age<=59 then '50-59岁' "
					+ "when age>=60 then '60岁以上' "
					+ "else '其他' "
					+ "end";
			break;
		case 3:
			sqlpv="select case "
					+ "when title=4 then '其他' "
					+ "when title=0 then '初级' "
					+ "when title=1 then '中级' "
					+ "when title=2 then '副高级' "
					+ "when title=3 then '正高级' "
					+ "end as label,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ "where d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when title=4 then '其他' "
					+ "when title=0 then '初级' "
					+ "when title=1 then '中级' "
					+ "when title=2 then '副高级' "
					+ "when title=3 then '正高级' "
					+ "end";
			sqlvv="select case "
					+ "when title=4 then '其他' "
					+ "when title=0 then '初级' "
					+ "when title=1 then '中级' "
					+ "when title=2 then '副高级' "
					+ "when title=3 then '正高级' "
					+ "end as label,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ "where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when title=4 then '其他' "
					+ "when title=0 then '初级' "
					+ "when title=1 then '中级' "
					+ "when title=2 then '副高级' "
					+ "when title=3 then '正高级' "
					+ "end";
			sqluv="select case "
					+ "when title=4 then '其他' "
					+ "when title=0 then '初级' "
					+ "when title=1 then '中级' "
					+ "when title=2 then '副高级' "
					+ "when title=3 then '正高级' "
					+ "end as label,count(distinct user_id) as numbers "
					+ "from kylin_analysis "
					+ "where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by case "
					+ "when title=4 then '其他' "
					+ "when title=0 then '初级' "
					+ "when title=1 then '中级' "
					+ "when title=2 then '副高级' "
					+ "when title=3 then '正高级' "
					+ "end";
			break;
		case 4:
			sqlpv="select reserch_domain as label,count(user_id) as numbers "
					+ "from kylin_analysis " 
					+ "where d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ "group by reserch_domain";
			
			sqlvv="select reserch_domain as label,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ " where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ " group by reserch_domain";
			sqluv="select reserch_domain as label,count(distinct user_id) as numbers "
					+ " from kylin_analysis "
					+ " where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+modelstr+domainstr
					+ " group by reserch_domain";
			break;
		}
		
		
		listuv=kylin.findToListMap(sqluv);
		listpv = kylin.findToListMap(sqlpv);
		listvv = kylin.findToListMap(sqlvv);
		
		List<String> modelname =new ArrayList<String>();
		List<Object> listmodel = new ArrayList<Object>();
		for(Map<String,String> pvname : listpv){
			if(StringUtils.isNotBlank(pvname.get("LABEL"))){
				String name = pvname.get("LABEL");
				if(!modelname.contains(name)){
					modelname.add(name);
				}
			}
		}
		
		for(Map<String,String>vvname : listvv){
			if(StringUtils.isNotBlank(vvname.get("LABEL"))){
				String name = vvname.get("LABEL");
				if(!modelname.contains(name)){
					modelname.add(name);
				}
			}
		}
		
		for(Map<String,String>uvname : listuv){
			if(StringUtils.isNotBlank(uvname.get("LABEL"))){
				String name = uvname.get("LABEL");
				if(!modelname.contains(name)){
					modelname.add(name);
				}
			}
		}
		
		for(String name : modelname){
			ModelTable mt = new ModelTable();
			mt.setModelname(name);
			boolean pvrt  = false;
			for(Map<String,String> pv : listpv){
				if(StringUtils.isNoneBlank(pv.get("LABEL"))){
					if(name.contains(pv.get("LABEL"))){
						pvrt = true;
					}
					if(pvrt){
						mt.setModelPV(pv.get("NUMBERS"));
					}
				}
				pvrt = false;
			}
			
			if(mt.getModelPV()==null||"".equals(mt.getModelPV())){
				mt.setModelPV("0");
			}
			
			boolean vvrt = false;
			for(Map<String,String> vv : listvv){
				if(StringUtils.isNoneBlank(vv.get("LABEL"))){
					if(name.contains(vv.get("LABEL"))){
						vvrt = true;
					}
					if(vvrt){
						mt.setModelVV(vv.get("NUMBERS"));
					}
				}
				vvrt = false;
			}
			
			if(mt.getModelVV()==null||"".equals(mt.getModelVV())){
				mt.setModelVV("0");
			}
			
			boolean uvrt = false;
			for(Map<String,String> uv : listuv){
				if(StringUtils.isNoneBlank(uv.get("LABEL"))){
					if(name.contains(uv.get("LABEL"))){
						uvrt = true;
					}
					if(uvrt){
						mt.setModelUV(uv.get("NUMBERS"));
						mt.setModelAU(uv.get("NUMBERS"));
						mt.setModeAUP(Integer.valueOf(uv.get("NUMBERS"))/COUNT_USER*100+"%");
					}
				}
				
				uvrt = false;
			}
			
			if(mt.getModelAU()==null||"".equals(mt.getModelAU())){
				mt.setModelUV("0");
				mt.setModelAU("0");
				mt.setModeAUP("0%");
			}
			
			listmodel.add(mt);
		}
		
		int s = (pagenum-1)*pagesize;
		int n = pagenum*pagesize-1;
		
		List<Object> pageobject = new ArrayList<Object>();
		
		if(n<=listmodel.size()){
			for (int i = s;i<=n;i++){
				pageobject.add(listmodel.get(i));
			}
		}else{
			for (int i = s;i<listmodel.size();i++){
				pageobject.add(listmodel.get(i));
			}
		}
		
		
		PageList pl = new PageList();
			pl.setPageNum(pagenum);
			pl.setPageSize(pagesize);
			pl.setPageTotal(listmodel.size());
			pl.setPageRow(listmodel);
		
		return pl;
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

}
