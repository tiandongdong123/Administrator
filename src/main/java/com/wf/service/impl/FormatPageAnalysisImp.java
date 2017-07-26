package com.wf.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;

import com.utils.KylinJDBC;
import com.wf.service.FormatPageAnalysis;

public class FormatPageAnalysisImp implements FormatPageAnalysis {

	@Override
	public Object Formate(String sql,
			String type) {
		// TODO Auto-generated method stub
		KylinJDBC kdbc=new KylinJDBC();	
		List<Object> jsonp=new ArrayList<Object>();
		
		if(type.contains("1"))
		{
				Map<String , Object> map=new HashMap<String, Object>();
				String sql1="";
				sql1="select sum(numb.sune) from (select count(*) as sune "+sql+" ) as numb";
				JSONArray json=JSONArray.fromObject(kdbc.findToList(sql1));
				map.put("type", "1");
				if(json.get(0).equals(null)||"".equals(json.get(0))||json.get(0)==null)
				{
					map.put("value", "0");
				}
				else
				{
					map.put("value", json.get(0));
				}
				jsonp.add(map);
		}
		 if(type.contains("2"))
		{	
			 	Map<String , Object> map=new HashMap<String, Object>();
			 	String sql1="";
				sql1="select sum(numb.sune) from (select count(*) as sune "+sql+" and IS_ONLINE=0 ) as numb";
				JSONArray json=JSONArray.fromObject(kdbc.findToList(sql1));
				map.put("type", "2");
				if(json.get(0).equals(null)||"".equals(json.get(0))||json.get(0)==null)
				{
					map.put("value", "0");
				}
				else
				{
					map.put("value", json.get(0));
				}
				jsonp.add(map);
		}
		 if(type.contains("3"))
		{	
			 	Map<String , Object> map=new HashMap<String, Object>();
			 	String sql1="";
				sql1="select sum(numb.sune) from (select count(*)as sune from (select count(*)"+sql+" and IS_ONLINE=0  group by user_id having  count(user_id) > 1)) as numb";
				JSONArray json=JSONArray.fromObject(kdbc.findToList(sql1));
				map.put("type", "3");
				if(json.get(0).equals(null)||"".equals(json.get(0))||json.get(0)==null)
				{
					map.put("value", "0");
				}
				else
				{
					map.put("value", json.get(0));
				}
				jsonp.add(map);
		}
		 if(type.contains("4"))
			{	
				 		Map<String , Object> map=new HashMap<String, Object>();
				 		String sql1="";
						sql1="select sum(numb.sune) from  (select count(*) as sune from (select count(*)"+sql+" and IS_ONLINE=0 group by user_id having  count(user_id) > 1)) as numb ";
						JSONArray json=JSONArray.fromObject(kdbc.findToList(sql1));
						map.put("type", "6");
						if(json.get(0).equals(null)||"".equals(json.get(0))||json.get(0)==null)
						{
							map.put("value", "0");
						}
						else
						{
							map.put("value", json.get(0));
						}map.put("value", json.get(0));
						jsonp.add(map);
			}		
			 if(type.contains("5"))
			{	
				 		Map<String , Object> map=new HashMap<String, Object>();
				 		String sql1="";
						sql1=" select sum(c.a/c.b)  from  (select sum(tp) as a , count(*) as b   "+sql+"  ) as c ";
						JSONArray json=JSONArray.fromObject(kdbc.findToList(sql1));
						map.put("type", "7");
						if(json.get(0).equals(null)||"".equals(json.get(0))||json.get(0)==null)
						{
							map.put("value", "0");
						}
						else
						{
							map.put("value", json.get(0));
						}
						jsonp.add(map);
			}
				if(type.contains("6"))
				{		
						String sql1="";
						Map<String , Object> map=new HashMap<String, Object>();
						sql1="select sum(numb.sune) from (select count(*) as sune "+sql+" and IS_ONLINE=1  ) as numb ";
						JSONArray json=JSONArray.fromObject(kdbc.findToList(sql1));
						map.put("type", "5");
						if(json.get(0).equals(null)||"".equals(json.get(0))||json.get(0)==null)
						{
							map.put("value", "0");
						}
						else
						{
							map.put("value", json.get(0));
						}
						jsonp.add(map);
				}
			 
			 if(type.contains("7"))
			{		
				 	String sql1="";
				 	Map<String , Object> map=new HashMap<String, Object>();
					sql1="select sum(numb.sune) from (select count(*) as sune "+sql+" and IS_ONLINE=1 ) as numb";
					JSONArray json=JSONArray.fromObject(kdbc.findToList(sql1));
					map.put("type", "4");
					if(json.get(0).equals(null)||"".equals(json.get(0))||json.get(0)==null)
					{
						map.put("value", "0");
					}
					else
					{
						map.put("value", json.get(0));
					}
					jsonp.add(map);
			}
				
	
		
			 if(type.contains("8"))
			{	
				 	Map<String , Object> map=new HashMap<String, Object>();			
					String sql1="";
					String sql2="";
			
					sql1=" select count(*) "+sql+"  ";
					sql2="select count(*) "+sql+" and is_online=1  ";
					JSONArray json=JSONArray.fromObject(kdbc.findToList(sql1));
					JSONArray json1=JSONArray.fromObject(kdbc.findToList(sql2));
					map.put("type", "8");
					if(json.get(0).equals("0")||json1.get(0).equals("0"))
					{
						map.put("value", "0");
					}
					else
					{
						DecimalFormat df = new DecimalFormat("#0.00");
						map.put("value",(Double.valueOf(df.format(Double.valueOf(json1.get(0).toString())/Double.valueOf(json.get(0).toString())))*100));
					}
				
					jsonp.add(map);
			}	
			return jsonp;
		}

}
