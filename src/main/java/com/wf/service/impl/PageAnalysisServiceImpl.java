package com.wf.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.KylinJDBC;
import com.wf.dao.ModularMapper;
import com.wf.dao.PageManagerMapper;
import com.wf.service.FormatPageAnalysis;
import com.wf.service.PageAnalysisService;
@Service
public class PageAnalysisServiceImpl implements PageAnalysisService {

	@Autowired
	private ModularMapper modular;
	@Autowired
	private PageManagerMapper pageManagerMapper;
	@Override
	public Map<String,Object> getline(String title,String age,String exlevel,String datetype,String reserchdomain,String pageName,Integer type,String starttime,String endtime) {
		Map<String,Object> map = new HashMap<String, Object>();
		map = this.getViews(title, age, exlevel, datetype,reserchdomain,pageName,starttime,endtime,type);
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
	/**
	 * 
	 * @param title 职称
	 * @param age 年龄
	 * @param exlevel 学历 
	 * @param datetype 日期类型
	 * @param reserchdomain 感兴趣主题
	 * @param pageName 页面名称
	 * @param type 指标类型
	 * @param starttime 开始日期
	 * @param endtime 结束日期
	 * @return 
	 */
	public Map<String,Object> getViews(String title,String age,String exlevel,String datetype,String reserchdomain,String pageName,String starttime,String endtime,Integer type){
		List<String> reslistnames = new ArrayList<String>();
		List<String> alldate = new ArrayList<String>();
		KylinJDBC  kylin = new KylinJDBC();
		List<Map<String, String>> list= new ArrayList<Map<String,String>>();
		List<String> listrearch= new ArrayList<String>();
		String titlestr="";
		String agestr="";
		String exlevelstr="";
		String reserchdomainstr="";
		String pageNamestr ="";
		String sql = "";
		
		if(title!=null&&!"".equals(title)){
			titlestr=" and title in("+title+")";
		}
		if(exlevel!=null&&!"".equals(exlevel)){
			exlevelstr=" and education_level in("+exlevel+")";
		}
		if(reserchdomain!=null&&!"".equals(reserchdomain)){
			
			reserchdomainstr =" and RESERCH_DOMAIN like '%"+reserchdomain+"%'";
		}
		
		if(age!=null && !"".equals(age)){
			String[] ages = age.split(",");
			
			for(int i = 0 ;i<ages.length;i++){
				String[] a = ages[i].split("-");
				if(a.length==2){
					agestr=agestr+"  (age>="+a[0]+" and  age<="+a[1]+") or";
				}else{
					agestr=agestr+"(age>=60) or";
				}
			}
			agestr = " and "+agestr;
			agestr = agestr.substring(0,agestr.length()-2);
		}
		//需要更改 未完
		if(pageName!=null && !"".equals(pageName)){//pageName 为URL
			pageNamestr =" and URL = 'http://www.wanfangdata.com/download/degree?id=491'";//+ pageName ;
		}else if(pageName ==null || "".equals(pageName)){
			pageNamestr =" and URL = 'http://www.wanfangdata.com/download/degree?id=327'";
		}
		
		
		String reserch_domain = "";
		if(datetype.equals("1")){
			Calendar   cal   =   Calendar.getInstance();
			cal.add(Calendar.DATE,   -1);
			String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
			if(type==1){//页面浏览量
				sql = "select URL, h,count(user_id) as numbers from kylin_analysis where d='"+yesterday+"'"+pageNamestr+titlestr+exlevelstr+agestr+reserchdomainstr+" group by URL,h order by h";
				reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+pageNamestr+titlestr+exlevelstr+agestr+reserchdomainstr;
			}else if(type==2 || type==4){//页面访问次数 退出页次数  登录状态为1
				sql="select URL,h,count(user_id) as numbers from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h,is_online order by h";
				reserch_domain = "select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
			}else if(type==3 || type==6){//独立访客数、活跃用户数
				sql="select URL,h,count(distinct user_id) as numbers from kylin_analysis where  d='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h order by h";
				reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
			}else if(type == 5){
				sql="select URL,h,count(user_id) as numbers from kylin_analysis where d='"+yesterday+"' and SOURCE_URL='"+pageName+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h order by h";
				reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
			}else if(type==7){
				sql ="select URL,h,sum(TP)/count(user_id) as numbers from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h,is_online order by h";
				reserch_domain = "select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
			}else if(type==8){
				sql="select b.h h,b.url url,b.num/a.num numbers from (select count(user_id) as num from kylin_analysis where d ='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr +") a,(select url,h,cast(count(1) as decimal) as num from kylin_analysis where d ='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h) b order by b.h";
				reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+yesterday+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;	
			}
			list=kylin.findToListMap1(sql);
			listrearch =kylin.findToList1(reserch_domain);
			if(list.size()>0){
				for(Map<String,String> li :list){
					reslistnames.add(li.get("NUMBERS"));
					alldate.add(li.get("H"));
					
				}
		}
	}else if(datetype.equals("2")||datetype.equals("3")){
		int num = 0;
		if(datetype.equals("2")){
			num = 6;
		}else if(datetype.equals("3")){
			num = 30;
		}
		String days = "";
		for(int k=num;k>0;k--){
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,   -k);
		String day = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
		//alldate.add(day);
		days = days+"'"+day+"'"+",";
		}
		days=days+"'"+new SimpleDateFormat( "yyyy-MM-dd").format(new Date())+"'"+",";
		days=days.substring(0,days.length()-1);
		if(type==1){
			sql = "select URL,d,count(user_id) as numbers from kylin_analysis where d in("+days+") "+pageNamestr+titlestr+exlevelstr+agestr+reserchdomainstr+" group by URL,d order by d";
			reserch_domain = "select distinct reserch_domain from kylin_analysis where d in("+days+") "+pageNamestr+titlestr+exlevelstr+agestr+reserchdomainstr;
		}else if(type==2 || type==4){
			sql = "select URL,d,count(user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d order by d";
			reserch_domain = "select distinct reserch_domain from kylin_analysis where is_online=1 and d in("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;	
		}else if(type==3||type==6){
			sql="select URL,d,count(distinct user_id) as numbers from kylin_analysis where d in ("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d";
			reserch_domain = "select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
		}else if(type ==5){
			sql="select URL,d,count(user_id) as numbers from kylin_analysis where d in ("+days+") and SOURCE_URL='"+pageName+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d order by d";
			reserch_domain = "select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
		} else if(type ==7){
			sql ="select URL,d,sum(TP)/count(user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d,is_online order by d";
			reserch_domain = "select distinct reserch_domain from kylin_analysis where is_online=1 and d in("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
		}else if(type ==8){
			sql="select b.d d,b.url url,b.num/a.num numbers from (select count(user_id) as num from kylin_analysis where d in ("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr +") a,(select url,d,cast(count(1) as decimal) as num from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d) b order by b.d";
			reserch_domain = "select distinct reserch_domain from kylin_analysis where d in("+days+") "+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
		}
		list=kylin.findToListMap1(sql);
		listrearch =kylin.findToList1(reserch_domain);
		if(list.size()>0){
			for(Map<String,String> li :list){
				reslistnames.add(li.get("NUMBERS"));
				alldate.add(li.get("D"));
			}
		}
	}else{
			if(starttime.equals(endtime)){
				if(type==1){//页面浏览量
					sql = "select URL,h,count(user_id) as numbers from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h orderby h";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type==2 || type==4){//页面访问次数 退出页次数  登录状态为1
					sql="select URL,h,count(user_id) as numbers from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h,is_online orderby h";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type==3 || type==6){//独立访客数、活跃用户数
					sql="select URL,h,count(distinct user_id) as numbers from kylin_analysis where  d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h orderby h";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type == 5){
					sql="select URL,h,count(user_id) as numbers from kylin_analysis where d='"+endtime+"' and SOURCE_URL='"+pageName+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h orderby h";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type==7){
					sql ="select URL,h,sum(TP)/count(user_id) as numbers from kylin_analysis where is_online=1 and d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h,is_online orderby h";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where is_online=1 d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type==8){
					sql="select b.h h,b.url url,b.num/a.num numbers from (select count(user_id) as num from kylin_analysis where d ='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr +") a,(select url,h,cast(count(1) as decimal) as num from kylin_analysis where d ='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,h) b order by b.h";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d='"+endtime+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}
				list=kylin.findToListMap1(sql);
				listrearch =kylin.findToList1(reserch_domain);
				if(list.size()>0){
					for(Map<String,String> li :list){
						reslistnames.add(li.get("NUMBERS"));
						alldate.add(li.get("H"));
					}
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
				if(type==1){
					sql = "select URL,d,count(user_id) as numbers from kylin_analysis where d in("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d order by d";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type==2 || type==4){
					sql = "select URL,d,count(user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d order by d";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type==3||type==6){
					sql="select URL,d,count(distinct user_id) as numbers from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type ==5){
					sql="select URL,d,count(user_id) as numbers from kylin_analysis where d in ("+days+") and SOURCE_URL='"+pageName+"'"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d order by d";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				} else if(type ==7){
					sql ="select URL,d,sum(TP)/count(user_id) as numbers from kylin_analysis where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d,is_online order by d";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where is_online=1 and d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}else if(type ==8){
					sql="select b.d d,b.url url,b.num/a.num numbers from (select count(user_id) as num from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr +") a,(select url,d,cast(count(1) as decimal) as num from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr+" group by URL,d) b order by b.d";
					reserch_domain = "select distinct reserch_domain from kylin_analysis where d in ("+days+")"+titlestr+exlevelstr+agestr+pageNamestr+reserchdomainstr;
				}
				list=kylin.findToListMap1(sql);
				listrearch =kylin.findToList1(reserch_domain);
				if(list.size()>0){
					for(Map<String,String> li :list){
						reslistnames.add(li.get("NUMBERS"));
						alldate.add(li.get("D"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
	}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", reslistnames);
		map.put("date", alldate);
		map.put("reserch_domain",listrearch);
		map.put("type",typeToString(type));
		map.put("page",findAllPages());
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
	public List<Object> findAllPages(){
		return pageManagerMapper.findAllPages();
	}
	/* type类型 转换成字符串* */
	public static String typeToString(int type) {
		String str = "";
		switch (type) {

		case 1:
			str = "页面浏览量";
			break;
		case 2:
			str = "页面访问次数";
			break;
		case 3:
			str = "独立访客数";
			break;
		case 4:
			str = "退出页次数";
			break;
		case 5:
			str = "贡献下游浏览量";
			break;
		case 6:
			str = "活跃用户数";
			break;
		case 7:
			str = "页面平均访问时长";
			break;
		case 8:
			str = "退出率";
			break;
		}
		return str;
	}

	@Override
	public Object foemat(String age, String title, String exlevel,
			String reserchdomain, String pageName, String datetype,
			String type, String starttime, String endtime) {
		// TODO Auto-generated method stub
		KylinJDBC kdbc=new KylinJDBC();				
		String sql="";
		String sql1="";
		String sql2="";
		String time="";
		String teach_name="";
		String eduction_level="";
		String keyword="";
		String htmlword="";		
		String next_day="";
		List<Object> jsonp=new ArrayList<Object>();
		List<String> node=new ArrayList<String>();
		List<String> link=new ArrayList<String>();
		List<String > date=new ArrayList<String>();
		List<String > number=new ArrayList<String>();
		if(!("".equals(age)||age.equals(null)))
		{/*
			List<String> list = Arrays.asList(age.split(",")) ;
			if(list.size()==6)
			{
				time="";
			}
			else 
			{
				if(list.size()>3)
				{	
					List<String> dlist=new ArrayList<String>();
					for(int i=1;i<7;i++)
					{
						dlist.add(String.valueOf(i));							
					}
					dlist.removeAll(list);
					
					for(int i=0;i<dlist.size();i++)
					{
						if(dlist.get(i).equals("1"))
						{
							time=time+"and (age>19)  ";
						}
						if(dlist.get(i).equals("2"))
						{
							time=time+"and (age<20 or age>29)  ";
						}
						if(dlist.get(i).equals("3"))
						{
							time=time+"and (age<0 or age>39)  ";
						}
						if(dlist.get(i).equals("4"))
						{
							time=time+"and (age<40 and age>49)  ";
						}
						if(dlist.get(i).equals("5"))
						{
							time=time+"and (age<50 or age>59)  ";
						}
						if(dlist.get(i).equals("6"))
						{
							time=time+"and age<60  ";
						}
				}
					time = time.substring(3,time.length());
				}
				else
				{
					for(int i=0;i<list.size();i++)
					{
						if(list.get(i).equals("1"))
						{
							time=time+"or (age>0 and age<=19)  ";
						}
						if(list.get(i).equals("2"))
						{
							time=time+"or (age>=20 and age<=29)  ";
						}
						if(list.get(i).equals("3"))
						{
							time=time+"or (age>=30 and age<=39)  ";
						}
						if(list.get(i).equals("4"))
						{
							time=time+"or (age>=40 and age<=49)  ";
						}
						if(list.get(i).equals("5"))
						{
							time=time+"or (age>=50 and age<=59)  ";
						}
						if(list.get(i).equals("6"))
						{
							time=time+"or age>=60  ";
						}					
					}
					time = time.substring(2,time.length());
				}				
				time=" and ("+time+")";
			}
		*/
			
			List<String> list = Arrays.asList(age.split(","));
			
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i).equals("1"))
				{
					time=time+"or (age>0 and age<=19)  ";
				}
				if(list.get(i).equals("2"))
				{
					time=time+"or (age>=20 and age<=29)  ";
				}
				if(list.get(i).equals("3"))
				{
					time=time+"or (age>=30 and age<=39)  ";
				}
				if(list.get(i).equals("4"))
				{
					time=time+"or (age>=40 and age<=49)  ";
				}
				if(list.get(i).equals("5"))
				{
					time=time+"or (age>=50 and age<=59)  ";
				}
				if(list.get(i).equals("6"))
				{
					time=time+"or age>=60  ";
				}
				if(list.get(i).equals("7"))
				{
					time=time+"or age is null  ";
				}
			}
			
			time = " and ("+time.substring(2,time.length())+")";
		
		}		
		if(!("".equals(title)||title.equals(null)))
		{
			List<String> list = Arrays.asList(title.split(",")) ;
			if(list.size()==5)
			{
				teach_name="";
			}
			else
			{
				for(int i=0;i<list.size();i++)
				{
					if(list.get(i).equals("0"))
					{
						teach_name=teach_name+"or title=0 ";
					}
					if(list.get(i).equals("1"))
					{
						teach_name=teach_name+"or title=1 ";
					}
					if(list.get(i).equals("2"))
					{
						teach_name=teach_name+"or title=2 ";
					}
					if(list.get(i).equals("3"))
					{
						teach_name=teach_name+"or title=3 ";
					}
					if(list.get(i).equals("4"))
					{
						teach_name=teach_name+"or title=4 ";
					}
				}
				teach_name = teach_name.substring(2,teach_name.length());
				teach_name=" and ( "+teach_name+")";
			}
		}		
		if(!("".equals(exlevel)||exlevel.equals(null)))
		{
			List<String> list = Arrays.asList(exlevel.split(",")) ;
			if(list.size()==5)
			{
				eduction_level="";
			}
			else
			{
				for(int i=0;i<list.size();i++)
				{
					if(list.get(i).equals("0"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=0 ";
					}
					if(list.get(i).equals("1"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=1 ";
					}
					if(list.get(i).equals("2"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=2 ";
					}
					if(list.get(i).equals("3"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=3 ";
					}
					if(list.get(i).equals("4"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=4 ";
					}
				}	
				eduction_level = eduction_level.substring(2,eduction_level.length());
				eduction_level="and ("+eduction_level+")";
			}
		}
		
		if(!("".equals(reserchdomain)||reserchdomain.equals(null)))
		{
			keyword=" and reserch_domain='"+reserchdomain+"'";
		}
		if(!("".equals(pageName)||pageName.equals(null)))
		{	
			String link_address=pageManagerMapper.getLink_Address(pageName);
			htmlword="and source_url='"+link_address+"'";
		}
		Calendar   c   =   Calendar.getInstance();   
		c.add(Calendar.DAY_OF_MONTH, -1);  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String mDateTime=formatter.format(c.getTime());  
		String strStart=mDateTime.substring(0, 10); 
		if(datetype.equals("1"))
		{
			/*
			 * 昨天
			 */			
			sql=" from kylin_analysis where  d='"+strStart+"' "+time+teach_name+eduction_level+keyword+htmlword;
		
		}
		else if(datetype.equals("2"))
		{
			/*
			 * 一周
			 */
			Calendar   d  =   Calendar.getInstance();   
			d.add(Calendar.DAY_OF_MONTH, -7);  	 
			String dDateTime=formatter.format(d.getTime());  
			String dtrStart=dDateTime.substring(0, 10); 
			sql=" from kylin_analysis where  d<='"+strStart+"'and d>='"+dtrStart+"' "+time+teach_name+eduction_level+keyword+htmlword;
			
		}
		else if(datetype.equals("3"))
		{
			/*
			 * 一月
			 */
			Calendar   d  =   Calendar.getInstance();   
			d.add(Calendar.DAY_OF_MONTH, -30);  	 
			String dDateTime=formatter.format(d.getTime());  
			String dtrStart=dDateTime.substring(0, 10); 
			sql=" from kylin_analysis where  d<='"+strStart+"'and d>='"+dtrStart+"' "+time+teach_name+eduction_level+keyword+htmlword;	
		}
		else if(datetype.equals("4"))
		{
			/*
			 * 时间段
			 */
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Long betwoon=null;
			try {
				betwoon = sdf.parse(starttime).getTime()-sdf.parse(endtime).getTime();
				long d = betwoon/1000/60/60/24;//天
				if(d==0)
				{
				sql="from kylin_analysis where  d='"+starttime+"'"+time+teach_name+eduction_level+keyword+htmlword;
				}
				else if(d>0)
				{
					sql="from kylin_analysis where  d>='"+endtime+"' and d<='"+starttime+"'"+time+teach_name+eduction_level+keyword+htmlword;
				}
				else if(d<0)
				{
					sql="from kylin_analysis where  d<='"+endtime+"' and d>='"+starttime+"'"+time+teach_name+eduction_level+keyword+htmlword;
				}
				next_day=d+"";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(type.equals("1"))
		{
			if(datetype.equals("1"))
			{
				sql="select h,count(*)"+sql+" group by h order by h";
			}
			else 
			{
				if("".equals(next_day))
				{
					sql="select d,count(*)"+sql+" group by d order by d";
				}
				else 
				{
					if(next_day.equals("0"))
					{
						sql="select h,count(*)"+sql+" group by h order by h";
					}
					else
					{
						sql="select d,count(*)"+sql+" group by d order by d";
					}
				}
				
			}
			JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));					
			for(int i=0;i<json.size();i+=2)
			{
				node.add(json.get(i).toString());
			}						
			for(int i=1;i<json.size();i+=2)
			{
				link.add(json.get(i).toString());
			}
		}
		 if(type.equals("2"))
		{
			if(datetype.equals("1"))
			{
				sql="select h,count(*)"+sql+" and IS_ONLINE=0  group by h order by h";
			}
			else 
			{
				if("".equals(next_day))
				{
					sql="select d,count(*)"+sql+" and IS_ONLINE=0  group by d order by d";
				}
				else 
				{
					if(next_day.equals("0"))
					{
						sql="select h,count(*)"+sql+" and IS_ONLINE=0 group by h order by h";
					}
					else
					{
						sql="select d,count(*)"+sql+" and IS_ONLINE=0 group by d order by d";
					}
				}
			}		
			JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));
			for(int i=0;i<json.size();i+=2)
			{
				node.add(json.get(i).toString());
			}
			for(int i=1;i<json.size();i+=2)
			{
				link.add(json.get(i).toString());
			}
		}
		 if(type.equals("3"))
		{
			if(datetype.equals("1"))
			{
				sql="select h,count(*) from (select h,count(*)"+sql+" and IS_ONLINE=0  group by h,user_id having  count(user_id) > 1) group by h order by h ";
			}
			else 
			{
				if("".equals(next_day))
				{
					sql="select d,count(*) from (select d,count(*)"+sql+" and IS_ONLINE=0  group by d,user_id having  count(user_id) > 1) group by d order by d";
				}
				else 
				{
					if(next_day.equals("0"))
					{
						sql="select h,count(*) from (select h,count(*)"+sql+" and IS_ONLINE=0  group by h,user_id having  count(user_id) > 1) group by h order by h";
					}
					else
					{
						sql="select d,count(*) from (select d,count(*)"+sql+" and IS_ONLINE=0  group by d,user_id having  count(user_id) > 1) group by d order by d";					
						}
				}				
			}	
			JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));
			for(int i=0;i<json.size();i+=2)
			{
				node.add(json.get(i).toString());
			}
			for(int i=1;i<json.size();i+=2)
			{
				link.add(json.get(i).toString());
			}
		}
		 if(type.equals("4"))
		{
			if(datetype.equals("1"))
			{
				sql="select h,count(*)"+sql+" and IS_ONLINE=1  group by h order by h";
			}
			else 
			{
				if("".equals(next_day))
				{
					sql="select d,count(*)"+sql+" and IS_ONLINE=1  group by d order by d";
				}
				else 
				{
					if(next_day.equals("0"))
					{
						sql="select h,count(*)"+sql+" and IS_ONLINE=1 group by h order by h";
					}
					else
					{
						sql="select d,count(*)"+sql+" and IS_ONLINE=1 group by d order by d";
					}
				}
			}		
			JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));
			
			for(int i=0;i<json.size();i+=2)
			{
				node.add(json.get(i).toString());
			}
			
			
			for(int i=1;i<json.size();i+=2)
			{
				link.add(json.get(i).toString());
			}
		}
				
		if(type.equals("5"))
		{
			if(datetype.equals("1"))
			{
				sql="select h,count(*)"+sql+" and IS_ONLINE=1  group by h order by h";
			}
			else 
			{
				if("".equals(next_day))
				{
					sql="select d,count(*)"+sql+" and IS_ONLINE=1  group by d order by d";
				}
				else 
				{
					if(next_day.equals("0"))
					{
						sql="select h,count(*)"+sql+" and IS_ONLINE=1 group by h order by h";
					}
					else
					{
						sql="select d,count(*)"+sql+" and IS_ONLINE=1 group by d order by d";
					}
				}
			}
			JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));
			for(int i=0;i<json.size();i+=2)
			{
				node.add(json.get(i).toString());
			}
			for(int i=1;i<json.size();i+=2)
			{
				link.add(json.get(i).toString());
			}
		}
		 if(type.equals("6"))
		{
			 if(datetype.equals("1"))
				{
					sql="select h,count(*) from (select h,count(*)"+sql+" and IS_ONLINE=0  group by h,user_id having  count(user_id) > 1) group by h order by h ";
				}
				else 
				{
					if("".equals(next_day))
					{
						sql="select d,count(*) from (select d,count(*)"+sql+" and IS_ONLINE=0  group by d,user_id having  count(user_id) > 1) group by d order by d";
					}
					else 
					{
						if(next_day.equals("0"))
						{
							sql="select h,count(*) from (select h,count(*)"+sql+" and IS_ONLINE=0  group by h,user_id having  count(user_id) > 1) group by h order by h";
						}
						else
						{
							sql="select d,count(*) from (select d,count(*)"+sql+" and IS_ONLINE=0  group by d,user_id having  count(user_id) > 1) group by d order by d";					
							}
					}				
				}
				JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));	
				for(int i=0;i<json.size();i+=2)
				{
					node.add(json.get(i).toString());
				}
				for(int i=1;i<json.size();i+=2)
				{
					link.add(json.get(i).toString());
				}
		}
		
		 if(type.equals("7"))
		{
			 if(datetype.equals("1"))
				{
					sql=" select c.h ,cast(c.a/c.b as varchar)  from  (select  h, sum(tp) as a , count(*) as b   "+sql+"  group by h order by h) as c order by h";
				}
				else 
				{
					if("".equals(next_day))
					{
						sql=" select c.d ,cast(c.a/c.b as varchar)  from  (select  d, sum(tp) as a , count(*) as b   "+sql+"  group by d order by d) as c order by d";
					}
					else 
					{
						if(next_day.equals("0"))
						{
							sql=" select c.h ,cast(c.a/c.b as varchar)  from  (select  h, sum(tp) as a , count(*) as b   "+sql+"  group by h order by h) as c order by h";
						}
						else
						{
							sql=" select c.d ,cast(c.a/c.b as varchar)  from  (select  d, sum(tp) as a , count(*) as b   "+sql+"  group by d order by d) as c order by d";					
							}
					}				
				}
				JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));	
				for(int i=0;i<json.size();i+=2)
				{
					node.add(json.get(i).toString());
				}
				for(int i=1;i<json.size();i+=2)
				{
					link.add(json.get(i).toString());
				}
		}
		 if(type.equals("8"))
		{
			 if(datetype.equals("1"))
				{
					sql1=" select h,count(*) "+sql+" group by h order by h ";
					sql2="select h,count(*) "+sql+" and is_online=1  group by h order by h ";
				}
				else 
				{
					if("".equals(next_day))
					{
						sql1=" select d,count(*) "+sql+" group by d order by d ";
						sql2="select d,count(*) "+sql+" and is_online=1  group by d order by d ";					
						}
					else 
					{
						if(next_day.equals("0"))
						{
							sql1=" select h,count(*) "+sql+" group by h order by h ";
							sql2="select h,count(*) "+sql+" and is_online=1  group by h order by h ";						}
						else
						{
							sql1=" select d,count(*) "+sql+" group by d order by d ";
							sql2="select d,count(*) "+sql+" and is_online=1  group by d order by d ";							}
					}				
				}
				JSONArray json =JSONArray.fromObject(kdbc.findToList(sql1));	
				JSONArray json1 =new JSONArray();
				
				for (int i = 0; i < json.size(); i+=2) {
					json1.add(i,json.get(i));
					json1.add(i+1,0);
				}
				
				JSONArray tempjson=JSONArray.fromObject(kdbc.findToList(sql2));	
				
				for (int i = 0; i < json1.size(); i+=2) {
					for (int j = 0; j <tempjson.size(); j+=2) {
						if(json1.get(i).equals(tempjson.get(j))){
							 json1.set(i+1,tempjson.get(j+1));
						}
					}
				}
				
				for(int i=0;i<json.size();i+=2)
				{
					node.add(json.get(i).toString());
				}							
				for(int i=1;i<json.size();i+=2)
				{
					link.add(json.get(i).toString());
				}
				
				List<String> node1=new ArrayList<String>();
				for(int i=0;i<json1.size();i+=2)
				{
					node1.add(json1.get(i).toString());
				}							
				List<String> link1=new ArrayList<String>();
				for(int i=1;i<json1.size();i+=2)
				{
					link1.add(json1.get(i).toString());
				}
				
				
				
				List<String > num=new ArrayList<>();
				for(int i=0;i<node1.size();i++)
				{
					if(link.get(i).equals("0"))
					{
						num.add(i,"100");
					}
					else 
					{
						num.add(i,(new BigDecimal(Double.valueOf(link1.get(i))/Double.valueOf(link.get(i))).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue())+"");
					}
				}
				link=num;
		}
			if(node.size()>0)
			{
				if((node.get(0)+"").toString().length()==2)
				{					
					for(int i=0;i<24;i++)
					{
						if(i<10)
						{
							date.add("0"+i);
						}
						else 
						{
							date.add(i+"");
						}
						number.add(""+0);
					}
					for(int i=0;i<node.size();i++)
					{
						for(int j=date.size()-1;j>=0;j--)
						{
							if(node.get(i).equals(date.get(j)))
							{
								number.remove(j);
								number.add(j,link.get(i));
							}
						}
					}
				}
				else 
				{
					if(datetype.equals("2"))
					{	
						for(int i=7;i>=1;i--)
						{
							Calendar   d  =   Calendar.getInstance();   
							d.add(Calendar.DAY_OF_MONTH, -i);  	 
							String dDateTime=formatter.format(d.getTime());  
							String dtrStart=dDateTime.substring(0, 10); 
							date.add(dtrStart);
							number.add(""+0);
						}
						for(int i=0;i<node.size();i++)
						{
							for(int j=date.size()-1;j>=0;j--)
							{
								if(node.get(i).equals(date.get(j)))
								{
									number.remove(j);
									number.add(j,link.get(i));
									
								}
							}
						}
					}
					else if(datetype.equals("3"))
					{
						for(int i=30;i>=1;i--)
						{
							Calendar   d  =   Calendar.getInstance();   
							d.add(Calendar.DAY_OF_MONTH, -i);  	 
							String dDateTime=formatter.format(d.getTime());  
							String dtrStart=dDateTime.substring(0, 10); 
							date.add(dtrStart);
							number.add(""+0);
						}
						for(int i=0;i<node.size();i++)
						{
							for(int j=date.size()-1;j>=0;j--)
							{
								if(node.get(i).equals(date.get(j)))
								{
									number.remove(j);
									number.add(j,link.get(i));
								}
							}
						}
					}
					else if(datetype.equals("4"))
					{	
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");										
								Date d1=null;
								Date d2=null;
								try {
									d1 = new SimpleDateFormat("yyyy-MM-dd").parse(starttime);
									d2 = new SimpleDateFormat("yyyy-MM-dd").parse(endtime);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								Calendar dd = Calendar.getInstance();//定义日期实例
								dd.setTime(d1);//设置日期起始时间
								while(dd.getTime().before(d2)){
								//判断是否到结束日期
								SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
								String str = sdf1.format(dd.getTime());
								date.add(str);
								number.add(""+0);
								dd.add(Calendar.DAY_OF_WEEK, 1);//进行当前日期月份加1
								}															
						for(int i=0;i<node.size();i++)
						{
							for(int j=date.size()-1;j>=0;j--)
							{
								if(node.get(i).equals(date.get(j)))
								{
									number.remove(j);
									number.add(j,link.get(i));
								}
							}
						}
					}
				}
			}				
			jsonp.add(date);
			jsonp.add(number);
			return jsonp;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.wf.service.PageAnalysisService#getdatasource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public  Object getdatasource(String title, String age,
			String exlevel, String datetype, String reserchdomain,String type,
			String pageName, String starttime, String endtime) {
		// TODO Auto-generated method stub
		
				
		String sql="";
		String keyword="";
		String htmlword="";		
		List<Object> jsonp=new ArrayList<Object>();
		FormatPageAnalysis fpa=new FormatPageAnalysisImp();
		
		if(!("".equals(reserchdomain)||reserchdomain.equals(null)))
		{
			keyword=" and reserch_domain='"+reserchdomain+"'";
		}
		if(!("".equals(pageName)||pageName.equals(null)))
		{	
			String link_address=pageManagerMapper.getLink_Address(pageName);
			htmlword="and source_url='"+link_address+"'";
		}
		sql=keyword+htmlword;
		
		Calendar   c   =   Calendar.getInstance();   
		c.add(Calendar.DAY_OF_MONTH, -1);  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String mDateTime=formatter.format(c.getTime());  
		String strStart=mDateTime.substring(0, 10); 
		
		if(datetype.equals("1"))
		{
			/*
			 * 昨天
			 */			
			sql=" from kylin_analysis where  d='"+strStart+"' " +sql;
		
		}
		else if(datetype.equals("2"))
		{
			/*
			 * 一周
			 */
			Calendar   d  =   Calendar.getInstance();   
			d.add(Calendar.DAY_OF_MONTH, -7);  	 
			String dDateTime=formatter.format(d.getTime());  
			String dtrStart=dDateTime.substring(0, 10); 
			sql=" from kylin_analysis where  d<='"+strStart+"'and d>='"+dtrStart+"' "+sql;
			
		}
		else if(datetype.equals("3"))
		{
			/*
			 * 一月
			 */
			Calendar   d  =   Calendar.getInstance();   
			d.add(Calendar.DAY_OF_MONTH, -30);  	 
			String dDateTime=formatter.format(d.getTime());  
			String dtrStart=dDateTime.substring(0, 10); 
			sql=" from kylin_analysis where  d<='"+strStart+"'and d>='"+dtrStart+"' "+sql;	
		}
		else if(datetype.equals("4"))
		{
			/*
			 * 时间段
			 */
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Long betwoon=null;
			try {
				betwoon = sdf.parse(starttime).getTime()-sdf.parse(endtime).getTime();
				long d = betwoon/1000/60/60/24;//天
				if(d==0)
				{
				sql="from kylin_analysis where  d='"+starttime+"'"+sql;
				}
				else if(d>0)
				{
					sql="from kylin_analysis where  d>='"+endtime+"' and d<='"+starttime+"'"+sql;
				}
				else if(d<0)
				{
					sql="from kylin_analysis where  d<='"+endtime+"' and d>='"+starttime+"'"+sql;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		if(title.equals("null")&&exlevel.equals("null") && StringUtils.isEmpty(reserchdomain))
		{
			
			if(age.contains("1"))
			{	
				
				String sql1="";
				sql1=sql+"and (age<=19) ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 1);
				map.put("value", json);
				jsonp.add(map);
			}
			if(age.contains("2"))
			{	
				String sql1="";
				sql1=sql+"and ( age>=20 and  age<=29 )  ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 2);
				map.put("value", json);
				jsonp.add(map);
			}
			if(age.contains("3"))
			{	
				String sql1="";
				sql1=sql+"and ( age>=30 and  age<=39 )  ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 3);
				map.put("value", json);
				jsonp.add(map);
			}
			if(age.contains("4"))
			{	
				String sql1="";
				sql1=sql+"and ( age>=40 and  age<=49)  ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 4);
				map.put("value", json);
				jsonp.add(map);
			}
			if(age.contains("5"))
			{	
				String sql1="";
				sql1=sql+"and (age>=50 and age<=59)  ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 5);
				map.put("value", json);
				jsonp.add(map);
			}
			if(age.contains("6"))
			{	
				String sql1="";
				sql1=sql+"and  age>=60  ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 6);
				map.put("value", json);
				jsonp.add(map);
			}
			if(age.contains("7"))
			{	
				String sql1="";
				sql1=sql+"and  age is null  ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 7);
				map.put("value", json);
				jsonp.add(map);
			}
			
		}
		
		if(title.equals("null")&&age.equals("null") && StringUtils.isEmpty(reserchdomain))
		{
			if(exlevel.contains("0"))
			{	
				String sql1="";
				sql1=sql+"and  EDUCATION_LEVEL=0 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 0);
				map.put("value", json);
				jsonp.add(map);
			}			
			if(exlevel.contains("1"))
			{
				String sql1="";
				sql1=sql+"and  EDUCATION_LEVEL=1 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 1);
				map.put("value", json);
				jsonp.add(map);
			}
			if(exlevel.contains("2"))
			{
				String sql1="";
				sql1=sql+"and EDUCATION_LEVEL=2 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 2);
				map.put("value", json);
				jsonp.add(map);
			}
			if(exlevel.contains("3"))
			{
				String sql1="";
				sql1=sql+"and  EDUCATION_LEVEL=3 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 3);
				map.put("value", json);
				jsonp.add(map);
			
			}
			if(exlevel.contains("4"))
			{
				String sql1="";
				sql1=sql+"and  EDUCATION_LEVEL=4 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 4);
				map.put("value", json);
				jsonp.add(map);
			}
		}		
		if(exlevel.equals("null")&&age.equals("null") && StringUtils.isEmpty(reserchdomain))
		{
			if(title.contains("0"))
			{	
				String sql1="";
				sql1=sql+"and   title=0 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 0);
				map.put("value", json);
				jsonp.add(map);
			}
			if(title.contains("1"))
			{
				String sql1="";
				sql1=sql+"and  title=1 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 1);
				map.put("value", json);
				jsonp.add(map);
			}
			if(title.contains("2"))
			{
				String sql1="";
				sql1=sql+"and  title=2 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 2);
				map.put("value", json);
				jsonp.add(map);
			}
			if(title.contains("3"))
			{
				String sql1="";
				sql1=sql+"and  title=3 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 3);
				map.put("value", json);
				jsonp.add(map);
			}
			if(title.contains("4"))
			{
				String sql1="";
				sql1=sql+"and  title=4 ";
				JSONArray json=JSONArray.fromObject(fpa.Formate(sql1, type));
				Map<String , Object> map=new HashMap<>();
				map.put("type", 4);
				map.put("value", json);
				jsonp.add(map);
			}
		}
		
		if(StringUtils.isNotBlank(reserchdomain) && exlevel.equals("")&&age.equals("") && title.equals(""))
		{
			KylinJDBC kdbc=new KylinJDBC();	
			JSONArray json=new JSONArray();
			Map<String , Object> map=new HashMap<>();	
			
			map.put("value",reserchdomain);
			
			String sql1="select sum(numb.sune) from (select count(*) as sune "+sql+") as numb";
			json=JSONArray.fromObject(kdbc.findToList(sql1));
			map.put("value1",StringUtils.isNotBlank(json.get(0).toString())?json.get(0):"0");
	
			String  sql2="select sum(numb.sune) from (select count(*) as sune "+sql+" and IS_ONLINE=0 ) as numb";
			json=JSONArray.fromObject(kdbc.findToList(sql2));	
			map.put("value2",StringUtils.isNotBlank(json.get(0).toString())?json.get(0):"0");
		
			String sql3=sql1="select sum(numb.sune) from (select count(*)as sune from (select count(*)"+sql+"  and IS_ONLINE=0  group by user_id having  count(user_id) > 1)) as numb";
			json=JSONArray.fromObject(kdbc.findToList(sql3));
			map.put("value3",StringUtils.isNotBlank(json.get(0).toString())?json.get(0):"0");
					 		
			String sql4="select sum(numb.sune) from  (select count(*) as sune from (select count(*)"+sql+" and IS_ONLINE=0 group by user_id having  count(user_id) > 1)) as numb ";
		    json=JSONArray.fromObject(kdbc.findToList(sql4));	
		    map.put("value4",StringUtils.isNotBlank(json.get(0).toString())?json.get(0):"0");
		    
			String sql5=" select sum(c.a/c.b)  from  (select sum(tp) as a , count(*) as b   "+sql+"  ) as c ";
			json=JSONArray.fromObject(kdbc.findToList(sql5));	
			map.put("value5",StringUtils.isNotBlank(json.get(0).toString())?json.get(0):"0");
			
			
			String sql6="select sum(numb.sune) from (select count(*) as sune "+sql+" and IS_ONLINE=1  ) as numb ";
			json=JSONArray.fromObject(kdbc.findToList(sql6));
			map.put("value6",StringUtils.isNotBlank(json.get(0).toString())?json.get(0):"0");
			
			String sql7="select sum(numb.sune) from (select count(*) as sune "+sql+" and IS_ONLINE=1 ) as numb";
			json=JSONArray.fromObject(kdbc.findToList(sql7));
			map.put("value7",StringUtils.isNotBlank(json.get(0).toString())?json.get(0):"0");
			
			String sql8=" select count(*) "+sql;
			String sql9="select count(*) "+sql+" and is_online=1  ";
			
			json=JSONArray.fromObject(kdbc.findToList(sql8));
			JSONArray json1=JSONArray.fromObject(kdbc.findToList(sql9));
			map.put("value8",StringUtils.isNotBlank(json.get(0).toString())&&StringUtils.isNotBlank(json1.get(0).toString())?
					"0":Double.valueOf(new DecimalFormat("#0.00").format(Double.valueOf(json1.get(0).toString())/Double.valueOf(json.get(0).toString())*100)));		
			
			jsonp.add(map);
			
		}
		
			return jsonp;
	}

	@Override
	public Object getonedatasource(String title, String age, String exlevel,
			String datetype, String reserchdomain, String type,
			String pageName, String starttime, String endtime) {
		// TODO Auto-generated method stub		
		String sql="";
		String time="";
		String teach_name="";
		String eduction_level="";
		String keyword="";
		String htmlword="";		
		List<Object> jsonp=new ArrayList<Object>();

		FormatPageAnalysis fpa=new FormatPageAnalysisImp();
		if(!("".equals(age)||age.equals(null)))
		{/*
			List<String> list = Arrays.asList(age.split(",")) ;
			if(list.size()==6)
			{
				time="";
			}
			else 
			{
				if(list.size()>3)
				{	
					List<String> dlist=new ArrayList<String>();
					for(int i=1;i<7;i++)
					{
						dlist.add(String.valueOf(i));							
					}
					dlist.removeAll(list);
					
					for(int i=0;i<dlist.size();i++)
					{
						if(dlist.get(i).equals("1"))
						{
							time=time+"and (age>19)  ";
						}
						if(dlist.get(i).equals("2"))
						{
							time=time+"and (age<20 or age>29)  ";
						}
						if(dlist.get(i).equals("3"))
						{
							time=time+"and (age<0 or age>39)  ";
						}
						if(dlist.get(i).equals("4"))
						{
							time=time+"and (age<40 and age>49)  ";
						}
						if(dlist.get(i).equals("5"))
						{
							time=time+"and (age<50 or age>59)  ";
						}
						if(dlist.get(i).equals("6"))
						{
							time=time+"and age<60  ";
						}
				}
					time = time.substring(3,time.length());
				}
				else
				{
					for(int i=0;i<list.size();i++)
					{
						if(list.get(i).equals("1"))
						{
							time=time+"or (age>0 and age<=19)  ";
						}
						if(list.get(i).equals("2"))
						{
							time=time+"or (age>=20 and age<=29)  ";
						}
						if(list.get(i).equals("3"))
						{
							time=time+"or (age>=30 and age<=39)  ";
						}
						if(list.get(i).equals("4"))
						{
							time=time+"or (age>=40 and age<=49)  ";
						}
						if(list.get(i).equals("5"))
						{
							time=time+"or (age>=50 and age<=59)  ";
						}
						if(list.get(i).equals("6"))
						{
							time=time+"or age>=60  ";
						}					
					}
					time = time.substring(2,time.length());
				}				
				time=" and ("+time+")";
			}
		*/
			
			List<String> list = Arrays.asList(age.split(",")) ;
		
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i).equals("1"))
				{
					time=time+"or (age>0 and age<=19)  ";
				}
				if(list.get(i).equals("2"))
				{
					time=time+"or (age>=20 and age<=29)  ";
				}
				if(list.get(i).equals("3"))
				{
					time=time+"or (age>=30 and age<=39)  ";
				}
				if(list.get(i).equals("4"))
				{
					time=time+"or (age>=40 and age<=49)  ";
				}
				if(list.get(i).equals("5"))
				{
					time=time+"or (age>=50 and age<=59)  ";
				}
				if(list.get(i).equals("6"))
				{
					time=time+"or age>=60  ";
				}	
				if(list.get(i).equals("7"))
				{
					time=time+"or age is null  ";
				}	
			}
			time = time.substring(2,time.length());			
			time=" and ("+time+")";
		
		}		
		if(!("".equals(title)||title.equals(null)))
		{
			List<String> list = Arrays.asList(title.split(",")) ;
			if(list.size()==5)
			{
				teach_name="";
			}
			else
			{
				for(int i=0;i<list.size();i++)
				{
					if(list.get(i).equals("0"))
					{
						teach_name=teach_name+"or title=0 ";
					}
					if(list.get(i).equals("1"))
					{
						teach_name=teach_name+"or title=1 ";
					}
					if(list.get(i).equals("2"))
					{
						teach_name=teach_name+"or title=2 ";
					}
					if(list.get(i).equals("3"))
					{
						teach_name=teach_name+"or title=3 ";
					}
					if(list.get(i).equals("4"))
					{
						teach_name=teach_name+"or title=4 ";
					}
				}
				teach_name = teach_name.substring(2,teach_name.length());
				teach_name=" and ( "+teach_name+")";
			}
		}		
		if(!("".equals(exlevel)||exlevel.equals(null)))
		{
			List<String> list = Arrays.asList(exlevel.split(",")) ;
			if(list.size()==5)
			{
				eduction_level="";
			}
			else
			{
				for(int i=0;i<list.size();i++)
				{
					if(list.get(i).equals("0"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=0 ";
					}
					if(list.get(i).equals("1"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=1 ";
					}
					if(list.get(i).equals("2"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=2 ";
					}
					if(list.get(i).equals("3"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=3 ";
					}
					if(list.get(i).equals("4"))
					{
						eduction_level=eduction_level+"or EDUCATION_LEVEL=4 ";
					}
				}	
				eduction_level = eduction_level.substring(2,eduction_level.length());
				eduction_level="and ("+eduction_level+")";
			}
		}
		
		if(!("".equals(reserchdomain)||reserchdomain.equals(null)))
		{
			keyword=" and reserch_domain='"+reserchdomain+"'";
		}
		if(!("".equals(pageName)||pageName.equals(null)))
		{	
			String link_address=pageManagerMapper.getLink_Address(pageName);
			htmlword="and source_url='"+link_address+"'";
		}
		Calendar   c   =   Calendar.getInstance();   
		c.add(Calendar.DAY_OF_MONTH, -1);  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String mDateTime=formatter.format(c.getTime());  
		String strStart=mDateTime.substring(0, 10); 
		if(datetype.equals("1"))
		{
			/*
			 * 昨天
			 */			
			sql=" from kylin_analysis where  d='"+strStart+"' "+time+teach_name+eduction_level+keyword+htmlword;
		
		}
		else if(datetype.equals("2"))
		{
			/*
			 * 一周
			 */
			Calendar   d  =   Calendar.getInstance();   
			d.add(Calendar.DAY_OF_MONTH, -7);  	 
			String dDateTime=formatter.format(d.getTime());  
			String dtrStart=dDateTime.substring(0, 10); 
			sql=" from kylin_analysis where  d<='"+strStart+"'and d>='"+dtrStart+"' "+time+teach_name+eduction_level+keyword+htmlword;
			
		}
		else if(datetype.equals("3"))
		{
			/*
			 * 一月
			 */
			Calendar   d  =   Calendar.getInstance();   
			d.add(Calendar.DAY_OF_MONTH, -30);  	 
			String dDateTime=formatter.format(d.getTime());  
			String dtrStart=dDateTime.substring(0, 10); 
			sql=" from kylin_analysis where  d<='"+strStart+"'and d>='"+dtrStart+"' "+time+teach_name+eduction_level+keyword+htmlword;	
		}
		else if(datetype.equals("4"))
		{
			/*
			 * 时间段
			 */
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Long betwoon=null;
			try {
				betwoon = sdf.parse(starttime).getTime()-sdf.parse(endtime).getTime();
				long d = betwoon/1000/60/60/24;//天
				if(d==0)
				{
				sql="from kylin_analysis where  d='"+starttime+"'"+time+teach_name+eduction_level+keyword+htmlword;
				}
				else if(d>0)
				{
					sql="from kylin_analysis where  d>='"+endtime+"' and d<='"+starttime+"'"+time+teach_name+eduction_level+keyword+htmlword;
				}
				else if(d<0)
				{
					sql="from kylin_analysis where  d<='"+endtime+"' and d>='"+starttime+"'"+time+teach_name+eduction_level+keyword+htmlword;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JSONArray json=JSONArray.fromObject(fpa.Formate(sql, type));
		Map<String , Object> map=new HashMap<>();
		map.put("type", 1);
		map.put("value", json);
		jsonp.add(map);
		
		return jsonp;
	}
	
	
	
		
}
