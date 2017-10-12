package com.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private DatabaseUseDailyMapper databaseUseDailyMapper;
	@Autowired
	private DatabaseUseHourlyMapper databaseUseHourlyMapper;
	@Autowired
	private PersonMapper personMapper;
	
	@Override
	public PageList getDatabaseAnalysisList(DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer pageNum,Integer pageSize){
		
		PageList pl=new PageList();
		

		if(!"".equals(databaseUseDaily.getDate())&&databaseUseDaily.getDate()!=null){
			pl=databaseUseHourly(databaseUseDaily,startTime,endTime,pageNum,pageSize);
		}else{
			pl=databaseUseDaily(databaseUseDaily,startTime,endTime,pageNum,pageSize);
		}
		
		return pl;
	}
	
	@Override
	public Map<String, Object> DatabaseAnalysisStatistics(
			DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer[]urlType,String[]database_name) {
		Map<String,Object> map=new HashMap();
		
		if(!"".equals(databaseUseDaily.getDate())&&databaseUseDaily.getDate()!=null){
			String institutionName=databaseUseDaily.getInstitution_name();
			String userName=databaseUseDaily.getUser_id();
			String date=databaseUseDaily.getDate();
			map=DatabaseUseHourlyStatistics(institutionName,userName,date,startTime,endTime,urlType,database_name);
		}else{
			map=DatabaseUseDailyStatistics(databaseUseDaily,startTime,endTime,urlType,database_name);
		}
		
		return map;
	};
	
	/**
	* @Title: databaseUseDaily
	* @Description: TODO(到天表中取页面列表数据) 
	* @param databaseUseDaily
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return PageList 返回类型 
	* @author LiuYong 
	* @date 26 Dis 2016 9:32:45 AM
	 */
	public PageList databaseUseDaily(DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer pageNum,Integer pageSize){
		PageList pl=new PageList();
		
		int startNum = (pageNum-1)*pageSize;
		String institutionName=databaseUseDaily.getInstitution_name();
		String userId=databaseUseDaily.getUser_id();
		List<Object> PageList=new ArrayList<Object>();
		List<Object> list=new ArrayList<Object>();
		if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
			PageList=databaseUseDailyMapper.getDatabaseAnalysisList(databaseUseDaily, startTime, endTime, startNum, pageSize);
			list=databaseUseDailyMapper.getDatabaseAnalysisLists(databaseUseDaily, startTime, endTime);
		}else if(StringUtils.isBlank(institutionName) && StringUtils.isNotBlank(userId)){
			String userType=personMapper.getUserTypeByUserId(userId);
			if(StringUtils.isNoneBlank(userType)){
				//userType等于0为个人用户
				if(userType.equals("0")){
					PageList=databaseUseDailyMapper.getDatabaseAnalysisList(databaseUseDaily, startTime, endTime, startNum, pageSize);
					list=databaseUseDailyMapper.getDatabaseAnalysisLists(databaseUseDaily, startTime, endTime);
				}else{
					PageList=databaseUseDailyMapper.getDatabaseAnalysisListIsInstitution(databaseUseDaily, startTime, endTime, startNum, pageSize);
					list=databaseUseDailyMapper.getDatabaseAnalysisListsIsInstitution(databaseUseDaily, startTime, endTime);
				}
			}
		}else{
			PageList=databaseUseDailyMapper.getDatabaseAnalysisListIsInstitution(databaseUseDaily, startTime, endTime, startNum, pageSize);
			list=databaseUseDailyMapper.getDatabaseAnalysisListsIsInstitution(databaseUseDaily, startTime, endTime);
		}
		
		
		

		pl.setTotalRow(list.size());
		pl.setPageRow(PageList);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		return pl;
	}
	
	/**
	* @Title: databaseUseHourly
	* @Description: TODO(到小时表中取页面列表数据) 
	* @param databaseUseDaily
	* @param startTime
	* @param endTime
	* @param pageNum
	* @param pageSize
	* @return PageList 返回类型 
	* @author LiuYong 
	* @date 26 Dis 2016 9:34:00 AM
	 */
	public PageList databaseUseHourly(DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer pageNum,Integer pageSize){
		PageList pl=new PageList();
		List<Object> PageList=new ArrayList<Object>();
		List<Object> list=new ArrayList<Object>();
		
		int startNum = (pageNum-1)*pageSize;
		String institutionName=databaseUseDaily.getInstitution_name();
		String userId=databaseUseDaily.getUser_id();
		String date=databaseUseDaily.getDate();
		
		if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
			PageList=databaseUseHourlyMapper.getDatabaseAnalysisList(institutionName,userId,date, startTime, endTime, startNum, pageSize);
			list=databaseUseHourlyMapper.getDatabaseAnalysisLists(institutionName,userId,date, startTime, endTime);
		}else if(StringUtils.isBlank(institutionName) && StringUtils.isNotBlank(userId)){
			String userType=personMapper.getUserTypeByUserId(userId);
			if(StringUtils.isNoneBlank(userType)){
				//userType等于0为个人用户
				if(userType.equals("0")){
					PageList=databaseUseHourlyMapper.getDatabaseAnalysisList(institutionName,userId,date, startTime, endTime, startNum, pageSize);
					list=databaseUseHourlyMapper.getDatabaseAnalysisLists(institutionName,userId,date, startTime, endTime);
				}else{
					PageList=databaseUseHourlyMapper.getDatabaseAnalysisListIsInstitution(institutionName, userId, date, startTime, endTime, pageNum, pageSize);
					list=databaseUseHourlyMapper.getDatabaseAnalysisListsIsInstitution(institutionName, userId, date, startTime, endTime);
				}
			}
		}else{
			PageList=databaseUseHourlyMapper.getDatabaseAnalysisListIsInstitution(institutionName, userId, date, startTime, endTime, pageNum, pageSize);
			list=databaseUseHourlyMapper.getDatabaseAnalysisListsIsInstitution(institutionName, userId, date, startTime, endTime);
		}

		pl.setTotalRow(list.size());
		pl.setPageRow(PageList);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		return pl;
	}

	/**
	* @Title: DatabaseUseDailyStatistics
	* @Description: TODO(天表中取折线图统计数据) 
	* @param databaseUseDaily
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 26 Dis 2016 10:05:17 AM
	 */
	public Map<String, Object> DatabaseUseDailyStatistics(DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer[]urlType,String[]database_name){
		
		List<DatabaseUseDaily> list=new ArrayList<DatabaseUseDaily>();
		List<String> groupList=new ArrayList<String>();
		List<String>timeList=new ArrayList();
		List<String>browseList=new ArrayList();
		List<String>downloadList=new ArrayList();
		List<String>searchList=new ArrayList();
		
		
		String institutionName=databaseUseDaily.getInstitution_name();
		String userId=databaseUseDaily.getUser_id();
		
		if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
			list=databaseUseDailyMapper.databaseAnalysisStatistics(databaseUseDaily, startTime, endTime,urlType,database_name);
			groupList=databaseUseDailyMapper.getGroupList(databaseUseDaily, startTime, endTime,urlType,database_name);
		}else if(StringUtils.isBlank(institutionName) && StringUtils.isNotBlank(userId)){
			
			String userType=personMapper.getUserTypeByUserId(userId);
			if(StringUtils.isNoneBlank(userType)){
				//userType等于0为个人用户
				if(userType.equals("0")){
					list=databaseUseDailyMapper.databaseAnalysisStatistics(databaseUseDaily, startTime, endTime,urlType,database_name);
					groupList=databaseUseDailyMapper.getGroupList(databaseUseDaily, startTime, endTime,urlType,database_name);
				}else{
					list=databaseUseDailyMapper.databaseAnalysisStatisticsIsInstitution(databaseUseDaily, startTime, endTime,urlType,database_name);
					groupList=databaseUseDailyMapper.getGroupListIsInstitution(databaseUseDaily, startTime, endTime,urlType,database_name);
				}
			}
		}else{
			list=databaseUseDailyMapper.databaseAnalysisStatisticsIsInstitution(databaseUseDaily, startTime, endTime,urlType,database_name);
			groupList=databaseUseDailyMapper.getGroupListIsInstitution(databaseUseDaily, startTime, endTime,urlType,database_name);
		}
		
		int size=7;
		if(groupList!=null){
			size=groupList.size();
		}
		
		for (DatabaseUseDaily item : list) {
			timeList.add(item.getDate());
			searchList.add(item.getSum1());
			browseList.add(item.getSum2());
			downloadList.add(item.getSum3());
			
		}

		
		Map<String,Object> map=new HashMap();
		map.put("timeArr", timeList.toArray());
		map.put("browseArr", browseList.toArray());//浏览数
		map.put("downloadArr", downloadList.toArray());//下载数
		map.put("searchArr", searchList.toArray());//检索数
		
		
		return map;
		
	}
	
	/**
	* @Title: DatabaseUseHourlyStatistics
	* @Description: TODO(小时表中取折线图统计数据) 
	* @param institutionName
	* @param userId
	* @param date
	* @param startTime
	* @param endTime
	* @return Map<String,Object> 返回类型 
	* @author LiuYong 
	* @date 26 Dis 2016 10:05:54 AM
	 */
	public Map<String, Object> DatabaseUseHourlyStatistics(String institutionName,String userId,String date,String startTime,String endTime,Integer[]urlType,String[]database_name){
		
		List<DatabaseUseHourly> list=new ArrayList<DatabaseUseHourly>();
		List<String> groupList=new ArrayList<String>();
		
		List<String>timeList=new ArrayList();
		List<String>browseList=new ArrayList();
		List<String>downloadList=new ArrayList();
		List<String>searchList=new ArrayList();
		
		if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
			list=databaseUseHourlyMapper.DatabaseAnalysisStatistics(institutionName,userId,date, startTime, endTime,urlType,database_name);
			groupList=databaseUseHourlyMapper.getGroupList(institutionName,userId,date, startTime, endTime,urlType,database_name);
		}else if(StringUtils.isBlank(institutionName) && StringUtils.isNotBlank(userId)){
			
			String userType=personMapper.getUserTypeByUserId(userId);
			if(StringUtils.isNoneBlank(userType)){
				//userType等于0为个人用户
				if(userType.equals("0")){
					list=databaseUseHourlyMapper.DatabaseAnalysisStatistics(institutionName,userId,date, startTime, endTime,urlType,database_name);
					groupList=databaseUseHourlyMapper.getGroupList(institutionName,userId,date, startTime, endTime,urlType,database_name);
				}else{
					list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsIsInstitution(institutionName,userId,date, startTime, endTime,urlType,database_name);
					groupList=databaseUseHourlyMapper.getGroupListIsInstitution(institutionName,userId,date, startTime, endTime,urlType,database_name);
				}
			}
		}else{
			list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsIsInstitution(institutionName,userId,date, startTime, endTime,urlType,database_name);
			groupList=databaseUseHourlyMapper.getGroupListIsInstitution(institutionName,userId,date, startTime, endTime,urlType,database_name);
		}
		
		int size=7;
		if(groupList!=null){
			size=groupList.size();
		}
		
		
		for (DatabaseUseHourly item : list) {
			timeList.add(item.getHour());
			searchList.add(item.getSum1());
			browseList.add(item.getSum2());
			downloadList.add(item.getSum3());
			
		}
		
		Map<String,Object> map=new HashMap();
		map.put("timeArr", timeList.toArray());
		map.put("browseArr", browseList.toArray());//浏览数
		map.put("downloadArr", downloadList.toArray());//下载数
		map.put("searchArr", searchList.toArray());//检索数
		
		
		return map;
		
	}

	@Override
	public List<Map<String, String>> exportDatabase(DatabaseUseDaily databaseUseDaily,
			String startTime, String endTime) {
		
		String institutionName=databaseUseDaily.getInstitution_name();
		String userId=databaseUseDaily.getUser_id();
		
		List<Map<String, String>> listmap=new ArrayList<Map<String,String>>();
		
		try {
			if(!"".equals(databaseUseDaily.getDate())&&databaseUseDaily.getDate()!=null){
				
				if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
					listmap=databaseUseHourlyMapper.exportDatabaseOneDay(databaseUseDaily, startTime, endTime);
				}else if(StringUtils.isBlank(institutionName) && StringUtils.isNotBlank(userId)){
					
					String userType=personMapper.getUserTypeByUserId(userId);
					if(StringUtils.isNoneBlank(userType)){
						//userType等于0为个人用户
						if(userType.equals("0")){
							listmap=databaseUseHourlyMapper.exportDatabaseOneDay(databaseUseDaily, startTime, endTime);
						}else{
							listmap=databaseUseHourlyMapper.exportDatabaseOneDayIsInstitution(databaseUseDaily, startTime, endTime);
						}
					}
				}else{
					listmap=databaseUseHourlyMapper.exportDatabaseOneDayIsInstitution(databaseUseDaily, startTime, endTime);
				}
			}else{
				
				if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
					listmap=databaseUseDailyMapper.exportDatabaseByDay(databaseUseDaily, startTime, endTime);
				}else if(StringUtils.isBlank(institutionName) && StringUtils.isNotBlank(userId)){
					
					String userType=personMapper.getUserTypeByUserId(userId);
					if(StringUtils.isNoneBlank(userType)){
						//userType等于0为个人用户
						if(userType.equals("0")){
							listmap=databaseUseDailyMapper.exportDatabaseByDay(databaseUseDaily, startTime, endTime);
						}else{
							listmap=databaseUseDailyMapper.exportDatabaseByDayIsInstitution(databaseUseDaily, startTime, endTime);
						}
					}
				}else{
					listmap=databaseUseDailyMapper.exportDatabaseByDayIsInstitution(databaseUseDaily, startTime, endTime);
				}
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return listmap;
		
	}
	
	
}
