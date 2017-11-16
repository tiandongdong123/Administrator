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
		int startNum = (pageNum-1)*pageSize;//sql中分页使用，开始的条数（limit）
		List<Object> list=new ArrayList<Object>();
		String institutionName=databaseUseDaily.getInstitution_name();//机构名称
		String userId=databaseUseDaily.getUser_id();//用户ID
		String date=databaseUseDaily.getDate();//日期（按小时查询用到）
		String product_source_code=databaseUseDaily.getProduct_source_code();//数据库code
		String source_db=databaseUseDaily.getSource_db();//数据来源
		/**
		 * 分三种情况：
		 * 1.机构名称和用户ID都为空时
		 * 2.用户ID不为空时
		 * 3.机构名称不为空（用户ID要为空）
		 */
		if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
			//按查询条件得到表格中的分页后数据
			PageList=databaseUseHourlyMapper.getData(product_source_code,source_db,date, startTime, endTime, startNum, pageSize);
			//按查询条件得到表格中的所有数据
			list=databaseUseHourlyMapper.getDataAnalysisLists(product_source_code,source_db,date, startTime, endTime);
		}else if(StringUtils.isNotBlank(userId)){
			//按查询条件得到表格中的分页后数据
			PageList=databaseUseHourlyMapper.getDataById(userId,product_source_code,source_db,date, startTime, endTime, startNum, pageSize);
			//按查询条件得到表格中的所有数据
			list=databaseUseHourlyMapper.getDataAnalysisListsById(userId,product_source_code,source_db,date, startTime, endTime);
		}else{
			//得到此机构中所有的用户（包括账号和其子账号）
			List users = personMapper.getInstitutionUser(institutionName);
			//按查询条件得到表格中的分页后数据
			PageList=databaseUseHourlyMapper.getDataByIds(institutionName,users,product_source_code,source_db,date, startTime, endTime, startNum, pageSize);
			//按查询条件得到表格中的所有数据
			list=databaseUseHourlyMapper.getDataAnalysisListsByIds(institutionName,users,product_source_code,source_db,date, startTime, endTime);
		}
		pageList.setTotalRow(list.size());//每页显示的数量
		pageList.setPageRow(PageList);//查询结果表
		pageList.setPageNum(pageNum);//当前页
		pageList.setPageSize(pageSize);//每页显示的数量
		return pageList;
	}
	
	@Override
	public Map<String, Object> DatabaseAnalysisStatistics(
			DatabaseUseDaily databaseUseDaily,String startTime,String endTime,Integer[] urlType,String[] database_name) {
		Map<String,Object> map=new HashMap();
		List<DatabaseUseHourly> list=new ArrayList<DatabaseUseHourly>();
		Map<String, List<String>> content = new HashMap<String, List<String>>();
		List<String>timeList=new ArrayList();//时间集合（按小时：1-24 按天：每一天的日期）
		List<String>browseList=new ArrayList();//浏览数集合
		List<String>downloadList=new ArrayList();//下载数集合
		List<String>searchList=new ArrayList();//检索数集合
		List<String> titleList = new ArrayList<>();//折线图标题集合
		List<String> databases = new ArrayList<>();//资源类型集合

		String institutionName=databaseUseDaily.getInstitution_name();//机构名称
		String userId=databaseUseDaily.getUser_id();//用户ID
		String date=databaseUseDaily.getDate();//日期（按小时查询用到）
		String product_source_code=databaseUseDaily.getProduct_source_code();//数据库code
		String source_db=databaseUseDaily.getSource_db();//数据来源
		//根据分析指标数判断
		//当分析指标数组长度小于等于1，按分析指标展示
		//当分析指标数组长度大于1，按数据库名称展示
		if(database_name.length<=1){
			/**
			 * 分三种情况：
			 * 1.机构名称和用户ID都为空时
			 * 2.用户ID不为空时
			 * 3.机构名称不为空（用户ID要为空）
			 */
			if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
				//按条件查询
				list=databaseUseHourlyMapper.DatabaseAnalysisStatistics(product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}else if(StringUtils.isNotBlank(userId)){
				//按条件查询
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsById(userId,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}else{
				//查处属于此机构的所有用户（包括机构账号和机构子账号）
				List users = personMapper.getInstitutionUser(institutionName);
				//按条件查询
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsByIds(institutionName,users,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}
			//按小时显示
			if(date!=null&&!"".equals(date)){
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				//设置每个时间点的各项指标数
				for(int i = 0;i<24;i++){
					for (DatabaseUseHourly item : list) {
						if(Integer.parseInt(item.getHour())==i+1){
							searchList.add(item.getSum1());
							browseList.add(item.getSum2());
							downloadList.add(item.getSum3());
							break;
						}
					}
					//如果此时间点没有数据，设为0
					if(searchList.size()==i){
						searchList.add("0");
						browseList.add("0");
						downloadList.add("0");
					}
				}
			}else {
				//按天统计
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date starttime = format.parse(startTime);
					Date endtime = format.parse(endTime);
					List<Date> days = this.getDate(starttime, endtime);
					//获取统计时间内的每天
					for(Date d : days){
						timeList.add(format.format(d));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//设置每一天的各项指标数
				for(int i = 0;i<timeList.size();i++){
					for (DatabaseUseHourly item : list) {
						if(timeList.get(i).equals(item.getDate())){
							searchList.add(item.getSum1());
							browseList.add(item.getSum2());
							downloadList.add(item.getSum3());
							break;
						}
					}
					//若当天没有数据，则设为0
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
			/**
			 * 分三种情况：
			 * 1.机构名称和用户ID都为空时
			 * 2.用户ID不为空时
			 * 3.机构名称不为空（用户ID要为空）
			 */
			if(StringUtils.isBlank(institutionName) && StringUtils.isBlank(userId)){
				//按条件查询
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsMore(product_source_code,source_db,date, startTime, endTime,urlType,database_name);
				titleList = databaseUseHourlyMapper.getTtitle(product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}else if(StringUtils.isNotBlank(userId)){
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsByIdMore(userId,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
				titleList = databaseUseHourlyMapper.getTtitleById(userId,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}else{
				//查处属于此机构的所有用户（包括机构账号和机构子账号）
				List users = personMapper.getInstitutionUser(institutionName);
				list=databaseUseHourlyMapper.DatabaseAnalysisStatisticsByIdsMore(institutionName,users,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
				titleList = databaseUseHourlyMapper.getTtitleByIds(institutionName,users,product_source_code,source_db,date, startTime, endTime,urlType,database_name);
			}
			List<String> databaseName = datamanagerMapper.getdatabseName(database_name);
			if(date!=null&&!"".equals(date)){
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				//得到标题
				for(String item : titleList){
					databases.add(item);
				}
				/**
				 * 此处有3层for循环（业务需求）
				 * 数据量特别大时本不合理，但通过控制，虽有表面是3层循环，但实际上只相当于2层
				 */
				for(int j = 0;j<databases.size();j++){
					List arrayList = new ArrayList<>();
					//通过分析指标的值来判断需要哪个sum
					if(urlType[0]==1){
						//得到每个小时的值
						for(int i = 0;i<24;i++){
							//遍历list集合
							for (DatabaseUseHourly item : list) {
								//判断是否是当前标题和时间点
								if(databases.get(j).equals(item.getDatabase_name())&&Integer.parseInt(item.getHour())==i+1){
									arrayList.add(item.getSum2());
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==i){
								arrayList.add("0");
								}
							}
						content.put(databases.get(j),arrayList);
						}
					if(urlType[0]==2){
						for(int i = 0;i<24;i++){
							for (DatabaseUseHourly item : list) {
								if(databases.get(j).equals(item.getDatabase_name())&&Integer.parseInt(item.getHour())==i+1){
									arrayList.add(item.getSum3());
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databases.get(j),arrayList);
					}
					if(urlType[0]==3){
						for(int i = 0;i<24;i++){
							for (DatabaseUseHourly item : list) {
								if(databases.get(j).equals(item.getDatabase_name())&&Integer.parseInt(item.getHour())==i+1){
									arrayList.add(item.getSum1());
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databases.get(j),arrayList);
					}
				}
			}else {
				//按天统计
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
				//得到标题
				for(String item : titleList){
					databases.add(item);
				}
				for(int j = 0;j<databaseName.size();j++){
					List arrayList = new ArrayList<>();
					if(urlType[0]==1){
						for(int i = 0;i<timeList.size();i++){
							for (DatabaseUseHourly item : list) {
								if(databases.get(j).equals(item.getDatabase_name())&&timeList.get(i).equals(item.getDate())){
									arrayList.add(item.getSum2());
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databases.get(j),arrayList);
					}
					if(urlType[0]==2){
						for(int i = 0;i<timeList.size();i++){
							for (DatabaseUseHourly item : list) {
								if(databases.get(j).equals(item.getDatabase_name())&&timeList.get(i).equals(item.getDate())){
									arrayList.add(item.getSum3());
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databases.get(j),arrayList);
					}
					if(urlType[0]==3){
						for(int i = 0;i<timeList.size();i++){
							for (DatabaseUseHourly item : list) {
								if(databases.get(j).equals(item.getDatabase_name())&&timeList.get(i).equals(item.getDate())){
									arrayList.add(item.getSum1());
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==i){
								arrayList.add("0");
							}
						}
						content.put(databases.get(j),arrayList);
					}
				}
			}

			map.put("title",databases.toArray());
			map.put("timeArr", timeList.toArray());
			map.put("content", content);
		}

		return map;

	};

	@Override
	public List<Map<String, String>> exportDatabase(DatabaseUseDaily databaseUseDaily,
			String startTime, String endTime) {
		List<Map<String, String>> listmap=new ArrayList<Map<String,String>>();
		/**
		 * 分三种情况：
		 * 1.机构名称和用户ID都为空时
		 * 2.用户ID不为空时
		 * 3.机构名称不为空（用户ID要为空）
		 */
		if(StringUtils.isBlank(databaseUseDaily.getInstitution_name()) && StringUtils.isBlank(databaseUseDaily.getUser_id())){
			//获取导出的数据
			listmap=databaseUseHourlyMapper.exportDatabaseOneDay(databaseUseDaily, startTime, endTime);
		}else if(StringUtils.isNotBlank(databaseUseDaily.getUser_id())){
			//获取导出的数据
			listmap=databaseUseHourlyMapper.exportDatabaseOneDayById(databaseUseDaily, startTime, endTime);
		}else{
			//得到此机构中所有的用户（包括机构账号和机构子账号）
			List users = personMapper.getInstitutionUser(databaseUseDaily.getInstitution_name());
			//获取导出的数据
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
