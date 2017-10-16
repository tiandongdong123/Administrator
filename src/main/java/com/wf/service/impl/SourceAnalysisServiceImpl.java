package com.wf.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.xhtml.map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.bean.SourceAnalysisLinkHostDaily;
import com.wf.bean.SourceAnalysisLinkHostHourly;
import com.wf.bean.SourceAnalysisSearchEngineDaily;
import com.wf.bean.SourceAnalysisSearchEngineHourly;
import com.wf.bean.SourceAnalysisSearchWordDaily;
import com.wf.bean.SourceAnalysisSearchWordHourly;
import com.wf.bean.SourceAnalysisSourceDaily;
import com.wf.bean.SourceAnalysisSourceHourly;
import com.wf.dao.SourceAnalysisLinkHostDailyMapper;
import com.wf.dao.SourceAnalysisLinkHostHourlyMapper;
import com.wf.dao.SourceAnalysisSearchEngineDailyMapper;
import com.wf.dao.SourceAnalysisSearchEngineHourlyMapper;
import com.wf.dao.SourceAnalysisSearchWordDailyMapper;
import com.wf.dao.SourceAnalysisSearchWordHourlyMapper;
import com.wf.dao.SourceAnalysisSourceDailyMapper;
import com.wf.dao.SourceAnalysisSourceHourlyMapper;
import com.wf.service.SourceAnalysisService;
@Service
public class SourceAnalysisServiceImpl implements SourceAnalysisService {


	//@Autowired
	//private SourceAnalysisSourceDailyMapper sourceAnalysisSourceDailyMapper;
	@Autowired
	private SourceAnalysisSourceHourlyMapper sourceAnalysisSourceHourlyMapper;
	//@Autowired
	//private SourceAnalysisSearchEngineDailyMapper sourceAnalysisSearchEngineDailyMapper;
	@Autowired
	private SourceAnalysisSearchEngineHourlyMapper sourceAnalysisSearchEngineHourlyMapper;
	//@Autowired
	//private SourceAnalysisSearchWordDailyMapper sourceAnalysisSearchWordDailyMapper;
	@Autowired
	private SourceAnalysisSearchWordHourlyMapper sourceAnalysisSearchWordHourlyMapper;
	//@Autowired
	//private SourceAnalysisLinkHostDailyMapper sourceAnalysisLinkHostDailyMapper;
	@Autowired
	private SourceAnalysisLinkHostHourlyMapper sourceAnalysisLinkHostHourlyMapper;
	
	@Override
	public Map<String, Object> SourceAnalysisList(String flag, String date, String startTime,
			String endTime, Integer pageNum, Integer pageSize) {
		
		Map<String,Object> map=new HashMap();
		
		/**全部来源列表数据，天为统计单位时访问方法*/
		if("0".equals(flag)){
			map=SourceAnalysisSourceList(startTime,
				endTime, pageNum, pageSize);
		/**搜索引擎列表数据，天为统计单位时访问方法*/	
		}else if("1".equals(flag)){
			map=SourceAnalysisEngineList(startTime,
				endTime, pageNum, pageSize);
			
		/**检索词列表数据，天为统计单位时访问方法*/	
		}else if("2".equals(flag)){
			map=SourceAnalysisWordList(startTime,
				endTime, pageNum, pageSize);
			
		/**外部链接列表数据，天为统计单位时访问方法*/	
		}else if("3".equals(flag)){
			map=SourceAnalysisLinkList(startTime,
				endTime, pageNum, pageSize);
		
		/**全部来源列表数据，小时为统计单位时访问方法*/	
		}else if("4".equals(flag)){
			map=SourceAnalysisSourceHourlyList(date, Integer.valueOf(startTime),
				Integer.valueOf(endTime), pageNum, pageSize);
			
		/**搜索引擎列表数据，小时为统计单位时访问方法*/	
		}else if("5".equals(flag)){
			map=SourceAnalysisEngineHourlyList(date, Integer.valueOf(startTime),
				Integer.valueOf(endTime), pageNum, pageSize);
			
		/**检索词列表数据，小时为统计单位时访问方法*/	
		}else if("6".equals(flag)){
			map=SourceAnalysisWordHourlyList(date, Integer.valueOf(startTime),
				Integer.valueOf(endTime), pageNum, pageSize);
			
		/**外部链接列表数据，小时为统计单位时访问方法*/	
		}else if("7".equals(flag)){
			map=SourceAnalysisLinkHourlyList(date, Integer.valueOf(startTime),
				Integer.valueOf(endTime), pageNum, pageSize);
			
		}
		
		return map;
	}
	
	
	@Override
	public Map<String, Object> SourceAnalysisStatistics(
			String flag, String type, String date, String startTime, String endTime) {
		
		Map<String,Object> map=new HashMap();
		
		/**全部来源统计数据，天为统计单位时访问方法*/
		if("0".equals(flag)){
			map=SourceAnalysisSourceStatistics(flag,type,startTime, endTime);
		/**搜索引擎列表统计数据，天为统计单位时访问方法*/	
		}else if("1".equals(flag)){
			map=SourceAnalysisEngineStatistics(flag,type,date,startTime, endTime);
			
		/**检索词列表统计数据，天为统计单位时访问方法*/	
		}else if("2".equals(flag)){
			map=SourceAnalysisWordStatistics(flag,type,date,startTime, endTime);
			
		/**外部链接列表统计数据，天为统计单位时访问方法*/	
		}else if("3".equals(flag)){
			map=SourceAnalysisLinkStatistics(flag,type,date,startTime, endTime);
		
		/**全部来源统计数据，小时为统计单位访问方法*/	
		}else if("4".equals(flag)){
			map=SourceAnalysisSourceHourlyStatistics(type, date, Integer.valueOf(startTime), Integer.valueOf(endTime));
			
		/**搜索引擎列表统计数据，小时为统计单位访问方法*/	
		}else if("5".equals(flag)){
			map=SourceAnalysisEngineHourlyStatistics(flag, type, date, Integer.valueOf(startTime), Integer.valueOf(endTime));
			
		/**检索词列表统计数据，小时为统计单位访问方法*/	
		}else if("6".equals(flag)){
			map=SourceAnalysisWordHourlyStatistics(flag, type, date, Integer.valueOf(startTime), Integer.valueOf(endTime));
			
		/**外部链接列表统计数据，小时为统计单位访问方法*/	
		}else if("7".equals(flag)){
			map=SourceAnalysisLinkHourlyStatistics(flag, type, date, Integer.valueOf(startTime), Integer.valueOf(endTime));
			
		}
		
		return map;
	}
	
	
	/**
	* @Title: SourceAnalysisSourceList
	* @Description: TODO(全部来源列表数据，天为统计单位) 
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 8:22:01 PM
	 */
	public Map<String,Object> SourceAnalysisSourceList(String startTime,
			String endTime, Integer pageNum, Integer pageSize) {
		PageList pl=new PageList();
		
		/**放置页面底部列表汇总行数据*/
		Integer str=11111;
		Double str1=0.0;
		Double str2=0.0;
		Double str3=0.0;
		Double str4=0.0;
		Double str5=0.0;
		Double str6=0.0;
		Double str7=0.0;
		Double str8=0.0;
		
		/**页面底部列表其他行数据*/
		int startNum = (pageNum-1)*pageSize;
		List<SourceAnalysisSourceHourly> list=sourceAnalysisSourceHourlyMapper.getPage_day(startTime, endTime, startNum, pageSize);
		List<Object> list1=new ArrayList();
		
		for(int i=0;i<list.size();i++){
			SourceAnalysisSourceHourly sasd=list.get(i);
			
			str1+=Double.valueOf(sasd.getSum1());
			str2+=Double.valueOf(sasd.getSum2());
			str3+=Double.valueOf(sasd.getSum3());
			str5+=Double.valueOf(sasd.getSum5());
			str7+=Double.valueOf(sasd.getSum7());
			str4=str1/str2;
			sasd.setSum4(Double.isInfinite(str4)?"0":""+Double.valueOf(str4).intValue());
			Double dd=Double.valueOf(sasd.getSum5())*10000;
			int sum6=(int)(dd/Double.valueOf(sasd.getSum2()));
			sasd.setSum6(secToTime(sum6));
			
			String sum7=sasd.getSum7();
			String sum2=sasd.getSum2();
			
			if((!"0".equals(sum7))&&(!"0".equals(sum2))){
				sasd.setSum8(""+(int)(Double.valueOf(sum7)*100/(Double.valueOf(sum2)))+"%");
			}else{
				sasd.setSum8("0%");
			}
			list1.add(sasd);
		}
		
		/**当前页面汇总行数据，及页面底部最后一行数据*/
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSourceHourly sasd1=new SourceAnalysisSourceHourly();
		sasd1.setAccess_type(str); 
		sasd1.setSum1(""+str1.intValue());
		sasd1.setSum2(""+str2.intValue());
		sasd1.setSum3(""+str3.intValue());
		sasd1.setSum4(Double.isInfinite(str4)?"0":""+Double.valueOf(str4).intValue());
		sasd1.setSum5(""+str5.intValue());
		sasd1.setSum6(secToTime(str6.intValue()));
		sasd1.setSum7(""+str7.intValue());
		sasd1.setSum8(""+str8.intValue()+"%");
		list1.add(sasd1);
		
		
		str1=0.0;
		str2=0.0;
		str3=0.0;
		str4=0.0;
		str5=0.0;
		str6=0.0;
		str7=0.0;
		str8=0.0;
		/**当前条件所有数据汇总,即页面中间列表数据*/
		List<SourceAnalysisSourceHourly> lists=sourceAnalysisSourceHourlyMapper.getPages_day(startTime, endTime);
		for(int i=0;i<lists.size();i++){
			SourceAnalysisSourceHourly sasd=lists.get(i);
			str1+=Double.valueOf(sasd.getSum1());
			str2+=Double.valueOf(sasd.getSum2());
			str3+=Double.valueOf(sasd.getSum3());
			str5+=Double.valueOf(sasd.getSum5());
			str7+=Double.valueOf(sasd.getSum7());
		}	
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSourceHourly sasdSummary=new SourceAnalysisSourceHourly();
		sasdSummary.setSum1(""+str1.intValue());
		sasdSummary.setSum2(""+str2.intValue());
		sasdSummary.setSum3(""+str3.intValue());
		sasdSummary.setSum4(Double.isInfinite(str4)?"0":""+Double.valueOf(str4).intValue());
		sasdSummary.setSum5(""+str5.intValue());
		sasdSummary.setSum6(secToTime(str6.intValue()));
		sasdSummary.setSum7(""+str7.intValue());
		sasdSummary.setSum8(""+str8.intValue()+"%");
		
		
		if(lists!=null){
			
			pl.setTotalRow(lists.size());
		}
		pl.setPageRow(list1);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		
		Map<String,Object> map =new HashMap();
		map.put("pl", pl);
		map.put("sasdSummary", sasdSummary);
		return map;
	}
	
	
	/**
	* @Title: SourceAnalysisSourceStatistics
	* @Description: TODO(全部来源统计数据，天为统计单位) 
	* @param flag
	* @param type
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 3 Jan 2017 1:37:27 PM
	 */
	public Map<String, Object> SourceAnalysisSourceStatistics(
			String flag, String type, String startTime, String endTime) {
		List<SourceAnalysisSourceHourly> list=new ArrayList();
		List<String> groupList=new ArrayList();
		Map<String,Object> map=new HashMap();
		List<String> dataList=new ArrayList();
		String accessType="";
		
		/**饼图组织数据，手动拼写json字符串*/
		if("1".equals(type)||"2".equals(type)){/**组织饼图数据*/
		list=sourceAnalysisSourceHourlyMapper.getChart_day(type,startTime, endTime);
		StringBuffer sb=new StringBuffer();
		int count=0;
		sb.append("[");
		
		if(list.size()>0){
			sb.append("{");	
		}
		
		for(int i=0;i<list.size();i++){
			groupList.add(list.get(i).getAccess_type().toString());
			if("0".equals(list.get(i).getAccess_type().toString())){
				accessType="直接访问";
			}else if("1".equals(list.get(i).getAccess_type().toString())){
				accessType="搜索引擎";
			}else if("2".equals(list.get(i).getAccess_type().toString())){
				accessType="外部链接";
			}
			sb.append("value:"+list.get(i).getSum()+",name:\""+accessType);
			if(count==list.size()-1){
				sb.append("\"}");
			}else{
				sb.append("\"},{");
			}
			count++;
		}
		sb.append("]");
		map.put("jsonArr", sb);//访问类型数据数组
		}/**如果分析指标即type选择为平均访问页数4、平均访问时长6、跳出率8时，组织条形图数据*/
		else if("6".equals(type)||"4".equals(type)){
			
			String type1="";
			String type2="2";
			if("4".equals(type)){
				type1="1";
			}else if("6".equals(type)){
				type1="5";
			}else if("8".equals(type)){
				type1="7";
			}
			String average=sourceAnalysisSourceHourlyMapper.average_day(type1,type2,startTime,endTime);
			list=sourceAnalysisSourceHourlyMapper.indexAverageList_day(type1, type2, startTime, endTime);
			for(int i=0;i<list.size();i++){
				dataList.add(""+Math.ceil(Double.valueOf(list.get(i).getSum())));
				groupList.add(""+list.get(i).getAccess_type());
			}
			
			if("0".equals(groupList.get(groupList.size()-1))){
				accessType="直接访问";
			}else if("1".equals(groupList.get(groupList.size()-1))){
				accessType="搜索引擎";
			}else if("2".equals(groupList.get(groupList.size()-1))){
				accessType="外部链接";
			}
			
			map.put("average", null!=average?Math.ceil(Double.valueOf(average)):"");
			map.put("dataList",dataList);
			map.put("xaverage", accessType);
		}else if("8".equals(type)){
			List<SourceAnalysisSourceHourly> lists=sourceAnalysisSourceHourlyMapper.getPage_day(startTime, endTime, 0, 10);
			for(int i=0;i<lists.size();i++){
				dataList.add(""+""+(int)(Double.valueOf(lists.get(i).getSum7())*100/(Double.valueOf(lists.get(i).getSum2()))));
				groupList.add(""+lists.get(i).getAccess_type());
			}
			
			map.put("dataList",dataList);
		}
		
		/**折线图组织数据*/
		List<String> dateList=sourceAnalysisSourceHourlyMapper.getDateList_day(type,startTime, endTime);
		StringBuffer seriesStr=new StringBuffer();
		int count1=0;
		seriesStr.append("[");
		
		if(groupList.size()>0){
			seriesStr.append("{");	
		}
		
		for(int j=0;j< groupList.size();j++){
//			groupList.set(j,"\""+groupList.get(j)+"\"");
			List<String> numbersList=new ArrayList();
			List<String> numbersList1=new ArrayList();
			
			if("8".equals(type)){
				List<SourceAnalysisSourceHourly> temp=new ArrayList<SourceAnalysisSourceHourly>();
				SourceAnalysisSourceHourly analysisSourceDaily=null;
				for (int i = 0; i < dateList.size(); i++) {
					analysisSourceDaily=new SourceAnalysisSourceHourly();
					analysisSourceDaily.setDate(dateList.get(i));
					analysisSourceDaily.setNumbers("0%");
					temp.add(analysisSourceDaily);
				}
				
				List<Map<String, Object>> tempList=new ArrayList<Map<String, Object>>();
				tempList=sourceAnalysisSourceHourlyMapper.getLine1_day(groupList.get(j), type, startTime, endTime);
				
				for (int i = 0; i < temp.size(); i++) {
					for (int k = 0; k < tempList.size(); k++) {
						 if(temp.get(i).getDate().equals(tempList.get(k).get("date").toString())){
							 temp.get(i).setNumbers(tempList.get(k).get("numbers").toString());
							 break;
						 }
					}
				}
				
				for (SourceAnalysisSourceHourly sourceAnalysisSourceDaily : temp) {
					numbersList.add(sourceAnalysisSourceDaily.getNumbers());
				}
				
				
				for(String str:numbersList){
					numbersList1.add((""+Math.ceil(Double.valueOf(str.replace("%", "")))).split("\\.")[0]);
				}
			}else{	
				numbersList=sourceAnalysisSourceHourlyMapper.getLine_day(groupList.get(j),type,startTime, endTime);
				for(String str:numbersList){
					numbersList1.add((""+Math.ceil(Double.valueOf(str))).split("\\.")[0]);
				}
			}
			
			if("0".equals(groupList.get(j))){
				groupList.set(j,"\"直接访问\"");
			}else if("1".equals(groupList.get(j))){
				groupList.set(j,"\"搜索引擎\"");
			}else if("2".equals(groupList.get(j))){
				groupList.set(j,"\"外部链接\"");
			}
			
			seriesStr.append("itemStyle:{ normal: {label : {show: false}}},name:"+groupList.get(j)+",type:\"line\",data:"+numbersList1);
			if(count1==groupList.size()-1){
				seriesStr.append("}]");
			}else{
				seriesStr.append("},{");
			}
			
			count1++;
		}
		
		map.put("seriesArr", seriesStr);
		map.put("dateArr", dateList);
		map.put("groupArr", groupList.toString());
		return map;
	}
	
	
	/**
	* @Title: SourceAnalysisSourceHourlyList
	* @Description: TODO(全部来源列表数据,小时为统计单位)
	* @param date 
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 8:22:01 PM
	 */
	public Map<String,Object> SourceAnalysisSourceHourlyList(String date, Integer startTime,
			Integer endTime, Integer pageNum, Integer pageSize) {
		PageList pl=new PageList();	
		
		/**放置页面底部列表汇总行数据*/
		Integer str=11111;
		Double str1=0.0;
		Double str2=0.0;
		Double str3=0.0;
		Double str4=0.0;
		Double str5=0.0;
		Double str6=0.0;
		Double str7=0.0;
		Double str8=0.0;
		
		/**页面底部列表其他行数据*/
		int startNum = (pageNum-1)*pageSize;
		List<SourceAnalysisSourceHourly> list=sourceAnalysisSourceHourlyMapper.getPage(date, startTime, endTime, startNum, pageSize);
		List<Object> list1=new ArrayList();
		for(int i=0;i<list.size();i++){
			SourceAnalysisSourceHourly sash=list.get(i);
			
			str1+=Double.valueOf(sash.getSum1());
			str2+=Double.valueOf(sash.getSum2());
			str3+=Double.valueOf(sash.getSum3());
			str5+=Double.valueOf(sash.getSum5());
			str7+=Double.valueOf(sash.getSum7());
			str4=(Double.valueOf(sash.getSum1())/Double.valueOf(sash.getSum2()));
			sash.setSum4(Double.isInfinite(str4)?"0":""+Double.valueOf(str4).intValue());
			Double dd=Double.valueOf(sash.getSum5())*10000;
			int sum6=(int)(dd/Double.valueOf(sash.getSum2()));
			sash.setSum6(secToTime(sum6));
			
			String sum7=sash.getSum7();
			String sum2=sash.getSum2();
			
			if((!"0".equals(sum7))&&(!"0".equals(sum2))){
				sash.setSum8(""+(int)(Double.valueOf(sum7)*100/(Double.valueOf(sum2)))+"%");
			}else{
				sash.setSum8("0%");
			}
			list1.add(sash);
		}
		
		/**当前页面汇总行数据，及页面底部最后一行数据*/
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSourceHourly sasd1=new SourceAnalysisSourceHourly();
		sasd1.setAccess_type(str);
		sasd1.setSum1(""+str1.intValue());
		sasd1.setSum2(""+str2.intValue());
		sasd1.setSum3(""+str3.intValue());
		sasd1.setSum4(Double.isInfinite(str4)?"0":""+Double.valueOf(str4).intValue());
		sasd1.setSum5(""+str5.intValue());
		sasd1.setSum6(secToTime(str6.intValue()));
		sasd1.setSum7(""+str7.intValue());
		sasd1.setSum8(""+str8.intValue()+"%");
		list1.add(sasd1);
		
		str1=0.0;
		str2=0.0;
		str3=0.0;
		str4=0.0;
		str5=0.0;
		str6=0.0;
		str7=0.0;
		str8=0.0;
		/**当前条件所有数据汇总,即页面中间列表数据*/
		List<SourceAnalysisSourceHourly> lists=sourceAnalysisSourceHourlyMapper.getPages(date,startTime, endTime);
		for(int i=0;i<lists.size();i++){
			SourceAnalysisSourceHourly sasd=lists.get(i);
			str1+=Double.valueOf(sasd.getSum1());
			str2+=Double.valueOf(sasd.getSum2());
			str3+=Double.valueOf(sasd.getSum3());
			str5+=Double.valueOf(sasd.getSum5());
			str7+=Double.valueOf(sasd.getSum7());
		}	
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSourceHourly sasdSummary=new SourceAnalysisSourceHourly();
		sasdSummary.setSum1(""+str1.intValue());
		sasdSummary.setSum2(""+str2.intValue());
		sasdSummary.setSum3(""+str3.intValue());
		sasdSummary.setSum4(Double.isInfinite(str4)?"0":""+Double.valueOf(str4).intValue());
		sasdSummary.setSum5(""+str5.intValue());
		sasdSummary.setSum6(secToTime(str6.intValue()));
		sasdSummary.setSum7(""+str7.intValue());
		sasdSummary.setSum8(""+str8.intValue()+"%");
		
		if(lists!=null){
			pl.setTotalRow(lists.size());
		}
		pl.setPageRow(list1);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		
		Map<String,Object> map =new HashMap();
		map.put("pl", pl);
		map.put("sasdSummary", sasdSummary);
		return map;
	}
	
	
	/**
	* @Title: SourceAnalysisSourceHourlyStatistics
	* @Description: TODO(全部来源统计数据,小时为统计单位) 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 19 Dis 2016 9:41:13 AM
	 */
	public Map<String, Object> SourceAnalysisSourceHourlyStatistics(
			String type,String date, Integer startTime, Integer endTime) {
			
		List<SourceAnalysisSourceHourly> list=new ArrayList();
		List<String> groupList=new ArrayList();
		Map<String,Object> map=new HashMap();
		List<String> dataList=new ArrayList();
		String accessType="";
		
		/**饼图组织数据，手动拼写json字符串*/
		if("1".equals(type)||"2".equals(type)){/**组织饼图数据*/
		list=sourceAnalysisSourceHourlyMapper.getChart(type,date,startTime, endTime);
		StringBuffer sb=new StringBuffer();
		int count=0;
		sb.append("[");
		if(list.size()>0){
			sb.append("{");
		}
		for(int i=0;i<list.size();i++){
			if(null!=list.get(i).getAccess_type()){
				groupList.add(list.get(i).getAccess_type().toString());
				
				if("0".equals(list.get(i).getAccess_type().toString())){
					accessType="直接访问";
				}else if("1".equals(list.get(i).getAccess_type().toString())){
					accessType="搜索引擎";
				}else if("2".equals(list.get(i).getAccess_type().toString())){
					accessType="外部链接";
				}
				
				sb.append("value:"+list.get(i).getSum()+",name:\""+accessType);
			}
			if(count==list.size()-1){
				sb.append("\"}");
			}else{
				sb.append("\"},{");
			}
			count++;
			
		}
		sb.append("]");
		map.put("jsonArr", sb);//访问类型数据数组
		}/**如果分析指标即type选择为平均访问页数4、平均访问时长6、跳出率8时，组织条形图数据*/
		else if("6".equals(type)||"4".equals(type)){
			
			String type1="";
			String type2="2";
			if("4".equals(type)){
				type1="1";
			}else if("6".equals(type)){
				type1="5";
			}else if("8".equals(type)){
				type1="7";
			}
			String average=sourceAnalysisSourceHourlyMapper.average(type1,type2,date,startTime,endTime);
			list=sourceAnalysisSourceHourlyMapper.indexAverageList(type1, type2,date,startTime, endTime);
			for(int i=0;i<list.size();i++){
				dataList.add(""+Math.ceil(Double.valueOf(list.get(i).getSum())));
				groupList.add(""+list.get(i).getAccess_type());
			}
			
			if(groupList.size()>0){
				if("0".equals(groupList.get(groupList.size()-1))){
					accessType="直接访问";
				}else if("1".equals(groupList.get(groupList.size()-1))){
					accessType="搜索引擎";
				}else if("2".equals(groupList.get(groupList.size()-1))){
					accessType="外部链接";
				}
			}
			
			map.put("average",null!=average?Math.ceil(Double.valueOf(average)):"");
			map.put("dataList",dataList);
			map.put("xaverage", accessType);
		}else if("8".equals(type)){
			
			list=sourceAnalysisSourceHourlyMapper.getPage(date,startTime, endTime,0,10);
			for(int i=0;i<list.size();i++){
				dataList.add(""+""+(int)(Double.valueOf(list.get(i).getSum7())*100/(Double.valueOf(list.get(i).getSum2()))));
				groupList.add(""+list.get(i).getAccess_type());
			}
			map.put("dataList",dataList);
			
		}
		
		/**折线图组织数据*/
		List<String> dateList=sourceAnalysisSourceHourlyMapper.getDateList(type,date,startTime, endTime);
		StringBuffer seriesStr=new StringBuffer();
		int count1=0;
		seriesStr.append("[");
		
		if(groupList.size()>0){
			seriesStr.append("{");
		}
		
		for(int j=0;j< groupList.size();j++){
//			groupList.set(j,"\""+groupList.get(j)+"\"");
			List<String> numbersList=sourceAnalysisSourceHourlyMapper.getLine(groupList.get(j),type,date,startTime, endTime);
			List<String> numbersList1=new ArrayList();
			if("8".equals(type)){
				
				List<Map<String, Object>> temp=new ArrayList<Map<String, Object>>();
				Map<String, Object> tempmap=null;
				for (int i = 0; i < dateList.size(); i++) {
					tempmap=new HashMap<String, Object>();
					tempmap.put("date", dateList.get(i));
					tempmap.put("numbers","0%");
					temp.add(tempmap);
				}
				
				List<Map<String, Object>> tempList=new ArrayList<Map<String, Object>>();
				tempList=sourceAnalysisSourceHourlyMapper.getLine1(groupList.get(j),type,date,startTime, endTime);
				
				for (int i = 0; i < temp.size(); i++) {
					for (int k = 0; k < tempList.size(); k++) {
						 if(temp.get(i).get("date").equals(tempList.get(k).get("date")+"")){
							 temp.get(i).put("numbers",tempList.get(k).get("numbers"));
							 break;
						 }
					}
				}
				
				for (Map<String, Object> item : temp) {
					numbersList1.add((""+Math.ceil(Double.valueOf(item.get("numbers").toString().replace("%", "")))).split("\\.")[0]);
				}
				
			}else{
				for(String str:numbersList){
					numbersList1.add((""+Math.ceil(Double.valueOf(str))).split("\\.")[0]);
				}
			}
			
			
			if("0".equals(groupList.get(j))){
				groupList.set(j,"\"直接访问\"");
			}else if("1".equals(groupList.get(j))){
				groupList.set(j,"\"搜索引擎\"");
			}else if("2".equals(groupList.get(j))){
				groupList.set(j,"\"外部链接\"");
			}
			
			seriesStr.append("itemStyle:{ normal: {label : {show: false}}},name:"+groupList.get(j)+",type:\"line\",data:"+numbersList1);
			if(count1==groupList.size()-1){
				seriesStr.append("}]");
			}else{
				seriesStr.append("},{");
			}
			
			count1++;
		}
		map.put("seriesArr", seriesStr);
		map.put("dateArr", dateList);
		map.put("groupArr", groupList.toString());
		
		return map;
	}
	
	/**
	* @Title: SourceAnalysisEngineList
	* @Description: TODO(搜索引擎列表数据，天为统计单位) 
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 15 Dis 2016 8:24:36 PM
	 */
	public Map<String,Object> SourceAnalysisEngineList(String startTime,
			String endTime, Integer pageNum, Integer pageSize) {
		PageList pl=new PageList();
		
		/**放置页面底部列表汇总行数据*/
		String str="当前汇总";
		Double str1=0.0;
		Double str2=0.0;
		Double str3=0.0;
		Double str4=0.0;
		Double str5=0.0;
		Double str6=0.0;
		Double str7=0.0;
		Double str8=0.0;
		
		
		/**页面底部列表其他行数据*/
		int startNum = (pageNum-1)*pageSize;
		List<SourceAnalysisSearchEngineHourly> list=sourceAnalysisSearchEngineHourlyMapper.getPage_day(startTime, endTime, startNum, pageSize);
		List<Object> list1=new ArrayList();
		for(int i=0;i<list.size();i++){
			SourceAnalysisSearchEngineHourly sase=list.get(i);

			str1+=Double.valueOf(sase.getSum1());
			str2+=Double.valueOf(sase.getSum2());
			str3+=Double.valueOf(sase.getSum3());
			str5+=Double.valueOf(sase.getSum5());
			str7+=Double.valueOf(sase.getSum7());
			
			sase.setSum4(Double.isInfinite(Double.valueOf(sase.getSum1())/Double.valueOf(sase.getSum2()))?"0":""+(int)(Double.valueOf(sase.getSum1())/Double.valueOf(sase.getSum2())));
			Double dd=Double.valueOf(sase.getSum5())*10000;
			int sum6=(int)(dd/Double.valueOf(sase.getSum2()));
			sase.setSum6(secToTime(sum6));
			
			String sum7=sase.getSum7();
			String sum2=sase.getSum2();
			
			if((!"0".equals(sum7))&&(!"0".equals(sum2))){
				sase.setSum8(""+(int)(Double.valueOf(sum7)*100/(Double.valueOf(sum2)))+"%");
			}else{
				sase.setSum8("0%");
			}
			list1.add(sase);
		}
		
		/**当前页面汇总行数据，即页面底部最后一行数据*/
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSearchEngineHourly sase1=new SourceAnalysisSearchEngineHourly();
		sase1.setEngine_name(""+str);
		sase1.setSum1(""+str1.intValue());
		sase1.setSum2(""+str2.intValue());
		sase1.setSum3(""+str3.intValue());
		sase1.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		sase1.setSum5(""+str5.intValue());
		sase1.setSum6(secToTime(str6.intValue()));
		sase1.setSum7(""+str7.intValue());
		sase1.setSum8(""+str8.intValue()+"%");
		list1.add(sase1);
		
		/**变量清零后,重新投入使用*/
		str1=0.0;
		str2=0.0;
		str3=0.0;
		str4=0.0;
		str5=0.0;
		str6=0.0;
		str7=0.0;
		str8=0.0;
		/**当前条件所有数据汇总,即页面中间列表数据*/
		List<SourceAnalysisSearchEngineHourly> lists=sourceAnalysisSearchEngineHourlyMapper.getPages_day(startTime, endTime);
		for(int i=0;i<lists.size();i++){
			SourceAnalysisSearchEngineHourly sase=lists.get(i);
			str1+=Double.valueOf(sase.getSum1());
			str2+=Double.valueOf(sase.getSum2());
			str3+=Double.valueOf(sase.getSum3());
			str5+=Double.valueOf(sase.getSum5());
			str7+=Double.valueOf(sase.getSum7());
		}	
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSearchEngineHourly saseSummary=new SourceAnalysisSearchEngineHourly();
		saseSummary.setSum1(""+str1.intValue());
		saseSummary.setSum2(""+str2.intValue());
		saseSummary.setSum3(""+str3.intValue());
		saseSummary.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		saseSummary.setSum5(""+str5.intValue());
		saseSummary.setSum6(secToTime(str6.intValue()));
		saseSummary.setSum7(""+str7.intValue());
		saseSummary.setSum8(""+str8.intValue()+"%");
		
		
		if(lists!=null){
			
			pl.setTotalRow(lists.size());
		}
		pl.setPageRow(list1);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		
		Map<String,Object> map= new HashMap();
		map.put("pl", pl);
		map.put("saseSummary", saseSummary);
		return map;
	}
	
	/**
	* @Title: SourceAnalysisEngineStatistics
	* @Description: TODO(搜索引擎统计数据，天为统计单位) 
	* @param type
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 16 Dis 2016 10:18:55 AM
	 */
	public Map<String, Object> SourceAnalysisEngineStatistics(
			String flag, String type,String date, String startTime, String endTime) {
		
		List<SourceAnalysisSearchEngineHourly> list=new ArrayList();
		List<String> groupList=new ArrayList();
		Map<String,Object> map=new HashMap();
		List<String> dataList=new ArrayList();
		
		if("1".equals(type)||"2".equals(type)){/**组织饼图数据*/
			list=sourceAnalysisSearchEngineHourlyMapper.getChart_day(type,startTime, endTime);
			Map<String,Object> map1=new HashMap();
			map1.put("list", list);
			map1=pieData(flag,map1);
			StringBuffer sb=(StringBuffer)map1.get("sb");
			groupList=(List<String>)map1.get("groupList");
			
			map.put("jsonArr", sb);//访问类型数据数组
		}/**如果分析指标即type选择为平均访问页数4、平均访问时长6、跳出率8时，组织条形图数据*/
		else if("6".equals(type)||"4".equals(type)){
			
			String type1="";
			String type2="2";
			if("4".equals(type)){
				type1="1";
			}else if("6".equals(type)){
				type1="5";
			}else if("8".equals(type)){
				type1="7";
			}
			String average=sourceAnalysisSearchEngineHourlyMapper.average_day(type1,type2,startTime,endTime);
			list=sourceAnalysisSearchEngineHourlyMapper.indexAverageList_day(type1, type2, startTime, endTime);
			for(int i=0;i<list.size();i++){
				dataList.add((""+Math.ceil(Double.valueOf(list.get(i).getSum()))).split("\\.")[0]);
				groupList.add(list.get(i).getEngine_name());
			}
			
			map.put("average",null!=average?Math.ceil(Double.valueOf(average)):"");
			map.put("dataList",dataList);
			map.put("xaverage", groupList.get(groupList.size()-1));
		}else if("8".equals(type)){
			List<SourceAnalysisSearchEngineHourly> lists=sourceAnalysisSearchEngineHourlyMapper.getPage_day(startTime, endTime, 0, 10);
			for(int i=0;i<lists.size();i++){
				dataList.add(""+""+(int)(Double.valueOf(lists.get(i).getSum7())*100/(Double.valueOf(lists.get(i).getSum2()))));
				groupList.add(""+lists.get(i).getEngine_name());
			}
			
			map.put("dataList",dataList);
			
		}
		/**折线图组织数据*/
		List<String> dateList=sourceAnalysisSearchEngineHourlyMapper.getDateList_day(type,startTime, endTime);
		StringBuffer seriesStr=lineData(dateList,groupList,flag,type,date,startTime,endTime);
		
		map.put("seriesArr", seriesStr);
		map.put("dateArr", dateList);
		map.put("groupArr", groupList.toString());
		
		
		return map;
		
	}
	

	/**
	* @Title: SourceAnalysisEngineHourlyList
	* @Description: TODO(搜索引擎列表数据，小时为统计单位) 
	* @param date
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 3 Jan 2017 1:39:19 PM
	 */
	public Map<String,Object> SourceAnalysisEngineHourlyList(String date, Integer startTime,
			Integer endTime, Integer pageNum, Integer pageSize) {
		PageList pl=new PageList();
		
		/**放置页面底部列表汇总行数据*/
		String str="当前汇总";
		Double str1=0.0;
		Double str2=0.0;
		Double str3=0.0;
		Double str4=0.0;
		Double str5=0.0;
		Double str6=0.0;
		Double str7=0.0;
		Double str8=0.0;
		
		/**页面底部列表其他行数据*/
		int startNum = (pageNum-1)*pageSize;
		List<SourceAnalysisSearchEngineHourly> list=sourceAnalysisSearchEngineHourlyMapper.getPage(date, startTime, endTime, startNum, pageSize);
		List<Object> list1=new ArrayList();
		for(int i=0;i<list.size();i++){
			SourceAnalysisSearchEngineHourly sase=list.get(i);
			
			str1+=Double.valueOf(sase.getSum1());
			str2+=Double.valueOf(sase.getSum2());
			str3+=Double.valueOf(sase.getSum3());
			str5+=Double.valueOf(sase.getSum5());
			str7+=Double.valueOf(sase.getSum7());
			
			sase.setSum4(Double.isInfinite(Double.valueOf(sase.getSum1())/Double.valueOf(sase.getSum2()))?"0":""+(int)(Double.valueOf(sase.getSum1())/Double.valueOf(sase.getSum2())));
			Double dd=Double.valueOf(sase.getSum5())*10000;
			int sum6=(int)(dd/Double.valueOf(sase.getSum2()));
			sase.setSum6(secToTime(sum6));
			
			String sum7=sase.getSum7();
			String sum2=sase.getSum2();
			
			if((!"0".equals(sum7))&&(!"0".equals(sum2))){
				sase.setSum8(""+(int)(Double.valueOf(sum7)*100/(Double.valueOf(sum2)))+"%");
			}else{
				sase.setSum8("0%");
			}
			list1.add(sase);
		}
		
		/**当前页面汇总行数据，即页面底部最后一行数据*/
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSearchEngineHourly sase1=new SourceAnalysisSearchEngineHourly();
		sase1.setEngine_name(""+str);
		sase1.setSum1(""+str1.intValue());
		sase1.setSum2(""+str2.intValue());
		sase1.setSum3(""+str3.intValue());
		sase1.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		sase1.setSum5(""+str5.intValue());
		sase1.setSum6(secToTime(str6.intValue()));
		sase1.setSum7(""+str7.intValue());
		sase1.setSum8(""+str8.intValue()+"%");
		list1.add(sase1);
		
		/**变量清零后,重新投入使用*/
		str1=0.0;
		str2=0.0;
		str3=0.0;
		str4=0.0;
		str5=0.0;
		str6=0.0;
		str7=0.0;
		str8=0.0;
		/**当前条件所有数据汇总,即页面中间列表数据*/
		List<SourceAnalysisSearchEngineHourly> lists=sourceAnalysisSearchEngineHourlyMapper.getPages(date,startTime, endTime);
		for(int i=0;i<lists.size();i++){
			SourceAnalysisSearchEngineHourly sase=lists.get(i);
			str1+=Double.valueOf(sase.getSum1());
			str2+=Double.valueOf(sase.getSum2());
			str3+=Double.valueOf(sase.getSum3());
			str5+=Double.valueOf(sase.getSum5());
			str7+=Double.valueOf(sase.getSum7());
		}	
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSearchEngineHourly saseSummary=new SourceAnalysisSearchEngineHourly();
		saseSummary.setSum1(""+str1.intValue());
		saseSummary.setSum2(""+str2.intValue());
		saseSummary.setSum3(""+str3.intValue());
		saseSummary.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		saseSummary.setSum5(""+str5.intValue());
		saseSummary.setSum6(secToTime(str6.intValue()));
		saseSummary.setSum7(""+str7.intValue());
		saseSummary.setSum8(""+str8.intValue()+"%");
		
		if(lists!=null){
			
			pl.setTotalRow(lists.size());
		}
		pl.setPageRow(list1);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		
		Map<String,Object> map= new HashMap();
		map.put("pl", pl);
		map.put("saseSummary", saseSummary);
		return map;
	}
	
	/**
	* @Title: SourceAnalysisEngineHourlyStatistics
	* @Description: TODO(搜索引擎统计数据，小时为统计单位) 
	* @param flag
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 3 Jan 2017 1:39:43 PM
	 */
	public Map<String, Object> SourceAnalysisEngineHourlyStatistics(
			String flag,String type,String date, Integer startTime, Integer endTime) {
		
		List<SourceAnalysisSearchEngineHourly> list=new ArrayList();
		List<String> groupList=new ArrayList();
		Map<String,Object> map=new HashMap();
		List<String> dataList=new ArrayList();
		
		if("1".equals(type)||"2".equals(type)){/**组织饼图数据*/
			list=sourceAnalysisSearchEngineHourlyMapper.getChart(type, date, startTime, endTime);
			Map<String,Object> map1=new HashMap();
			map1.put("list", list);
			map1=pieData(flag,map1);
			StringBuffer sb=(StringBuffer)map1.get("sb");
			groupList=(List<String>)map1.get("groupList");
			map.put("jsonArr", sb);//访问类型数据数组
		}/**如果分析指标即type选择为平均访问页数4、平均访问时长6、跳出率8时，组织条形图数据*/
		else if("6".equals(type)||"4".equals(type)){
			
			String type1="";
			String type2="2";
			if("4".equals(type)){
				type1="1";
			}else if("6".equals(type)){
				type1="5";
			}else if("8".equals(type)){
				type1="7";
			}
			String average=sourceAnalysisSearchEngineHourlyMapper.average(type1,type2,date,startTime,endTime);
			list=sourceAnalysisSearchEngineHourlyMapper.indexAverageList(type1, type2,date,startTime, endTime);
			for(int i=0;i<list.size();i++){
				dataList.add(""+Math.ceil(Double.valueOf(list.get(i).getSum())));
				groupList.add(list.get(i).getEngine_name());
			}
			
			map.put("average",null!=average?Math.ceil(Double.valueOf(average)):"");
			map.put("dataList",dataList);
			map.put("xaverage", groupList.get(groupList.size()-1));
		}else if("8".equals(type)){
			
			list=sourceAnalysisSearchEngineHourlyMapper.getPage(date,startTime, endTime,0,10);
			for(int i=0;i<list.size();i++){
				dataList.add(""+""+(int)(Double.valueOf(list.get(i).getSum7())*100/(Double.valueOf(list.get(i).getSum2()))));
				groupList.add(""+list.get(i).getEngine_name());
			}
			map.put("dataList",dataList);
			
		}
		
		/**折线图组织数据*/
		List<String> dateList=sourceAnalysisSearchEngineHourlyMapper.getDateList(type,date,startTime, endTime);
		StringBuffer seriesStr=lineData(dateList,groupList,flag,type,date,startTime.toString(),endTime.toString());
		
		map.put("seriesArr", seriesStr);
		map.put("dateArr", dateList);
		map.put("groupArr", groupList.toString());
		
		return map;
		
	}	
	
	
	/**
	* @Title: SourceAnalysisWordList
	* @Description: TODO(检索词列表数据，天为统计单位) 
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 16 Dis 2016 9:24:14 AM
	 */
	public Map<String,Object> SourceAnalysisWordList(String startTime,
			String endTime, Integer pageNum, Integer pageSize) {
		PageList pl=new PageList();
		/**放置页面底部列表汇总行数据*/
		String str="当前汇总";
		Double str1=0.0;
		Double str2=0.0;
		Double str3=0.0;
		Double str4=0.0;
		Double str5=0.0;
		Double str6=0.0;
		Double str7=0.0;
		Double str8=0.0;
		
		/**页面底部列表其他行数据*/
		int startNum = (pageNum-1)*pageSize;
		List<SourceAnalysisSearchWordHourly> list=sourceAnalysisSearchWordHourlyMapper.getPage_day(startTime, endTime, startNum, pageSize);
		List<Object> list1=new ArrayList();
		for(int i=0;i<list.size();i++){
			SourceAnalysisSearchWordHourly sasw=list.get(i);
			
			str1+=Double.valueOf(sasw.getSum1());
			str2+=Double.valueOf(sasw.getSum2());
			str3+=Double.valueOf(sasw.getSum3());
			str5+=Double.valueOf(sasw.getSum5());
			str7+=Double.valueOf(sasw.getSum7());
			
			sasw.setSum4(Double.isInfinite(Double.valueOf(sasw.getSum1())/Double.valueOf(sasw.getSum2()))?"0":""+(int)(Double.valueOf(sasw.getSum1())/Double.valueOf(sasw.getSum2())));
			Double dd=Double.valueOf(sasw.getSum5())*10000;
			int sum6=(int)(dd/Double.valueOf(sasw.getSum2()));
			sasw.setSum6(secToTime(sum6));
			
			String sum7=sasw.getSum7();
			String sum2=sasw.getSum2();
			
			if((!"0".equals(sum7))&&(!"0".equals(sum2))){
				sasw.setSum8(""+(int)(Double.valueOf(sum7)*100/(Double.valueOf(sum2)))+"%");
			}else{
				sasw.setSum8("0%");
			}
			list1.add(sasw);
		}
		
		/**当前页面汇总行数据，及页面底部最后一行数据*/
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSearchWordHourly sasw1=new SourceAnalysisSearchWordHourly();
		sasw1.setKeyWord(str);
		sasw1.setSum1(""+str1.intValue());
		sasw1.setSum2(""+str2.intValue());
		sasw1.setSum3(""+str3.intValue());
		sasw1.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		sasw1.setSum5(""+str5.intValue());
		sasw1.setSum6(secToTime(str6.intValue()));
		sasw1.setSum7(""+str7.intValue());
		sasw1.setSum8(""+str8.intValue()+"%");
		list1.add(sasw1);
		
		/**变量清零后,重新投入使用*/
		str1=0.0;
		str2=0.0;
		str3=0.0;
		str4=0.0;
		str5=0.0;
		str6=0.0;
		str7=0.0;
		str8=0.0;
		/**当前条件所有数据汇总,即页面中间列表数据*/
		List<SourceAnalysisSearchWordHourly> lists=sourceAnalysisSearchWordHourlyMapper.getPages_day(startTime, endTime);
		for(int i=0;i<lists.size();i++){
			SourceAnalysisSearchWordHourly sasw=lists.get(i);
			str1+=Double.valueOf(sasw.getSum1());
			str2+=Double.valueOf(sasw.getSum2());
			str3+=Double.valueOf(sasw.getSum3());
			str5+=Double.valueOf(sasw.getSum5());
			str7+=Double.valueOf(sasw.getSum7());
		}	
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSearchWordHourly saswSummary=new SourceAnalysisSearchWordHourly();
		saswSummary.setSum1(""+str1.intValue());
		saswSummary.setSum2(""+str2.intValue());
		saswSummary.setSum3(""+str3.intValue());
		saswSummary.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		saswSummary.setSum5(""+str5.intValue());
		saswSummary.setSum6(secToTime(str6.intValue()));
		saswSummary.setSum7(""+str7.intValue());
		saswSummary.setSum8(""+str8.intValue()+"%");
		
		if(lists!=null){
			
			pl.setTotalRow(lists.size());
		}
		pl.setPageRow(list1);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		Map<String,Object> map=new HashMap();
		map.put("pl", pl);
		map.put("saseSummary", saswSummary);
		return map;
	}
	
	/**
	* @Title: SourceAnalysisWordStatistics
	* @Description: TODO(检索词统计数据，天为统计单位) 
	* @param flag
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 3 Jan 2017 1:41:01 PM
	 */
	public Map<String, Object> SourceAnalysisWordStatistics(
			String flag, String type,String date, String startTime, String endTime) {
		List<SourceAnalysisSearchWordHourly> list=new ArrayList();
		List<String> groupList=new ArrayList();
		Map<String,Object> map=new HashMap();
		List<String> dataList=new ArrayList();
		
		if("1".equals(type)||"2".equals(type)){/**组织饼图数据*/
			list=sourceAnalysisSearchWordHourlyMapper.getChart_day(type,startTime, endTime);
			Map<String,Object> map1=new HashMap();
			map1.put("list", list);
			map1=pieData(flag,map1);
			StringBuffer sb=(StringBuffer)map1.get("sb");
			groupList=(List<String>)map1.get("groupList");
			
			map.put("jsonArr", sb);//访问类型数据数组
		}/**如果分析指标即type选择为平均访问页数4、平均访问时长6、跳出率8时，组织条形图数据*/
		else if("6".equals(type)||"4".equals(type)){
			
			String type1="";
			String type2="2";
			if("4".equals(type)){
				type1="1";
			}else if("6".equals(type)){
				type1="5";
			}else if("8".equals(type)){
				type1="7";
			}
			String average=sourceAnalysisSearchWordHourlyMapper.average_day(type1,type2,startTime,endTime);
			list=sourceAnalysisSearchWordHourlyMapper.indexAverageList_day(type1, type2, startTime, endTime);
			for(int i=0;i<list.size();i++){
				dataList.add(""+Math.ceil(Double.valueOf(list.get(i).getSum())));
				groupList.add(list.get(i).getKeyWord());
			}
			
			map.put("average",null!=average?Math.ceil(Double.valueOf(average)):"");
			map.put("dataList",dataList);
			map.put("xaverage", groupList.get(groupList.size()-1));
		}else if("8".equals(type)){
			List<SourceAnalysisSearchWordHourly> lists=sourceAnalysisSearchWordHourlyMapper.getPage_day(startTime, endTime, 0, 10);
			for(int i=0;i<lists.size();i++){
				dataList.add(""+""+(int)(Double.valueOf(lists.get(i).getSum7())*100/(Double.valueOf(lists.get(i).getSum2()))));
				groupList.add(""+lists.get(i).getKeyWord());
			}
			
			map.put("dataList",dataList);
		}
		
		/**折线图组织数据*/
		List<String> dateList=sourceAnalysisSearchWordHourlyMapper.getDateList_day(type,startTime, endTime);
		StringBuffer seriesStr=lineData(dateList,groupList,flag,type,date,startTime,endTime);
		
		map.put("seriesArr", seriesStr);
		map.put("dateArr", dateList);
		map.put("groupArr", groupList.toString());
		
		return map;
		
	}
	
	/**
	* @Title: SourceAnalysisWordHourlyList
	* @Description: TODO(检索词列表数据，小时为统计单位) 
	* @param date
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 16 Dis 2016 9:24:14 AM
	 */
	public Map<String,Object> SourceAnalysisWordHourlyList(String date, Integer startTime,
			Integer endTime, Integer pageNum, Integer pageSize) {
		PageList pl=new PageList();
		
		/**放置页面底部列表汇总行数据*/
		String str="当前汇总";
		Double str1=0.0;
		Double str2=0.0;
		Double str3=0.0;
		Double str4=0.0;
		Double str5=0.0;
		Double str6=0.0;
		Double str7=0.0;
		Double str8=0.0;
		
		/**页面底部列表其他行数据*/
		int startNum = (pageNum-1)*pageSize;
		List<SourceAnalysisSearchWordHourly> list=sourceAnalysisSearchWordHourlyMapper.getPage(date,startTime, endTime, startNum, pageSize);
		List<Object> list1=new ArrayList();
		for(int i=0;i<list.size();i++){
			SourceAnalysisSearchWordHourly sasw=list.get(i);
			
			str1+=Double.valueOf(sasw.getSum1());
			str2+=Double.valueOf(sasw.getSum2());
			str3+=Double.valueOf(sasw.getSum3());
			str5+=Double.valueOf(sasw.getSum5());
			str7+=Double.valueOf(sasw.getSum7());
			
			sasw.setSum4(Double.isInfinite(Double.valueOf(sasw.getSum1())/Double.valueOf(sasw.getSum2()))?"0":""+(int)(Double.valueOf(sasw.getSum1())/Double.valueOf(sasw.getSum2())));
			Double dd=Double.valueOf(sasw.getSum5())*10000;
			int sum6=(int)(dd/Double.valueOf(sasw.getSum2()));
			sasw.setSum6(secToTime(sum6));
			
			String sum7=sasw.getSum7();
			String sum2=sasw.getSum2();
			
			if((!"0".equals(sum7))&&(!"0".equals(sum2))){
				sasw.setSum8(""+(int)(Double.valueOf(sum7)*100/(Double.valueOf(sum2)))+"%");
			}else{
				sasw.setSum8("0%");
			}
			list1.add(sasw);
		}
		
		/**当前页面汇总行数据，即页面底部最后一行数据*/
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSearchWordHourly sase1=new SourceAnalysisSearchWordHourly();
		sase1.setKeyWord(""+str);
		sase1.setSum1(""+str1.intValue());
		sase1.setSum2(""+str2.intValue());
		sase1.setSum3(""+str3.intValue());
		sase1.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		sase1.setSum5(""+str5.intValue());
		sase1.setSum6(secToTime(str6.intValue()));
		sase1.setSum7(""+str7.intValue());
		sase1.setSum8(""+str8.intValue()+"%");
		list1.add(sase1);
		
		/**变量清零后,重新投入使用*/
		str1=0.0;
		str2=0.0;
		str3=0.0;
		str4=0.0;
		str5=0.0;
		str6=0.0;
		str7=0.0;
		str8=0.0;
		/**当前条件所有数据汇总,即页面中间列表数据*/
		List<SourceAnalysisSearchWordHourly> lists=sourceAnalysisSearchWordHourlyMapper.getPages(date,startTime, endTime);
		for(int i=0;i<lists.size();i++){
			SourceAnalysisSearchWordHourly sase=lists.get(i);
			str1+=Double.valueOf(sase.getSum1());
			str2+=Double.valueOf(sase.getSum2());
			str3+=Double.valueOf(sase.getSum3());
			str5+=Double.valueOf(sase.getSum5());
			str7+=Double.valueOf(sase.getSum7());
		}	
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisSearchWordHourly saseSummary=new SourceAnalysisSearchWordHourly();
		saseSummary.setSum1(""+str1.intValue());
		saseSummary.setSum2(""+str2.intValue());
		saseSummary.setSum3(""+str3.intValue());
		saseSummary.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		saseSummary.setSum5(""+str5.intValue());
		saseSummary.setSum6(secToTime(str6.intValue()));
		saseSummary.setSum7(""+str7.intValue());
		saseSummary.setSum8(""+str8.intValue()+"%");
		
		if(lists!=null){
			pl.setTotalRow(lists.size());
		}
		pl.setPageRow(list1);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		
		Map<String,Object> map= new HashMap();
		map.put("pl", pl);
		map.put("saseSummary", saseSummary);
		
		return map;
	}
	
	/**
	* @Title: SourceAnalysisWordHourlyStatistics
	* @Description: TODO(检索词统计数据，小时为统计单位)
	* @param flag 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 16 Dis 2016 11:02:43 AM
	 */
	public Map<String, Object> SourceAnalysisWordHourlyStatistics(
			String flag,String type,String date, Integer startTime, Integer endTime) {
		
		List<SourceAnalysisSearchWordHourly> list=new ArrayList();
		List<String> groupList=new ArrayList();
		Map<String,Object> map=new HashMap();
		List<String> dataList=new ArrayList();
		
		if("1".equals(type)||"2".equals(type)){/**组织饼图数据*/
			list=sourceAnalysisSearchWordHourlyMapper.getChart(type, date, startTime, endTime);
			Map<String,Object> map1=new HashMap();
			map1.put("list", list);
			map1=pieData(flag,map1);
			StringBuffer sb=(StringBuffer)map1.get("sb");
			groupList=(List<String>)map1.get("groupList");
			map.put("jsonArr", sb);//访问类型数据数组
		}/**如果分析指标即type选择为平均访问页数4、平均访问时长6、跳出率8时，组织条形图数据*/
		else if("6".equals(type)||"4".equals(type)){
			
			String type1="";
			String type2="2";
			if("4".equals(type)){
				type1="1";
			}else if("6".equals(type)){
				type1="5";
			}else if("8".equals(type)){
				type1="7";
			}
			String average=sourceAnalysisSearchWordHourlyMapper.average(type1,type2,date,startTime,endTime);
			list=sourceAnalysisSearchWordHourlyMapper.indexAverageList(type1, type2,date,startTime, endTime);
			for(int i=0;i<list.size();i++){
				dataList.add(""+Math.ceil(Double.valueOf(list.get(i).getSum())));
				groupList.add(list.get(i).getKeyWord());
			}
			
			map.put("average", null!=average?Math.ceil(Double.valueOf(average)):"");
			map.put("dataList",dataList);
			map.put("xaverage", groupList.get(groupList.size()-1));
		}else if("8".equals(type)){
			list=sourceAnalysisSearchWordHourlyMapper.getPage(date, startTime, endTime, 0, 10);
			
			for (int i = 0; i < list.size(); i++) {
				dataList.add(""+""+(int)(Double.valueOf(list.get(i).getSum7())*100/(Double.valueOf(list.get(i).getSum2()))));
				groupList.add(""+list.get(i).getKeyWord());

			}
		}
		
		/**折线图组织数据*/
		List<String> dateList=sourceAnalysisSearchWordHourlyMapper.getDateList(type,date,startTime, endTime);
		StringBuffer seriesStr=lineData(dateList,groupList,flag,type,date,startTime.toString(),endTime.toString());
		
		map.put("seriesArr", seriesStr);
		map.put("dateArr", dateList);
		map.put("groupArr", groupList.toString());
		
		return map;
		
	}
	
	/**
	* @Title: SourceAnalysisLinkList
	* @Description: TODO(外部链接列表数据，天为统计单位) 
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 16 Dis 2016 9:40:05 AM
	 */
	public Map<String,Object> SourceAnalysisLinkList(String startTime,
			String endTime, Integer pageNum, Integer pageSize) {
		PageList pl=new PageList();
		/**放置页面底部列表汇总行数据*/
		String str="当前汇总";
		Double str1=0.0;
		Double str2=0.0;
		Double str3=0.0;
		Double str4=0.0;
		Double str5=0.0;
		Double str6=0.0;
		Double str7=0.0;
		Double str8=0.0;
		
		/**页面底部列表其他行数据*/
		int startNum = (pageNum-1)*pageSize;
		List<SourceAnalysisLinkHostHourly> list=sourceAnalysisLinkHostHourlyMapper.getPage_day(startTime, endTime, startNum, pageSize);
		List<Object> list1=new ArrayList();
		for(int i=0;i<list.size();i++){
			SourceAnalysisLinkHostHourly salh=list.get(i);
			str1+=Double.valueOf(salh.getSum1());
			str2+=Double.valueOf(salh.getSum2());
			str3+=Double.valueOf(salh.getSum3());
			str5+=Double.valueOf(salh.getSum5());
			str7+=Double.valueOf(salh.getSum7());
			
			salh.setSum4(Double.isInfinite(Double.valueOf(salh.getSum1())/Double.valueOf(salh.getSum2()))?"0":""+(int)(Double.valueOf(salh.getSum1())/Double.valueOf(salh.getSum2())));
			
			Double dd=Double.valueOf(salh.getSum5())*10000;
			int sum6=(int)(dd/Double.valueOf(salh.getSum2()));
			salh.setSum6(secToTime(sum6));
			
			String sum7=salh.getSum7();
			String sum2=salh.getSum2();
			
			if((!"0".equals(sum7))&&(!"0".equals(sum2))){
				salh.setSum8(""+(int)(Double.valueOf(sum7)*100/(Double.valueOf(sum2)))+"%");
			}else{
				salh.setSum8("0%");
			}
			list1.add(salh);
		}
		
		/**当前页面汇总行数据，及页面底部最后一行数据*/
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisLinkHostHourly salh1=new SourceAnalysisLinkHostHourly();
		salh1.setLink_host(str);
		salh1.setSum1(""+str1.intValue());
		salh1.setSum2(""+str2.intValue());
		salh1.setSum3(""+str3.intValue());
		salh1.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		salh1.setSum5(""+str5.intValue());
		salh1.setSum6(secToTime(str6.intValue()));
		salh1.setSum7(""+str7.intValue());
		salh1.setSum8(""+str8.intValue()+"%");
		list1.add(salh1);
		
		/**变量清零后,重新投入使用*/
		str1=0.0;
		str2=0.0;
		str3=0.0;
		str4=0.0;
		str5=0.0;
		str6=0.0;
		str7=0.0;
		str8=0.0;
		/**当前条件所有数据汇总,即页面中间列表数据*/
		List<SourceAnalysisLinkHostHourly> lists=sourceAnalysisLinkHostHourlyMapper.getPages_day(startTime, endTime);
		for(int i=0;i<lists.size();i++){
			SourceAnalysisLinkHostHourly sasw=lists.get(i);
			str1+=Double.valueOf(sasw.getSum1());
			str2+=Double.valueOf(sasw.getSum2());
			str3+=Double.valueOf(sasw.getSum3());
			str5+=Double.valueOf(sasw.getSum5());
			str7+=Double.valueOf(sasw.getSum7());
		}	
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisLinkHostHourly salhSummary=new SourceAnalysisLinkHostHourly();
		salhSummary.setSum1(""+str1.intValue());
		salhSummary.setSum2(""+str2.intValue());
		salhSummary.setSum3(""+str3.intValue());
		salhSummary.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		salhSummary.setSum5(""+str5.intValue());
		salhSummary.setSum6(secToTime(str6.intValue()));
		salhSummary.setSum7(""+str7.intValue());
		salhSummary.setSum8(""+str8.intValue()+"%");
		
		if(lists!=null){
			
			pl.setTotalRow(lists.size());
		}
		pl.setPageRow(list1);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		
		Map<String,Object> map =new HashMap();
		map.put("pl", pl);
		map.put("saseSummary", salhSummary);
		return map;
	}
	
	/**
	* @Title: SourceAnalysisLinkStatistics
	* @Description: TODO(外部链接统计数据，天为统计单位)
	* @param flag 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 16 Dis 2016 11:06:48 AM
	 */
	public Map<String, Object> SourceAnalysisLinkStatistics(
			String flag, String type,String date,String startTime, String endTime) {
		
		List<SourceAnalysisLinkHostHourly> list=new ArrayList();
		List<String> groupList=new ArrayList();
		Map<String,Object> map=new HashMap();
		List<String> dataList=new ArrayList();
		
		if("1".equals(type)||"2".equals(type)){/**组织饼图数据*/
			list=sourceAnalysisLinkHostHourlyMapper.getChart_day(type,startTime, endTime);
			Map<String,Object> map1=new HashMap();
			map1.put("list", list);
			map1=pieData(flag,map1);
			StringBuffer sb=(StringBuffer)map1.get("sb");
			groupList=(List<String>)map1.get("groupList");
			
			map.put("jsonArr", sb);//访问类型数据数组
		}/**如果分析指标即type选择为平均访问页数4、平均访问时长6、跳出率8时，组织条形图数据*/
		else if("6".equals(type)||"4".equals(type)){
			
			String type1="";
			String type2="2";
			if("4".equals(type)){
				type1="1";
			}else if("6".equals(type)){
				type1="5";
			}else if("8".equals(type)){
				type1="7";
			}
			String average=sourceAnalysisLinkHostHourlyMapper.average_day(type1,type2,startTime,endTime);
			list=sourceAnalysisLinkHostHourlyMapper.indexAverageList_day(type1, type2, startTime, endTime);
			for(int i=0;i<list.size();i++){
				dataList.add(""+Math.ceil(Double.valueOf(list.get(i).getSum())));
				groupList.add(list.get(i).getLink_host());
			}
			
			map.put("average",null!=average?Math.ceil(Double.valueOf(average)):"");
			map.put("dataList",dataList);
			map.put("xaverage", groupList.get(groupList.size()-1));
		}else if("8".equals(type)){
			list=sourceAnalysisLinkHostHourlyMapper.getPage_day(startTime, endTime,0,10);
			for(int i=0;i<list.size();i++){
				dataList.add(""+""+(int)(Double.valueOf(list.get(i).getSum7())*100/(Double.valueOf(list.get(i).getSum2()))));
				groupList.add(""+list.get(i).getLink_host());
			}
			map.put("dataList",dataList);
			
		}
		/**折线图组织数据*/
		List<String> dateList=sourceAnalysisLinkHostHourlyMapper.getDateList_day(type,startTime, endTime);
		StringBuffer seriesStr=lineData(dateList,groupList,flag,type,date,startTime,endTime);
		
		map.put("seriesArr", seriesStr);
		map.put("dateArr", dateList);
		map.put("groupArr", groupList.toString());
		
		return map;
		
	}

	
	/**
	* @Title: SourceAnalysisLinkHourlyList
	* @Description: TODO(外部链接列表数据，小时为统计单位)
	* @param date
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 16 Dis 2016 9:40:05 AM
	 */
	public Map<String,Object> SourceAnalysisLinkHourlyList(String date, Integer startTime,
			Integer endTime, Integer pageNum, Integer pageSize) {
		PageList pl=new PageList();
		
		/**放置页面底部列表汇总行数据*/
		String str="当前汇总";
		Double str1=0.0;
		Double str2=0.0;
		Double str3=0.0;
		Double str4=0.0;
		Double str5=0.0;
		Double str6=0.0;
		Double str7=0.0;
		Double str8=0.0;
		
		/**页面底部列表其他行数据*/
		int startNum = (pageNum-1)*pageSize;
		List<SourceAnalysisLinkHostHourly> list=sourceAnalysisLinkHostHourlyMapper.getPage(date, startTime, endTime, startNum, pageSize);
		List<Object> list1=new ArrayList();
		for(int i=0;i<list.size();i++){
			SourceAnalysisLinkHostHourly salh=list.get(i);
			
			str1+=Double.valueOf(salh.getSum1());
			str2+=Double.valueOf(salh.getSum2());
			str3+=Double.valueOf(salh.getSum3());
			str5+=Double.valueOf(salh.getSum5());
			str7+=Double.valueOf(salh.getSum7());
			
			salh.setSum4(Double.isInfinite(Double.valueOf(salh.getSum1())/Double.valueOf(salh.getSum2()))?"0":""+(int)(Double.valueOf(salh.getSum1())/Double.valueOf(salh.getSum2())));
			Double dd=Double.valueOf(salh.getSum5())*10000;
			int sum6=(int)(dd/Double.valueOf(salh.getSum2()));
			salh.setSum6(secToTime(sum6));
			
			String sum7=salh.getSum7();
			String sum2=salh.getSum2();
			
			if((!"0".equals(sum7))&&(!"0".equals(sum2))){
				salh.setSum8(""+(int)(Double.valueOf(sum7)*100/(Double.valueOf(sum2)))+"%");
			}else{
				salh.setSum8("0%");
			}
			list1.add(salh);
		}
		
		/**当前页面汇总行数据，即页面底部最后一行数据*/
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisLinkHostHourly sase1=new SourceAnalysisLinkHostHourly();
		sase1.setLink_host(""+str);
		sase1.setSum1(""+str1.intValue());
		sase1.setSum2(""+str2.intValue());
		sase1.setSum3(""+str3.intValue());
		sase1.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		sase1.setSum5(""+str5.intValue());
		sase1.setSum6(secToTime(str6.intValue()));
		sase1.setSum7(""+str7.intValue());
		sase1.setSum8(""+str8.intValue()+"%");
		list1.add(sase1);
		
		/**变量清零后,重新投入使用*/
		str1=0.0;
		str2=0.0;
		str3=0.0;
		str4=0.0;
		str5=0.0;
		str6=0.0;
		str7=0.0;
		str8=0.0;
		/**当前条件所有数据汇总,即页面中间列表数据*/
		List<SourceAnalysisLinkHostHourly> lists=sourceAnalysisLinkHostHourlyMapper.getPages(date,startTime, endTime);
		for(int i=0;i<lists.size();i++){
			SourceAnalysisLinkHostHourly sase=lists.get(i);
			str1+=Double.valueOf(sase.getSum1());
			str2+=Double.valueOf(sase.getSum2());
			str3+=Double.valueOf(sase.getSum3());
			str5+=Double.valueOf(sase.getSum5());
			str7+=Double.valueOf(sase.getSum7());
		}	
		str4=str1/str2;
		str6=str5*10000/str2;
		str8=str7*100/str2;
		SourceAnalysisLinkHostHourly saseSummary=new SourceAnalysisLinkHostHourly();
		saseSummary.setSum1(""+str1.intValue());
		saseSummary.setSum2(""+str2.intValue());
		saseSummary.setSum3(""+str3.intValue());
		saseSummary.setSum4(Double.isInfinite(str4)?"0":""+str4.intValue());
		saseSummary.setSum5(""+str5.intValue());
		saseSummary.setSum6(secToTime(str6.intValue()));
		saseSummary.setSum7(""+str7.intValue());
		saseSummary.setSum8(""+str8.intValue()+"%");
		
		if(lists!=null){
			
			pl.setTotalRow(lists.size());
		}
		pl.setPageRow(list1);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		
		Map<String,Object> map= new HashMap();
		map.put("pl", pl);
		map.put("saseSummary", saseSummary);
		
		return map;
	}
	
	/**
	* @Title: SourceAnalysisLinkHourlyStatistics
	* @Description: TODO(外部链接统计数据，小时为统计单位)
	* @param flag 
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 16 Dis 2016 11:06:48 AM
	 */
	public Map<String, Object> SourceAnalysisLinkHourlyStatistics(
			String flag, String type, String date, Integer startTime, Integer endTime) {
		
		List<SourceAnalysisLinkHostHourly> list=new ArrayList();
		List<String> groupList=new ArrayList();
		Map<String,Object> map=new HashMap();
		List<String> dataList=new ArrayList();
		
		if("1".equals(type)||"2".equals(type)){/**组织饼图数据*/
			list=sourceAnalysisLinkHostHourlyMapper.getChart(type, date, startTime, endTime);
			Map<String,Object> map1=new HashMap();
			map1.put("list", list);
			map1=pieData(flag,map1);
			StringBuffer sb=(StringBuffer)map1.get("sb");
			groupList=(List<String>)map1.get("groupList");
			map.put("jsonArr", sb);//访问类型数据数组
		}/**如果分析指标即type选择为平均访问页数4、平均访问时长6、跳出率8时，组织条形图数据*/
		else if("6".equals(type)||"4".equals(type)){
			
			String type1="";
			String type2="2";
			if("4".equals(type)){
				type1="1";
			}else if("6".equals(type)){
				type1="5";
			}else if("8".equals(type)){
				type1="7";
			}
			String average=sourceAnalysisLinkHostHourlyMapper.average(type1,type2,date,startTime,endTime);
			list=sourceAnalysisLinkHostHourlyMapper.indexAverageList(type1, type2,date,startTime, endTime);
			for(int i=0;i<list.size();i++){
				dataList.add(""+Math.ceil(Double.valueOf(list.get(i).getSum())));
				groupList.add(list.get(i).getLink_host());
			}
			
			map.put("average", null!=average?Math.ceil(Double.valueOf(average)):"");
			map.put("dataList",dataList);
			map.put("xaverage", groupList.get(groupList.size()-1));
		}else if("8".equals(type)){
			list=sourceAnalysisLinkHostHourlyMapper.getPage(date, startTime, endTime, 0, 10);
			
			for (int i = 0; i < list.size(); i++) {
				dataList.add(""+""+(int)(Double.valueOf(list.get(i).getSum7())*100/(Double.valueOf(list.get(i).getSum2()))));
				groupList.add(""+list.get(i).getLink_host());

			}
			
		}
		
		/**折线图组织数据*/
		List<String> dateList=sourceAnalysisLinkHostHourlyMapper.getDateList(type,date,startTime, endTime);
		StringBuffer seriesStr=lineData(dateList,groupList,flag,type,date,startTime.toString(),endTime.toString());
		
		map.put("seriesArr", seriesStr);
		map.put("dateArr", dateList);
		map.put("groupArr", groupList.toString());
		
		return map;
		
	}
	
	/**
	* @Title: secToTime
	* @Description: TODO(整数(秒数)转换为时分秒格式(xx:xx:xx)) 
	* @param time
	* @return String 返回类型 
	* @author LiuYong 
	* @date 20 Dis 2016 9:11:22 PM
	 */
	public String secToTime(int time) {  
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
	
	/**
	* @Title: unitFormat
	* @Description: TODO(时分秒是否加0设置) 
	* @param i
	* @return String 返回类型 
	* @author LiuYong 
	* @date 20 Dis 2016 9:09:21 PM
	 */
	public String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    } 
	
	/**
	* @Title: lineData
	* @Description: TODO(组织折线图数据) 
	* @param groupList
	* @param flag
	* @param type
	* @param date
	* @param startTime
	* @param endTime
	* @return StringBuffer 返回类型 
	* @author LiuYong 
	* @date 28 Dis 2016 5:39:13 PM
	 */
	public StringBuffer lineData(List<String> dateList,List<String> groupList,String flag,String type,String date,String startTime,String endTime){
		StringBuffer seriesStr=new StringBuffer(); 
		List<String> numbersList=null;
		List<Map<String, Object>> numbersListMap=null;
		int count1=0;
		seriesStr.append("[");
		
		if(groupList.size()>0){
			seriesStr.append("{");
		}
		
		for(int j=0;j< groupList.size();j++){
			List<String> numbersList1=new ArrayList();
			
			if("8".equals(type)){
				numbersListMap=new ArrayList<Map<String, Object>>();
				
				if("1".equals(flag)){
					numbersListMap=sourceAnalysisSearchEngineHourlyMapper.getLine1_day(groupList.get(j),type,startTime, endTime);
				}else if("2".equals(flag)){
					numbersListMap=sourceAnalysisSearchWordHourlyMapper.getLine1_day(groupList.get(j),type,startTime, endTime);
				}else if("3".equals(flag)){
					numbersListMap=sourceAnalysisSearchEngineHourlyMapper.getLine1_day(groupList.get(j),type,startTime, endTime);
				}else if("5".equals(flag)){
					numbersListMap=sourceAnalysisSearchEngineHourlyMapper.getLine1(groupList.get(j),type,date,Integer.valueOf(startTime), Integer.valueOf(endTime));
				}else if("6".equals(flag)){
					numbersListMap=sourceAnalysisSearchWordHourlyMapper.getLine1(groupList.get(j),type,date,Integer.valueOf(startTime), Integer.valueOf(endTime));
				}else if("7".equals(flag)){
					numbersListMap=sourceAnalysisLinkHostHourlyMapper.getLine1(groupList.get(j),type,date,Integer.valueOf(startTime), Integer.valueOf(endTime));
				}
				
				List<Map<String, Object>> temp=new ArrayList<Map<String, Object>>();
				Map<String, Object> map=null;
				for (int i = 0; i < dateList.size(); i++) {
					map=new HashMap<String, Object>();
					map.put("date", dateList.get(i));
					map.put("numbers","0%");
					temp.add(map);
				}
				
				for (int i = 0; i < temp.size(); i++) {
					for (int k = 0; k < numbersListMap.size(); k++) {
						 if(temp.get(i).get("date").equals(numbersListMap.get(k).get("date")+"")){
							 temp.get(i).put("numbers", numbersListMap.get(k).get("numbers"));
							 break;
						 }
					}
				}
				
				for(Map<String, Object> item:temp){
					numbersList1.add((""+Math.ceil(Double.valueOf(item.get("numbers").toString().replace("%", "")))).split("\\.")[0]);
				}
				seriesStr.append("name:\""+groupList.get(j)+"\",type:\"line\",data:"+numbersList1);
			}else{
				numbersList=new ArrayList();
				if("1".equals(flag)){
					numbersList=sourceAnalysisSearchEngineHourlyMapper.getLine_day(groupList.get(j),type,startTime, endTime);
				}else if("2".equals(flag)){
					numbersList=sourceAnalysisSearchWordHourlyMapper.getLine_day(groupList.get(j),type,startTime, endTime);
				}else if("3".equals(flag)){
					numbersList=sourceAnalysisLinkHostHourlyMapper.getLine_day(groupList.get(j),type,startTime, endTime);
				}else if("5".equals(flag)){
					numbersList=sourceAnalysisSearchEngineHourlyMapper.getLine(groupList.get(j),type,date,Integer.valueOf(startTime), Integer.valueOf(endTime));
				}else if("6".equals(flag)){
					numbersList=sourceAnalysisSearchWordHourlyMapper.getLine(groupList.get(j),type,date,Integer.valueOf(startTime), Integer.valueOf(endTime));
				}else if("7".equals(flag)){
					numbersList=sourceAnalysisLinkHostHourlyMapper.getLine(groupList.get(j),type,date,Integer.valueOf(startTime), Integer.valueOf(endTime));
				}
				
				
				for (int i = 0; i < 30; i++) {
					numbersList.add("0");
				}
				
				for(String item:numbersList){
					numbersList1.add((""+Math.ceil(Double.valueOf(item))).split("\\.")[0]);
				}
				seriesStr.append("name:\""+groupList.get(j)+"\",type:\"line\",data:"+numbersList1);
			}
			
			groupList.set(j,"\""+groupList.get(j)+"\"");
			if(count1==groupList.size()-1){
				seriesStr.append("}]");
			}else{
				seriesStr.append("},{");
			}
			
			count1++;
		}
		return seriesStr;
	}
	
	/**
	* @Title: pieData
	* @Description: TODO(饼图数据构建) 
	* @param flag
	* @param map
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 28 Dis 2016 7:07:08 PM
	 */
	public Map<String,Object> pieData(String flag,Map<String,Object> map){
		
		List<String>groupList =new ArrayList();
		Map<String,Object> map1=new HashMap();
		List<Object> list=(List<Object>) map.get("list");
		StringBuffer sb=new StringBuffer();
		int count=0;
		sb.append("[");
		
		if(list.size()>0){
			sb.append("{");
		}
		
		/**饼图计算其他剩余数据*/
		int rest=0;
		Double sum=0.0;
		Double sum10=0.0;
		for(int i=0;i<list.size();i++){
			if("1".equals(flag)){
				SourceAnalysisSearchEngineHourly sase=(SourceAnalysisSearchEngineHourly)list.get(i);
				sum+=Double.valueOf(sase.getSum());
			}else if("2".equals(flag)){
				SourceAnalysisSearchWordHourly sasw=(SourceAnalysisSearchWordHourly)list.get(i);
				sum+=Double.valueOf(sasw.getSum());
			}else if("3".equals(flag)){
				SourceAnalysisLinkHostHourly salh=(SourceAnalysisLinkHostHourly)list.get(i);
				sum+=Double.valueOf(salh.getSum());
			}else if("5".equals(flag)){
				SourceAnalysisSearchEngineHourly sase=(SourceAnalysisSearchEngineHourly)list.get(i);
				sum+=Double.valueOf(sase.getSum());
			}else if("6".equals(flag)){
				SourceAnalysisSearchWordHourly sasw=(SourceAnalysisSearchWordHourly)list.get(i);
				sum+=Double.valueOf(sasw.getSum());
			}else if("7".equals(flag)){
				SourceAnalysisLinkHostHourly salh=(SourceAnalysisLinkHostHourly)list.get(i);
				sum+=Double.valueOf(salh.getSum());
			}
		}
		
		/**饼图手动拼写json字符串*/
		int length=0;
		if(list!=null&&list.size()<=10){
			length=list.size();
		}else if(list!=null&&list.size()>10){
			length=10;
		}
		for(int i=0;i<length;i++){
			if("1".equals(flag)){
				SourceAnalysisSearchEngineHourly sase=(SourceAnalysisSearchEngineHourly)list.get(i);
				sum10+=Double.valueOf(sase.getSum());
				groupList.add(sase.getEngine_name());
				sb.append("value:"+sase.getSum()+",name:\""+sase.getEngine_name());
			}else if("2".equals(flag)){
				SourceAnalysisSearchWordHourly sasw=(SourceAnalysisSearchWordHourly)list.get(i);
				sum10+=Double.valueOf(sasw.getSum());
				groupList.add(sasw.getKeyWord());
				sb.append("value:"+sasw.getSum()+",name:\""+sasw.getKeyWord());
			}else if("3".equals(flag)){
				SourceAnalysisLinkHostHourly salh=(SourceAnalysisLinkHostHourly)list.get(i);
				sum10+=Double.valueOf(salh.getSum());
				groupList.add(salh.getLink_host());
				sb.append("value:"+salh.getSum()+",name:\""+salh.getLink_host());
			}else if("5".equals(flag)){
				SourceAnalysisSearchEngineHourly sase=(SourceAnalysisSearchEngineHourly)list.get(i);
				sum10+=Double.valueOf(sase.getSum());
				groupList.add(sase.getEngine_name());
				sb.append("value:"+sase.getSum()+",name:\""+sase.getEngine_name());
			}else if("6".equals(flag)){
				SourceAnalysisSearchWordHourly sasw=(SourceAnalysisSearchWordHourly)list.get(i);
				sum10+=Double.valueOf(sasw.getSum());
				groupList.add(sasw.getKeyWord());
				sb.append("value:"+sasw.getSum()+",name:\""+sasw.getKeyWord());
			}else if("7".equals(flag)){
				SourceAnalysisLinkHostHourly salh=(SourceAnalysisLinkHostHourly)list.get(i);
				sum10+=Double.valueOf(salh.getSum());
				groupList.add(salh.getLink_host());
				sb.append("value:"+salh.getSum()+",name:\""+salh.getLink_host());
			}
			
			if(count==length-1){
				rest=(int)(sum-sum10);//前十外剩余数据量
				sb.append("\"},{value:"+rest+",name:\"其他");
				sb.append("\"}");
			}else{
				sb.append("\"},{");
			}
			count++;
			
		}
		sb.append("]");
		
		map1.put("groupList", groupList);
		map1.put("sb", sb);
		
		return map1;
	}
}

	



