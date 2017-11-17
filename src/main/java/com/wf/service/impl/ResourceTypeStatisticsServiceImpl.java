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

import com.wf.bean.PageList;
import com.wf.bean.ResourceStatistics;
import com.wf.bean.ResourceStatisticsHour;
import com.wf.bean.ResourceTableBean;
import com.wf.bean.ResourceType;
import com.wf.dao.PersonMapper;
import com.wf.dao.ResourceStatisticsHourMapper;
import com.wf.dao.ResourceStatisticsMapper;
import com.wf.dao.ResourceTypeMapper;
import com.wf.service.ResourceTypeStatisticsService;

@Service
public class ResourceTypeStatisticsServiceImpl implements
		ResourceTypeStatisticsService {

	@Autowired
	private ResourceTypeMapper type;
	//@Autowired
	//private ResourceStatisticsMapper restatistics;
	@Autowired
	private ResourceStatisticsHourMapper hour;
	@Autowired
	private PersonMapper personMapper;
	@Autowired
	private ResourceTypeMapper resourceTypeMapper;
	@Override
	public List<ResourceType> getResourceType() {
		List<ResourceType> listr = new ArrayList<ResourceType>();
		try {
			listr = this.type.getRtypeName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listr;
	}

	@Override
	public Map<String, Object> getAllLine(String starttime,String endtime,ResourceStatistics res,
										  Integer[] urls,Integer singmore,String[] database_name) {
		Map<String,Object> map=new HashMap();
		List<ResourceStatisticsHour> list=new ArrayList<ResourceStatisticsHour>();
		Map<String, List<String>> content = new HashMap<String, List<String>>();
		List<String>timeList=new ArrayList();//时间集合（按小时：1-24 按天：每一天的日期）
		List<String>browseList=new ArrayList();//浏览数集合
		List<String>downloadList=new ArrayList();//下载数集合
		List<String>searchList=new ArrayList();//检索数集合
		List<String>shareList=new ArrayList();//分享数
		List<String>collectionList=new ArrayList();//收藏数
		List<String>exportList=new ArrayList();//导出数
		List<String>noteList=new ArrayList();//笔记数
		List<String>jumpList=new ArrayList();//跳转数
		List<String>subscriptionList=new ArrayList();//订阅数
		List<String> resources = new ArrayList<>();//资源类型集合
		List users = new ArrayList();//用户集合
		List<String> titleList = new ArrayList<>();//折线图标题集合
		//根据分析指标数判断
		//当分析指标数组长度小于等于1，按分析指标展示
		//当分析指标数组长度大于1，按资源类型展示
		if(urls.length<=1){
			/**
			 * 分三种情况：
			 * 1.机构名称和用户ID都为空时
			 * 2.用户ID不为空时
			 * 3.机构名称不为空（用户ID要为空）
			 */
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				//按条件查询
				list = this.hour.getChart(starttime,endtime,res,urls,singmore,database_name);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				//按条件查询
				list = this.hour.getChartById(starttime,endtime,res,urls,singmore,database_name);
			} else {
				//查处属于此机构的所有用户（包括机构账号和机构子账号）
				users = personMapper.getInstitutionUser(res.getInstitutionName());
				//按条件查询
				list=this.hour.getChartByIds(starttime, endtime,res,users,urls,singmore,database_name);
			}
			titleList = resourceTypeMapper.getResourceByCode(database_name);
		}else {
			/**
			 * 分三种情况：
			 * 1.机构名称和用户ID都为空时
			 * 2.用户ID不为空时
			 * 3.机构名称不为空（用户ID要为空）
			 */
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				//按条件查询
				list = this.hour.getChartMore(starttime,endtime,res,urls,database_name);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				//按条件查询
				list = this.hour.getChartMoreById(starttime,endtime,res,urls,database_name);
			} else {
				//查处属于此机构的所有用户
				users = personMapper.getInstitutionUser(res.getInstitutionName());
				//按条件查询
				list=this.hour.getChartMoreByIds(starttime, endtime,res,users,urls,database_name);
			}
		}
		//处理按条件查的数据
		//当资源和表格中的复选框同时多选时
		if(singmore==0){
			//按小时统计
			if(res.getDate()!=null&&!"".equals(res.getDate())){
				//得到每个小时
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				//得到标题
				for(String item : titleList){
					resources.add(item);
				}
				/**
				 * 此处有3层for循环（业务需求）
				 * 数据量特别大时本不合理，但通过控制，虽有表面是3层循环，但实际上只相当于2层
				 */
				for(int i =0;i<resources.size();i++){
					List arrayList = new ArrayList<>();
					//通过分析指标的值来判断需要哪个sum
					if(urls[0]==1){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							//遍历list集合
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum1() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==2){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							//遍历list集合
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum2() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==3){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum3() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==4){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum4() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==5){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum5() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==6){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum6() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==7){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum7() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==8){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum8() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==9){
						//得到每个小时的值
						for(int j = 0;j<24;j++){
							for (ResourceStatisticsHour item : list) {
								//判断是否是当前标题和时间点
								if(resources.get(i).equals(item.getSourceTypeName())&&Integer.parseInt(item.getHour())==j+1){
									arrayList.add(item.getSum9() );
									//移除当前元素  目的：能够每次只判断第一条，减轻for循环次数
									list.remove(0);
								}
								break;
							}
							//如果这个时间点没有数据，设为0
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
				}
			}else {
				//按天统计
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date startTime = format.parse(starttime);
					Date endTime = format.parse(endtime);
					List<Date> days = this.getDate(startTime, endTime);
					//获取统计时间没的日期（每一天）
					for(Date d : days){
						timeList.add(format.format(d));
					}
				}catch (ParseException e) {
					e.printStackTrace();
				}//得到标题
				for(String item : titleList){
					resources.add(item);
				}
				for(int i =0;i<resources.size();i++){
					List arrayList = new ArrayList<>();
					if(urls[0]==1){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum1() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==2){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum2() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==3){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum3() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==4){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum4() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==5){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum5() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==6){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum6() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==7){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum7() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==8){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum8() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
					if(urls[0]==9){
						for(int j = 0;j<timeList.size();j++){
							for (ResourceStatisticsHour item : list) {
								if(resources.get(i).equals(item.getSourceTypeName())&&timeList.get(j).equals(item.getDate())){
									arrayList.add(item.getSum9() );
									list.remove(0);
								}
								break;
							}
							if(arrayList.size()==j){
								arrayList.add("0");
							}
						}
						content.put(resources.get(i),arrayList);
					}
				}
			}
			map.put("title",resources.toArray());
		}else {
			//按小时统计
			//获取时间(1-24)
			if(res.getDate()!=null&&!"".equals(res.getDate())){
				for(Integer i = 1;i<=24;i++){
					timeList.add(i.toString());
				}
				//设置每个时间点的各项指标数
				for(int i = 0;i<24;i++){
					for (ResourceStatisticsHour item : list) {
						if(Integer.parseInt(item.getHour())==i+1){
							browseList.add(item.getSum1() );
							downloadList.add(item.getSum2());
							searchList.add(item.getSum3());
							shareList.add(item.getSum4());
							collectionList.add(item.getSum5());
							exportList.add(item.getSum6());
							noteList.add(item.getSum7());
							jumpList.add(item.getSum8());
							subscriptionList.add(item.getSum9());
							break;
						}
					}
					//如果此时间点没有数据，设为0
					if(searchList.size()==i){
						searchList.add("0");
						browseList.add("0");
						downloadList.add("0");
						shareList.add("0");
						collectionList.add("0");
						exportList.add("0");
						noteList.add("0");
						jumpList.add("0");
						subscriptionList.add("0");
					}
				}
			}else {
				//按天统计
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date startTime = format.parse(starttime);
					Date endTime = format.parse(endtime);
					List<Date> days = this.getDate(startTime, endTime);
					//获取统计时间内的每天
					for(Date d : days){
						timeList.add(format.format(d));
					}
				}catch (ParseException e) {
					e.printStackTrace();
				}
				//设置每一天的各项指标数
				for(int i = 0;i<timeList.size();i++){
					for (ResourceStatisticsHour item : list) {
						if(timeList.get(i).equals(item.getDate())){
							browseList.add(item.getSum1() );
							downloadList.add(item.getSum2());
							searchList.add(item.getSum3());
							shareList.add(item.getSum4());
							collectionList.add(item.getSum5());
							exportList.add(item.getSum6());
							noteList.add(item.getSum7());
							jumpList.add(item.getSum8());
							subscriptionList.add(item.getSum9());
							break;
						}
					}
					//若当天没有数据，则设为0
					if(searchList.size()==i){
						searchList.add("0");
						browseList.add("0");
						downloadList.add("0");
						shareList.add("0");
						collectionList.add("0");
						exportList.add("0");
						noteList.add("0");
						jumpList.add("0");
						subscriptionList.add("0");
					}
				}
			}
			content.put("浏览数",browseList);
			content.put("下载数",downloadList);
			content.put("检索数",searchList);
			content.put("分享数",shareList);
			content.put("收藏数",collectionList);
			content.put("导出数",exportList);
			content.put("笔记数",noteList);
			content.put("跳转数",jumpList);
			content.put("订阅数",subscriptionList);
		}
		map.put("timeArr", timeList.toArray());
		map.put("content", content);
		return map;
	}
	@Override
	public PageList gettable(Integer table, String starttime, String endtime,
			ResourceStatistics res, Integer pageNum, Integer pageSize) {
		PageList pageList=new PageList();
		List<Object> PageList=new ArrayList<Object>();
		List<Object> list=new ArrayList<Object>();
		//分页开始的值（limit中的）
		int startNum = (pageNum-1)*pageSize;
		/**
		 * 判断表格显示
		 * 资源类型选择期刊、会议、学位中的一中 table==0
		 * 其他情况table==1
		 */
		if(table==0){
			/**
			 * 分三种情况：
			 * 1.机构名称和用户ID都为空时
			 * 2.用户ID不为空时
			 * 3.机构名称不为空（用户ID要为空）
			 */
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				//按查询条件得到表格中的分页后数据
				PageList = this.hour.getLine(starttime,endtime,res,startNum,pageSize);
				//按查询条件得到表格中的所有数据
				list=this.hour.getLineAll(starttime,endtime,res);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				//按查询条件得到表格中的数据
				PageList = this.hour.getLineById(starttime,endtime,res,startNum,pageSize);
				//用于得到页面总数
				list=this.hour.getLineAllById(starttime,endtime,res);
			} else {
				//得到此机构中所有的用户（包括机构账号和机构子账号）
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				//按查询条件得到表格中的数据
				PageList=this.hour.getLineByIds(starttime, endtime,res,users,startNum,pageSize);
				//用于得到页面总数
				list=this.hour.getLineAllByIds(starttime, endtime,res,users);
			}
		}else {
			/**
			 * 分三种情况：
			 * 1.机构名称和用户ID都为空时
			 * 2.用户ID不为空时
			 * 3.机构名称不为空（用户ID要为空）
			 */
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				//按查询条件得到表格中的数据
				PageList = this.hour.getLineMore(starttime,endtime,res,startNum,pageSize);
				//按查询条件得到表格中的所有数据
				list=this.hour.getLineMoreAll(starttime,endtime, res);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				//按查询条件得到表格中的数据
				PageList = this.hour.getLineMoreById(starttime,endtime,res,startNum,pageSize);
				//按查询条件得到表格中的所有数据
				list=this.hour.getLineMoreAllById(starttime,endtime,res);
			} else {
				//得到此机构中所有的用户（包括机构账号和机构子账号）
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				//按查询条件得到表格中的数据
				PageList=this.hour.getLineMoreByIds(starttime, endtime,res,users,startNum,pageSize);
				//按查询条件得到表格中的所有数据
				list=this.hour.getLineMoreAllByIds(starttime, endtime,res,users);
			}
		}
		pageList.setTotalRow(list.size());//每页显示的数量
		pageList.setPageRow(PageList);//查询结果表
		pageList.setPageNum(pageNum);//当前页
		pageList.setPageSize(pageSize);//每页显示的数量
		return pageList;
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

	@Override
	public List<Object> exportresourceType(Integer table, String starttime,
			String endtime, ResourceStatistics res) {
		List<Object> list = new ArrayList<Object>();
		/**
		 * 判断表格显示
		 * 资源类型选择期刊、会议、学位中的一中 table==0
		 * 其他情况table==1
		 */
		if(table==0){
			/**
			 * 分三种情况：
			 * 1.机构名称和用户ID都为空时
			 * 2.用户ID不为空时
			 * 3.机构名称不为空（用户ID要为空）
			 */
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				//按查询条件得出结果
				list = this.hour.getLineAll(starttime,endtime,res);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				//按查询条件得出结果
				list = this.hour.getLineAllById(starttime,endtime,res);
			} else {
				//得到此机构中所有的用户（包括账号和子账号）
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				//按查询条件得出结果
				list=this.hour.getLineAllByIds(starttime, endtime,res,users);
			}
		}else {
			/**
			 * 分三种情况：
			 * 1.机构名称和用户ID都为空时
			 * 2.用户ID不为空时
			 * 3.机构名称不为空（用户ID要为空）
			 */
			if (StringUtils.isBlank(res.getInstitutionName())&& StringUtils.isBlank(res.getUserId())) {
				//按查询条件得出结果
				list = this.hour.getLineMoreAll(starttime,endtime,res);
			} else if ( StringUtils.isNotBlank(res.getUserId())) {
				//按查询条件得出结果
				list = this.hour.getLineAllById(starttime,endtime,res);
			} else {
				//得到此机构中所有的用户（包括账号和子账号）
				List users = personMapper.getInstitutionUser(res.getInstitutionName());
				//按查询条件得出结果
				list=this.hour.getLineMoreAllByIds(starttime, endtime,res,users);
			}
		}
		return list;
	}

}
