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
	private DatabaseUseHourlyMapper databaseUseHourlyMapper;
	@Autowired
	private PersonMapper personMapper;
	
	@Override
	public PageList getDatabaseAnalysisList(DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer pageNum,Integer pageSize){
		PageList pl=new PageList();
		List<Object> PageList=new ArrayList<Object>();
		int startNum = (pageNum-1)*pageSize;
		List<Object> list=new ArrayList<Object>();
		String institutionName=databaseUseDaily.getInstitution_name();
		String userId=databaseUseDaily.getUser_id();
		String date=databaseUseDaily.getDate();
		String product_source_code=databaseUseDaily.getProduct_source_code();
		String source_db=databaseUseDaily.getSource_db();
		PageList=databaseUseHourlyMapper.getData(institutionName,userId,product_source_code,source_db,date, startTime, endTime, startNum, pageSize);
		list=databaseUseHourlyMapper.getDatabaseAnalysisLists(institutionName,userId,product_source_code,source_db,date, startTime, endTime);
		pl.setTotalRow(list.size());
		pl.setPageRow(PageList);
		pl.setPageNum(pageNum);
		pl.setPageSize(pageSize);
		return pl;
	}
	
	@Override
	public Map<String, Object> DatabaseAnalysisStatistics(
			DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer[]urlType,String[]database_name) {
		Map<String,Object> map=new HashMap();
		List<DatabaseUseHourly> list=new ArrayList<DatabaseUseHourly>();
		List<String>timeList=new ArrayList();
		List<String>browseList=new ArrayList();
		List<String>downloadList=new ArrayList();
		List<String>searchList=new ArrayList();

		String institutionName=databaseUseDaily.getInstitution_name();
		String userId=databaseUseDaily.getUser_id();
		String date=databaseUseDaily.getDate();
		String product_source_code=databaseUseDaily.getProduct_source_code();
		String source_db=databaseUseDaily.getSource_db();

		list=databaseUseHourlyMapper.DatabaseAnalysisStatistics(institutionName,userId,product_source_code,source_db,date, startTime, endTime,urlType,database_name);

		if(date!=null&&!"".equals(date)){
			for (DatabaseUseHourly item : list) {
				timeList.add(item.getHour());
				searchList.add(item.getSum1());
				browseList.add(item.getSum2());
				downloadList.add(item.getSum3());
			}
		}else {
			for (DatabaseUseHourly item : list) {
				timeList.add(item.getDate());
				searchList.add(item.getSum1());
				browseList.add(item.getSum2());
				downloadList.add(item.getSum3());

			}
		}

		map.put("timeArr", timeList.toArray());
		map.put("browseArr", browseList.toArray());//浏览数
		map.put("downloadArr", downloadList.toArray());//下载数
		map.put("searchArr", searchList.toArray());//检索数


		return map;

	};


	@Override
	public List<Map<String, String>> exportDatabase(DatabaseUseDaily databaseUseDaily,
			String startTime, String endTime) {
		List<Map<String, String>> listmap=new ArrayList<Map<String,String>>();
		listmap=databaseUseHourlyMapper.exportDatabaseOneDay(databaseUseDaily, startTime, endTime);
		return listmap;
	}
}
