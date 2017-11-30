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
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.KylinJDBC;
import com.wf.bean.PageList;
import com.wf.bean.ResourceTableBean;
import com.wf.dao.FunctionPageDailyMapper;
import com.wf.dao.FunctionPageMapper;
import com.wf.service.FunctionProfileService;


@Service
public class FunctionProfileServiceImpl implements FunctionProfileService {

	@Autowired
	private FunctionPageMapper functionPageMapper;
	@Autowired
	private FunctionPageDailyMapper functionPageDailyMapper;

	
	@Override
	public Map<String, Object> getline(String title, String age,
			String exlevel, String datetype, String type,
			String starttime, String endtime, String domain, Integer property) {
		
		String agestr =this.getAges(age);
		String days = this.getDayByDateType(datetype,starttime,endtime);
		
		Map<String , Object> map=new HashMap<String, Object>();
		map.put("days", days);
		map.put("title", title);
		map.put("education_level", exlevel);
		map.put("age", agestr);
		map.put("topic",domain);
		map.put("property",property);
		map.put("result",type);
		
		List<Object> list=functionPageDailyMapper.functionProfile_view(map);
		Map<String, Object> datamap = new HashMap<String, Object>();
		
		List<String> reslistnames=new ArrayList<>();
		List<String> reslistname=new ArrayList<>();
		List<String> alldate = new ArrayList<String>();
		
		if(list.size()>0){

			for(Object li :list){
				if(null!=((Map<String, Object>)li).get("classify")){
					reslistnames.add(((Map<String, Object>)li).get("classify").toString());
				}
			}
			
			for (String name : reslistnames) {
				if (!reslistname.contains(name)) {
					reslistname.add(name);
				}
			}
			
			if(datetype.equals("1")){
				Calendar   cal   =   Calendar.getInstance();
				cal.add(Calendar.DATE,   -1);
				alldate.add(new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime()));
			}else if(datetype.equals("2")||datetype.equals("3")){
				int num =datetype.equals("2")?6:29;
				for(int k=num;k>0;k--){
					Calendar   cal   =   Calendar.getInstance();
					cal.add(Calendar.DATE,   -k);
					String day = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
					alldate.add(day);
				}
			}else{
				
				if(starttime.equals(endtime)){
					alldate.add(endtime);
				}else {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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

			
			List<Map<String,String>> obj = new ArrayList<Map<String,String>>();
			boolean rt = true;
			for (String name : reslistname) {
				for (String d : alldate) {
					for (Object l : list) {
						if(null==((Map<String, Object>)l).get("classify"))
							continue;
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
			Map<String, List<String>> m = new HashMap<String, List<String>>();
			for (Object l : list) {
				if(null==((Map<String,Object>)l).get("classify"))
					continue;
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

	public Map<String,Object> getViews(String title, String age,
			String exlevel, String datetype, Integer type,
			String starttime, String endtime, String domain, Integer property){
		return null;
	}
	
	@Override
	public PageList gettable(Integer pagesize,Integer pagenum,String title, String age,
			String exlevel, String datetype, Integer type, String starttime,
			String endtime, String domain, Integer property) {
		
		PageList pageList=new PageList();
		List<Object> list=new ArrayList<>();
		int count=0;
		
		String agestr =this.getAges(age);
		String days = this.getDayByDateType(datetype,starttime,endtime);
		
		Map<String , Object> map=new HashMap<String, Object>();
		map.put("pageNum", (pagenum-1)*pagesize);
		map.put("pageSize",pagesize);
		map.put("days", days);
		map.put("title", title);
		map.put("education_level", exlevel);
		map.put("age", agestr);
		map.put("topic",domain);
		map.put("property",property);
		
		if(datetype.equals("1") ||
				(starttime.equals(endtime)
					&&StringUtils.isNotBlank(starttime)
					&& StringUtils.isNotBlank(endtime))){
			list=functionPageMapper.functionProfile_table(map);
			count=functionPageMapper.functionProfile_count(map).size();
		}else{
			list=functionPageDailyMapper.functionProfile_table(map);
			count=functionPageDailyMapper.functionProfile_count(map).size();
		}
		
		pageList.setPageNum(pagenum);
		pageList.setPageSize(pagesize);
		pageList.setPageRow(list);
		pageList.setTotalRow(count);
		
		return pageList;
		
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

	public Map<String, Object> indexanalysis(String title, String age,
			String exlevel, String datetype, String type,
			String starttime, String endtime, String domain, Integer property){

		List<String> reslistnames = new ArrayList<String>();
		List<String> reslistname = new ArrayList<String>();
		List<String> alldate = new ArrayList<String>();
		KylinJDBC  kylin = new KylinJDBC();
		List<Map<String, String>> list= new ArrayList<Map<String,String>>();
		List<String> listrearch= new ArrayList<String>();
		String titlestr="";
		String agestr="";
		String exlevelstr="";
		String domainstr = "";
		String sql="";
		String domainsql="";
		
		if(title!=null&&!"".equals(title)){
			titlestr=" and title in("+title+")";
		}
		if(exlevel!=null&&!"".equals(exlevel)){
			exlevelstr=" and education_level in("+exlevel+")";
		}
		if(domain!=null&&!"".equals(domain)){
			domainstr=" and reserch_domain =" +"'"+domain+"' ";
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
			String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
			switch(property){
				case 0:
					if(type.contains("0")){
						sql="select case "
								+ "when url_type=1 then '浏览数' "
								+ "when url_type=2 then '下载数' "
								+ "when url_type=3 then '检索数' "
								+ "when url_type=4 then '分享数' "
								+ "when url_type=5 then '收藏数' "
								+ "when url_type=6 then '导出数' "
								+ "when url_type=7 then '笔记数' "
								+ "when url_type=8 then '订阅数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=1 then '浏览数' "
								+ "when url_type=2 then '下载数' "
								+ "when url_type=3 then '检索数' "
								+ "when url_type=4 then '分享数' "
								+ "when url_type=5 then '收藏数' "
								+ "when url_type=6 then '导出数' "
								+ "when url_type=7 then '笔记数' "
								+ "when url_type=8 then '订阅数' "
								+ "end,h";
						
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr;
					}else if(type.contains("1")){
						sql="select case "
								+ "when url_type=1 then '浏览数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=1 then '浏览数' "
								+ "end,h";
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+" and url_type=1";
					}else if(type.contains("2")){
						sql="select case "
								+ "when url_type=2 then '下载数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=2 then '下载数' "
								+ "end,h";
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+" and url_type=2";
					}else if(type.contains("3")){
						sql="select case "
								+ "when url_type=3 then '检索数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=3 then '检索数' "
								+ "end,h";
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+" and url_type=3";
					}else if(type.contains("4")){
						sql="select case "
								+ "when url_type=4 then '分享数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=4 then '分享数' "
								+ "end,h";
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+" and url_type=4";
					}else if(type.contains("5")){
						sql="select case "
								+ "when url_type=5 then '收藏数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=5 then '收藏数' "
								+ "end,h";
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+" and url_type=5";
					}else if(type.contains("6")){
						sql="select case "
								+ "when url_type=6 then '导出数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=6 then '导出数' "
								+ "end,h";
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+" and url_type=6";
					}else if(type.contains("7")){
						sql="select case "
								+ "when url_type=7 then '笔记数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=7 then '笔记数' "
								+ "end,h";
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+" and url_type=7";
					}else if(type.contains("8")){
						sql="select case "
								+ "when url_type=8 then '订阅数' "
								+ "end as label,h,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+domainstr
								+ " group by case "
								+ "when url_type=8 then '订阅数' "
								+ "end,h";
						domainsql ="select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+" and url_type=8";
					}
					
				break;
				case 1:sql ="select case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end as label,h,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where url_type in ("+type+" ) and d='"+yesterday+"' "+exlevelstr
							+ "group by case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end,h";
				
				domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+" ) and d='"+yesterday+"' "+exlevelstr;
					break;
				case 2:sql="select case "
						+ "when age>=0 and age<=19 then '0-19岁' "
						+ "when age>=20 and age<=29 then '20-29岁' "
						+ "when age>=30 and age<=39 then '30-39岁' "
						+ "when age>=40 and age<=49 then '40-49岁' "
						+ "when age>=50 and age<=59 then '50-59岁' "
						+ "when age>=50  then '60岁以上' "
						+ "else '其他 'end "
						+ "as label,h,count(user_id) as numbers "
						+ "from kylin_analysis "
						+ "where url_type in ( "+type+" ) and d='"+yesterday+"' "+agestr
						+ "group by case "
						+ "when age>=0 and age<=19 then '0-19岁' "
						+ "when age>=20 and age<=29 then '20-29岁' "
						+ "when age>=30 and age<=39 then '30-39岁' "
						+ "when age>=40 and age<=49 then '40-49岁' "
						+ "when age>=50 and age<=59 then '50-59岁' "
						+ "when age>=60 then '60岁以上' "
						+ "else '其他'end,h";
				domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+") and d='"+yesterday+"' "+agestr;
					break;
				case 3:sql ="select case "
						+ "when title=4 then '其他' "
						+ "when title=0 then '初级' "
						+ "when title=1 then '中级' "
						+ "when title=2 then '副高级' "
						+ "when title=3 then '正高级' "
						+ "end as label,h,count(user_id) as numbers "
						+ "from kylin_analysis "
						+ "where url_type in ("+type+")  and d='"+yesterday+"' "+titlestr
						+ "group by case "
						+ "when title=4 then '其他' "
						+ "when title=0 then '初级' "
						+ "when title=1 then '中级' "
						+ "when title=2 then '副高级' "
						+ "when title=3 then '正高级' "
						+ "end,h";
				domainsql="select distinct reserch_domain from kylin_analysis where url_type ("+type+" ) and d='"+yesterday+"' "+titlestr;
						break;
				case 4:
					sql ="select reserch_domain as label,h,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where url_type in ("+type+")   and d='"+yesterday+"' "+domainstr
							+ "group by reserch_domain,url_type,h";
						break;
			}
			//listrearch = kylin.findToList(domainsql);
			list = kylin.findToListMap(sql);
			if(list.size()>0){
				if(list.size()>0){
					for(Map<String,String> li :list){
						if(StringUtils.isNotBlank(li.get("LABEL"))){
							reslistnames.add(li.get("LABEL"));
						}
					}

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
							if(StringUtils.isNotBlank(l.get("LABEL")) && StringUtils.isNotBlank(l.get("H"))){
								if (l.get("LABEL").equals(name)
										&& l.get("H").equals(d)) {
									rt = false;
								}
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
			
			switch(property){
			case 0:
				if(type.contains("0")){
					sql="select case "
							+ "when url_type=1 then '浏览数' "
							+ "when url_type=2 then '下载数' "
							+ "when url_type=3 then '检索数' "
							+ "when url_type=4 then '分享数' "
							+ "when url_type=5 then '收藏数' "
							+ "when url_type=6 then '导出数' "
							+ "when url_type=7 then '笔记数' "
							+ "when url_type=8 then '订阅数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where  d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=1 then '浏览数' "
							+ "when url_type=2 then '下载数' "
							+ "when url_type=3 then '检索数' "
							+ "when url_type=4 then '分享数' "
							+ "when url_type=5 then '收藏数' "
							+ "when url_type=6 then '导出数' "
							+ "when url_type=7 then '笔记数' "
							+ "when url_type=8 then '订阅数' "
							+ "end,d";
					
					domainsql="select distinct reserch_domain from kylin_analysis where  d in("+days+") "+titlestr+exlevelstr+agestr;
				}else if(type.contains("1")){
					sql="select case "
							+ "when url_type=1 then '浏览数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=1 then '浏览数' "
							+ "end,d";
					domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=1";
				}else if(type.contains("2")){
					sql="select case "
							+ "when url_type=2 then '下载数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=2 then '下载数' "
							+ "end,d";
					domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=2";
				}else if(type.contains("3")){
					sql="select case "
							+ "when url_type=3 then '检索数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in ("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=3 then '检索数' "
							+ "end,d";
					domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=3";
				}else if(type.contains("4")){
					sql="select case "
							+ "when url_type=4 then '分享数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=4 then '分享数' "
							+ "end,d";
					domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=4";
				}else if(type.contains("5")){
					sql="select case "
							+ "when url_type=5 then '收藏数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=5 then '收藏数' "
							+ "end,d";
					domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=5";
				}else if(type.contains("6")){
					sql="select case "
							+ "when url_type=6 then '导出数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=6 then '导出数' "
							+ "end,d";
					domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=6";
				}else if(type.contains("7")){
					sql="select case "
							+ "when url_type=7 then '笔记数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=7 then '笔记数' "
							+ "end,d";
					domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=7";
				}else if(type.contains("8")){
					sql="select case "
							+ "when url_type=8 then '订阅数' "
							+ "end as label,d,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
							+ " group by case "
							+ "when url_type=8 then '订阅数' "
							+ "end,d";
					domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=8";
				}
				
			break;
			case 1:sql ="select case "
						+ "when education_level=0 then '大专' "
						+ "when education_level=1 then '本科' "
						+ "when education_level=2 then '硕士' "
						+ "when education_level=3 then '博士' "
						+ "when education_level=4 then '其他' "
						+ "end as label,d,count(user_id) as numbers "
						+ "from kylin_analysis "
						+ "where url_type in ("+type+" ) and  d in("+days+") "+exlevelstr
						+ "group by case "
						+ "when education_level=0 then '大专' "
						+ "when education_level=1 then '本科' "
						+ "when education_level=2 then '硕士' "
						+ "when education_level=3 then '博士' "
						+ "when education_level=4 then '其他' "
						+ "end,d";
			domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+" ) and  d in("+days+") "+exlevelstr;
				break;
			case 2:sql="select case "
					+ "when age>=0 and age<=19 then '0-19岁' "
					+ "when age>=20 and age<=29 then '20-29岁' "
					+ "when age>=30 and age<=39 then '30-39岁' "
					+ "when age>=40 and age<=49 then '40-49岁' "
					+ "when age>=50 and age<=59 then '50-59岁' "
					+ "when age>=60 then '60岁以上' "
					+ "else '其他'end "
					+ "as label,d,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ "where url_type  in ("+type+") and  d in("+days+") "+agestr
					+ "group by case "
					+ "when age>=0 and age<=19 then '0-19岁' "
					+ "when age>=20 and age<=29 then '20-29岁' "
					+ "when age>=30 and age<=39 then '30-39岁' "
					+ "when age>=40 and age<=49 then '40-49岁' "
					+ "when age>=50 and age<=59 then '50-59岁' "
					+ "when age>=60 then '60岁以上' "
					+ "else '其他' end,d";
			
			domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+") and  d in("+days+") "+agestr;
				break;
			case 3:sql ="select case "
					+ "when title=4 then '其他' "
					+ "when title=0 then '初级' "
					+ "when title=1 then '中级' "
					+ "when title=2 then '副高级' "
					+ "when title=3 then '正高级' "
					+ "end as label,d,count(user_id) as numbers "
					+ "from kylin_analysis "
					+ "where url_type in ("+type+") and  d in("+days+") "+titlestr
					+ "group by case "
					+ "when title=4 then '其他' "
					+ "when title=0 then '初级' "
					+ "when title=1 then '中级' "
					+ "when title=2 then '副高级' "
					+ "when title=3 then '正高级' "
					+ "end,d";
			
			domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+") and  d in("+days+") "+titlestr;
					break;
			case 4:
				sql ="select reserch_domain as label,d,count(user_id) as numbers "
						+ "from kylin_analysis "
						+ "where url_type in ("+type+")   and d in ("+days+") "+domainstr
						+ "group by reserch_domain,url_type,d";
					break;
		}
		list = kylin.findToListMap(sql);
		//listrearch = kylin.findToList(domainsql);
		if(list.size()>0){
			for(Map<String,String> li :list){
				if(StringUtils.isNotBlank(li.get("LABEL"))){
					reslistnames.add(li.get("LABEL"));
				}
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
						if (null!=l.get("LABEL") && l.get("LABEL").equals(name)
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
		}else {
			if(starttime.equals(endtime)){
				switch(property){
					case 0:
						if(type.contains("0")){
							sql="select case "
									+ "when url_type=1 then '浏览数' "
									+ "when url_type=2 then '下载数' "
									+ "when url_type=3 then '检索数' "
									+ "when url_type=4 then '分享数' "
									+ "when url_type=5 then '收藏数' "
									+ "when url_type=6 then '导出数' "
									+ "when url_type=7 then '笔记数' "
									+ "when url_type=8 then '订阅数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=1 then '浏览数' "
									+ "when url_type=2 then '下载数' "
									+ "when url_type=3 then '检索数' "
									+ "when url_type=4 then '分享数' "
									+ "when url_type=5 then '收藏数' "
									+ "when url_type=6 then '导出数' "
									+ "when url_type=7 then '笔记数' "
									+ "when url_type=8 then '订阅数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr;
						}else if(type.contains("1")){
							sql="select case "
									+ "when url_type=1 then '浏览数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=1 then '浏览数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+" and url_type=1";
						}else if(type.contains("2")){
							sql="select case "
									+ "when url_type=2 then '下载数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=2 then '下载数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+" and url_type=2";
						}else if(type.contains("3")){
							sql="select case "
									+ "when url_type=3 then '检索数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=3 then '检索数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+" and url_type=3";
						}else if(type.contains("4")){
							sql="select case "
									+ "when url_type=4 then '分享数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=4 then '分享数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+" and url_type=4";
						}else if(type.contains("5")){
							sql="select case "
									+ "when url_type=5 then '收藏数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=5 then '收藏数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+" and url_type=5";
						}else if(type.contains("6")){
							sql="select case "
									+ "when url_type=6 then '导出数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=6 then '导出数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+" and url_type=6";
						}else if(type.contains("7")){
							sql="select case "
									+ "when url_type=7 then '笔记数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=7 then '笔记数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+" and url_type=7";
						}else if(type.contains("8")){
							sql="select case "
									+ "when url_type=8 then '订阅数' "
									+ "end as label,h,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where d='"+endtime+"'"+titlestr+exlevelstr+agestr+domainstr
									+ " group by case "
									+ "when url_type=8 then '订阅数' "
									+ "end,h;";
							domainsql="select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+" and url_type=8";
						}
				break;
				case 1:sql ="select case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end as label,h,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where url_type in ("+type+" ) and d='"+endtime+"' "+exlevelstr
							+ "group by case "
							+ "when education_level=0 then '大专' "
							+ "when education_level=1 then '本科' "
							+ "when education_level=2 then '硕士' "
							+ "when education_level=3 then '博士' "
							+ "when education_level=4 then '其他' "
							+ "end,h";
				domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+" ) and d='"+endtime+"' "+exlevelstr;
					break;
				case 2:sql="select case "
						+ "when age>=0 and age<=19 then '0-19岁' "
						+ "when age>=20 and age<=29 then '20-29岁' "
						+ "when age>=30 and age<=39 then '30-39岁' "
						+ "when age>=40 and age<=49 then '40-49岁' "
						+ "when age>=50 and age<=59 then '50-59岁' "
						+ "when age>=60 then '60岁以上' "
						+ "else '其他' end "
						+ "as label,h,count(user_id) as numbers "
						+ "from kylin_analysis "
						+ "where url_type in ("+type+" ) and d='"+endtime+"' "+agestr
						+ "group by case "
						+ "when age>=0 and age<=19 then '0-19岁' "
						+ "when age>=20 and age<=29 then '20-29岁' "
						+ "when age>=30 and age<=39 then '30-39岁' "
						+ "when age>=40 and age<=49 then '40-49岁' "
						+ "when age>=50 and age<=59 then '50-59岁' "
						+ "when age>=50 then '60岁以上' "
						+ "else '其他' end,h;";
				
				domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+" ) and d='"+endtime+"' "+agestr;
					break;
				case 3:sql ="select case "
						+ "when title=4 then '其他' "
						+ "when title=0 then '初级' "
						+ "when title=1 then '中级' "
						+ "when title=2 then '副高级' "
						+ "when title=3 then '正高级' "
						+ "end as label,h,count(user_id) as numbers "
						+ "from kylin_analysis "
						+ "where url_type in ("+type+" ) and d='"+endtime+"' "+titlestr
						+ "group by case "
						+ "when title=4 then '其他' "
						+ "when title=0 then '初级' "
						+ "when title=1 then '中级' "
						+ "when title=2 then '副高级' "
						+ "when title=3 then '正高级' "
						+ "end,h";
				domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+" ) and d='"+endtime+"' "+titlestr;
						break;
				case 4:
					sql ="select reserch_domain as label,h,count(user_id) as numbers "
							+ "from kylin_analysis "
							+ "where url_type in ("+type+")   and d='"+endtime+"' "+domainstr
							+ "group by reserch_domain,url_type,h";
						break;
			}
				list = kylin.findToListMap(sql);
				//listrearch=kylin.findToList(domainsql);
				if(list.size()>0){
					if(list.size()>0){
						for(Map<String,String> li :list){
							reslistnames.add(li.get("LABEL"));
						}

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
								if (StringUtils.isNotBlank(l.get("LABEL")) &&l.get("LABEL").equals(name)
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
			}else{
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
					switch(property){

						case 0:
							if(type.contains("0")){
								sql="select case "
										+ "when url_type=1 then '浏览数' "
										+ "when url_type=2 then '下载数' "
										+ "when url_type=3 then '检索数' "
										+ "when url_type=4 then '分享数' "
										+ "when url_type=5 then '收藏数' "
										+ "when url_type=6 then '导出数' "
										+ "when url_type=7 then '笔记数' "
										+ "when url_type=8 then '订阅数' "
										+ "end as label,d,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where  d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=1 then '浏览数' "
										+ "when url_type=2 then '下载数' "
										+ "when url_type=3 then '检索数' "
										+ "when url_type=4 then '分享数' "
										+ "when url_type=5 then '收藏数' "
										+ "when url_type=6 then '导出数' "
										+ "when url_type=7 then '笔记数' "
										+ "when url_type=8 then '订阅数' "
										+ "end,d";
								
								domainsql="select distinct reserch_domain from kylin_analysis where  d in("+days+") "+titlestr+exlevelstr+agestr;
							}else if(type.contains("1")){
								sql="select case "
										+ "when url_type=1 then '浏览数' "
										+ "end as label,h,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=1 then '浏览数' "
										+ "end,h";
								domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=1";
							}else if(type.contains("2")){
								sql="select case "
										+ "when url_type=2 then '下载数' "
										+ "end as label,h,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=2 then '下载数' "
										+ "end,h";
								domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=2";
							}else if(type.contains("3")){
								sql="select case "
										+ "when url_type=3 then '检索数' "
										+ "end as label,h,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=3 then '检索数' "
										+ "end,h";
								domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=3";
							}else if(type.contains("4")){
								sql="select case "
										+ "when url_type=4 then '分享数' "
										+ "end as label,h,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=4 then '分享数' "
										+ "end,h";
								domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=4";
							}else if(type.contains("5")){
								sql="select case "
										+ "when url_type=5 then '收藏数' "
										+ "end as label,h,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=5 then '收藏数' "
										+ "end,h";
								domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=5";
							}else if(type.contains("6")){
								sql="select case "
										+ "when url_type=6 then '导出数' "
										+ "end as label,h,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=6 then '导出数' "
										+ "end,h";
								domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=6";
							}else if(type.contains("7")){
								sql="select case "
										+ "when url_type=7 then '笔记数' "
										+ "end as label,h,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=7 then '笔记数' "
										+ "end,h";
								domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=7";
							}else if(type.contains("8")){
								sql="select case "
										+ "when url_type=8 then '订阅数' "
										+ "end as label,h,count(user_id) as numbers "
										+ "from kylin_analysis "
										+ "where d in("+days+") "+titlestr+exlevelstr+agestr+domainstr
										+ " group by case "
										+ "when url_type=8 then '订阅数' "
										+ "end,h";
								domainsql ="select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+" and url_type=8";
							}
							
						break;
						case 1:sql ="select case "
									+ "when education_level=0 then '大专' "
									+ "when education_level=1 then '本科' "
									+ "when education_level=2 then '硕士' "
									+ "when education_level=3 then '博士' "
									+ "when education_level=4 then '其他' "
									+ "end as label,d,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where url_type in ("+type+" ) and  d in("+days+") "+exlevelstr
									+ "group by case "
									+ "when education_level=0 then '大专' "
									+ "when education_level=1 then '本科' "
									+ "when education_level=2 then '硕士' "
									+ "when education_level=3 then '博士' "
									+ "when education_level=4 then '其他' "
									+ "end,d";
						domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+" ) and  d in("+days+") "+exlevelstr;
							break;
						case 2:sql="select case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他' end "
								+ "as label,d,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where url_type  in ("+type+") and  d in("+days+") "+agestr
								+ "group by case "
								+ "when age>=0 and age<=19 then '0-19岁' "
								+ "when age>=20 and age<=29 then '20-29岁' "
								+ "when age>=30 and age<=39 then '30-39岁' "
								+ "when age>=40 and age<=49 then '40-49岁' "
								+ "when age>=50 and age<=59 then '50-59岁' "
								+ "when age>=60 then '60岁以上' "
								+ "else '其他'end,d";
						
						domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+") and  d in("+days+") "+agestr;
							break;
						case 3:sql ="select case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end as label,d,count(user_id) as numbers "
								+ "from kylin_analysis "
								+ "where url_type in ("+type+") and  d in("+days+") "+titlestr
								+ "group by case "
								+ "when title=4 then '其他' "
								+ "when title=0 then '初级' "
								+ "when title=1 then '中级' "
								+ "when title=2 then '副高级' "
								+ "when title=3 then '正高级' "
								+ "end,d";
						
						domainsql="select distinct reserch_domain from kylin_analysis where url_type in ("+type+") and  d in("+days+") "+titlestr;
								break;	
						case 4:
							sql ="select reserch_domain as label,d,count(user_id) as numbers "
									+ "from kylin_analysis "
									+ "where url_type in ("+type+")   and d in ("+days+") "+domainstr
									+ "group by reserch_domain,url_type,d";
								break;
					}
					//listrearch = kylin.findToList(domainsql);
					list = kylin.findToListMap(sql);
					if(list.size()>0){
						for(Map<String,String> li :list){
							if(StringUtils.isNotBlank(li.get("LABEL"))){
								reslistnames.add(li.get("LABEL"));
							}
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
								for (Map<String, String> l : list) {
									if (StringUtils.isNotBlank(l.get("LABEL")) &&l.get("LABEL").equals(name)
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
			if(StringUtils.isNotBlank(typename)){
				if (m.get(typename) != null) {
					m.get(typename).add(l.get("NUMBERS"));
				} else {
					List<String> num = new ArrayList<String>();
					num.add(l.get("NUMBERS"));
					m.put(typename, num);
				}
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", m);
		map.put("title", reslistname);
		map.put("date", alldate);
		map.put("reserch_domain", listrearch);
		return map;
	
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
