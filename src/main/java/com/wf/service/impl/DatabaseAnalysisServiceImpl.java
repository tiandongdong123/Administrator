package com.wf.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.wf.dao.DatamanagerMapper;
import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.DataUtil;
import com.wf.bean.DatabaseUseDaily;
import com.wf.bean.DatabaseUseHourly;
import com.wf.bean.PageList;
import com.wf.dao.DatabaseUseDailyMapper;
import com.wf.dao.DatabaseUseHourlyMapper;
import com.wf.dao.PersonMapper;
import com.wf.service.DatabaseAnalysisService;
@Service
public class DatabaseAnalysisServiceImpl implements DatabaseAnalysisService {

	@Autowired
	private DatabaseUseHourlyMapper databaseUseHourlyMapper;
	@Autowired
	private PersonMapper personMapper;
	@Autowired
	private DatamanagerMapper datamanagerMapper;
	
	@Override
	public PageList getDatabaseAnalysisList(DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer pageNum,Integer pageSize){
		PageList pageList=new PageList();
		List<Object> PageList=new ArrayList<Object>();
		int startNum = (pageNum-1)*pageSize;
		List<Object> list=new ArrayList<Object>();
		String institutionName=databaseUseDaily.getInstitution_name();
		String userId=databaseUseDaily.getUser_id();
		String date=databaseUseDaily.getDate();
		String product_source_code=databaseUseDaily.getProduct_source_code();
		String source_db=databaseUseDaily.getSource_db();

		if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
			PageList=databaseUseHourlyMapper.getData(product_source_code,source_db,date, startTime, endTime, startNum, pageSize);
			list=databaseUseHourlyMapper.getDataAnalysisLists(product_source_code,source_db,date, startTime, endTime);
		}else if(StringUtils.isNotBlank(userId)){
			PageList=databaseUseHourlyMapper.getDataById(userId,product_source_code,source_db,date, startTime, endTime, startNum, pageSize);
			list=databaseUseHourlyMapper.getDataAnalysisListsById(userId,product_source_code,source_db,date, startTime, endTime);
		}else{
			List users = personMapper.getInstitutionUser(institutionName);
			PageList=databaseUseHourlyMapper.getDataByIds(institutionName,users,product_source_code,source_db,date, startTime, endTime, startNum, pageSize);
			list=databaseUseHourlyMapper.getDataAnalysisListsByIds(institutionName,users,product_source_code,source_db,date, startTime, endTime);
		}
		pageList.setTotalRow(list.size());
		pageList.setPageRow(PageList);
		pageList.setPageNum(pageNum);
		pageList.setPageSize(pageSize);
		return pageList;
	}
	
	@Override
	public Map<String, Object> DatabaseAnalysisStatistics(
			DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer[] urlType,String[] database_name) {
		Map<String,Object> map=new HashMap();
		List<DatabaseUseHourly> list=new ArrayList<DatabaseUseHourly>();
		Map<String, List<String>> content = new HashMap<String, List<String>>();
		List<String>timeList=new ArrayList();
		List<String>browseList=new ArrayList();
		List<String>downloadList=new ArrayList();
		List<String>searchList=new ArrayList();

		String institutionName=databaseUseDaily.getInstitution_name();
		String userId=databaseUseDaily.getUser_id();
		String date=databaseUseDaily.getDate();
		String product_source_code=databaseUseDaily.getProduct_source_code();
		String source_db=databaseUseDaily.getSource_db();

		if(database_name==null){
			if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
				list=databaseUseHourlyMapper.DatabaseAnalysisStatistics(product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}else if(StringUtils.isNotBlank(userId)){
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsById(userId,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}else{
				List users = personMapper.getInstitutionUser(institutionName);
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsByIds(institutionName,users,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}
			if(date!=null&&!"".equals(date)){
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				for(int i = 0;i<24;i++){
					for (DatabaseUseHourly item : list) {
						if(Integer.parseInt(item.getHour())==i+1){
							searchList.add(item.getSum1());
							browseList.add(item.getSum2());
							downloadList.add(item.getSum3());
							break;
						}
					}
					if(searchList.size()==i){
						searchList.add("0");
						browseList.add("0");
						downloadList.add("0");
					}
				}
			}else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date starttime = format.parse(startTime);
					Date endtime = format.parse(endTime);
					List<Date> days = this.getDate(starttime, endtime);
					for(Date d : days){
						timeList.add(format.format(d));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

				for(int i = 0;i<timeList.size();i++){
					for (DatabaseUseHourly item : list) {
						if(timeList.get(i).equals(item.getDate())){
							searchList.add(item.getSum1());
							browseList.add(item.getSum2());
							downloadList.add(item.getSum3());
							break;
						}
					}
					if(searchList.size()==i){
						searchList.add("0");
						browseList.add("0");
						downloadList.add("0");
					}

				}
			}
			content.put("浏览数",browseList);
			content.put("下载数",downloadList);
			content.put("检索数",searchList);

			map.put("timeArr", timeList.toArray());
			map.put("content", content);
		}else {
			if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsMore(product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}else if(StringUtils.isNotBlank(userId)){
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsByIdMore(userId,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}else{
				List users = personMapper.getInstitutionUser(institutionName);
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsByIdsMore(institutionName,users,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}
			List<String> databaseName = datamanagerMapper.getdatabseName(database_name);
			if(date!=null&&!"".equals(date)){
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				for(int j = 0;j<databaseName.size();j++){
					List arrayList = new ArrayList<>();
					if(urlType[0]==1){
					for(int i = 0;i<24;i++){
						for (DatabaseUseHourly item : list) {
							if(databaseName.get(j).equals(item.getDatabase_name())&&Integer.parseInt(item.getHour())==i+1){
								arrayList.add(item.getSum2());
								break;
								}
							}
						if(arrayList.size()==i){
							arrayList.add("0");
						}
						}
					content.put(databaseName.get(j),arrayList);
					}
					if(urlType[0]==2){
						for(int i = 0;i<24;i++){
							for (DatabaseUseHourly item : list) {
								if(databaseName.get(j).equals(item.getDatabase_name())&&Integer.parseInt(item.getHour())==i+1){
									arrayList.add(item.getSum3());
									break;
								}
							}
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databaseName.get(j),arrayList);
					}
					if(urlType[0]==3){
						for(int i = 0;i<24;i++){
							for (DatabaseUseHourly item : list) {
								if(databaseName.get(j).equals(item.getDatabase_name())&&Integer.parseInt(item.getHour())==i+1){
									arrayList.add(item.getSum1());
									break;
								}
							}
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databaseName.get(j),arrayList);
					}
				}
			}else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date starttime = format.parse(startTime);
					Date endtime = format.parse(endTime);
					List<Date> days = this.getDate(starttime, endtime);
					for(Date d : days){
						timeList.add(format.format(d));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

				for(int j = 0;j<databaseName.size();j++){
					List arrayList = new ArrayList<>();
					if(urlType[0]==1){
						for(int i = 0;i<timeList.size();i++){
							for (DatabaseUseHourly item : list) {
								if(databaseName.get(j).equals(item.getDatabase_name())&&timeList.get(i).equals(item.getDate())){
									arrayList.add(item.getSum2());
									break;
								}
							}
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databaseName.get(j),arrayList);
					}
					if(urlType[0]==2){
						for(int i = 0;i<timeList.size();i++){
							for (DatabaseUseHourly item : list) {
								if(databaseName.get(j).equals(item.getDatabase_name())&&timeList.get(i).equals(item.getDate())){
									arrayList.add(item.getSum3());
									break;
								}
							}
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databaseName.get(j),arrayList);
					}
					if(urlType[0]==3){
						for(int i = 0;i<timeList.size();i++){
							for (DatabaseUseHourly item : list) {
								if(databaseName.get(j).equals(item.getDatabase_name())&&timeList.get(i).equals(item.getDate())){
									arrayList.add(item.getSum1());
									break;
								}
							}
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databaseName.get(j),arrayList);
					}
				}
			}

			map.put("title",databaseName.toArray());
			map.put("timeArr", timeList.toArray());
			map.put("content", content);
		}

		return map;

	};


	@Override
	public List<Map<String, String>> exportDatabase(DatabaseUseDaily databaseUseDaily,
			String startTime, String endTime) {
		List<Map<String, String>> listmap=new ArrayList<Map<String,String>>();
		if(StringUtils.isBlank(databaseUseDaily.getInstitution_name()) && StringUtils.isBlank(databaseUseDaily.getUser_id())){
			listmap=databaseUseHourlyMapper.exportDatabaseOneDay(databaseUseDaily, startTime, endTime);
		}else if(StringUtils.isNotBlank(databaseUseDaily.getUser_id())){
			listmap=databaseUseHourlyMapper.exportDatabaseOneDayById(databaseUseDaily, startTime, endTime);
		}else{
			List users = personMapper.getInstitutionUser(databaseUseDaily.getInstitution_name());
			listmap=databaseUseHourlyMapper.exportDatabaseOneDayByIds(databaseUseDaily,users, startTime, endTime);
		}
		return listmap;
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
}
