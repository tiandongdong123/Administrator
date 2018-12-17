package com.job;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bigdata.framework.forbidden.iservice.IForbiddenSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.utils.jdbcUtils;
import com.wf.bean.HotWord;
import com.wf.bean.HotWordSetting;
import com.wf.dao.HotWordMapper;
import com.wf.service.HotWordService;
import com.wf.service.HotWordSettingService;
import com.xxl.conf.core.XxlConfClient;

@Component
public class HotWordJob {
	
	private static Logger log = Logger.getLogger(HotWordJob.class);
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static List<HotWord> hotwordList=new ArrayList<HotWord>();
	
	@Autowired
	private HotWordSettingService hotWordSettingService;
	@Autowired
	private HotWordService hotwordService;
	@Autowired
	private HotWordService hotWordService;
	@Autowired
	private IForbiddenSerivce forbiddenSerivce;
	
	@Scheduled(cron = "0 0/2 * * * ?")
	public void exechotWord() {
		try {
			log.info("开始执行热搜词的统计");
			InetAddress addr = InetAddress.getLocalHost();
			String hostip = addr.getHostAddress();
			String execIp = XxlConfClient.get("wf-admin.hotWordExecIP", null);
			if (!StringUtils.isEmpty(execIp)&&!"127.0.0.1".equals(execIp) && !hostip.equals(execIp)) {
				log.info("服务器" + hostip + "无需发布数据,有权限执行的ip:" + execIp);
				return;
			}
			HotWordSetting set = hotWordSettingService.getHotWordSettingTask();
			if(set==null){
				log.info("没有任务需要执行");
				return;
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int sys_hour = cal.get(Calendar.HOUR_OF_DAY);
			String end_time = set.getNext_publish_time().substring(0, 10) + " " + set.getGet_time();
			Date query_end = df.parse(end_time);// 开始查询时间
			cal.setTime(query_end);
			int task_hour = cal.get(Calendar.HOUR_OF_DAY);
			if (sys_hour != task_hour) {
				log.info("未到定时器执行时间");
				return;
			}
			hotwordList.clear();
			//统计要插入的数据
			cal.add(Calendar.DATE, -set.getTime_slot());
			Date query_start=cal.getTime();//结束查询时间
			int start_month=query_start.getMonth()+1;
			int end_month=query_end.getMonth()+1;
			if(start_month>end_month){
				end_month=12+end_month;
			}
			cal.setTime(query_start);
			StringBuffer sql=new StringBuffer("select e.theme,count(1) frequency from (");
			for(int i=start_month;i<=end_month;i++){
				if(i>start_month){
					sql.append(" UNION ALL ");
				}
				sql.append("select theme from ");
				int month=cal.get(Calendar.MONTH)+1;
				String str_month=month>9?String.valueOf(month):String.valueOf("0"+month);
				sql.append("zsfx_res_table.AUTO_THEHE_"+cal.get(Calendar.YEAR)+"_"+str_month+" ");
				sql.append("where create_time BETWEEN '"+df.format(query_start)+"' and '"+df.format(query_end)+"'");
				cal.add(Calendar.MONTH, 1);
			}
			sql.append(") e group by e.theme order by frequency desc limit ");
			int pageNum=0;
			int pageSize=100;
			Map<String,Integer> countMap=new LinkedHashMap<String,Integer>();
			while(true){
				String limit=pageNum*pageSize+","+pageSize;
				String query=sql.toString()+limit;
				log.info("执行sql:"+query);
				List<Map<String,String>> list=jdbcUtils.getConnectTheme(query);
				if(list.size()==0){
					break;
				}
				for(Map<String,String> map:list){
					String theme=map.get("theme");
					int count=Integer.parseInt(map.get("frequency"));
					int forbid=forbiddenSerivce.CheckForBiddenWord(theme);
					if(forbid>0 || isMessyCode(theme)){
						continue;
					}
					if(StringUtils.isBlank(theme)){
						continue;
					}
					String[] words=theme.trim().split(" ");
					for(String word:words){
						if(StringUtils.isBlank(word)){
							continue;
						}
						if(word.indexOf(":")!=-1){
							word=word.substring(word.indexOf(":")+1, word.length());
						}
						
						if(word.indexOf("：")!=-1){
							word=word.substring(word.indexOf("：")+1, word.length());
						}
						if (countMap.get(word) == null) {
							countMap.put(word, count);
						} else {
							countMap.put(word, countMap.get(word) + count);
						}
						if (countMap.size() >= 50) {
							break;
						}
					}
					if (countMap.size() >= 50) {
						break;
					}
				}
				if (countMap.size() >= 50) {
					break;
				}
				//下一页
				pageNum++;
			}
			
			// 然后通过比较器来实现排序
			List<Map.Entry<String, Integer>> listmap = new ArrayList<Map.Entry<String, Integer>>(countMap.entrySet());
			Collections.sort(listmap, new Comparator<Map.Entry<String, Integer>>() {
				// 升序排序
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
			// 插入数据库
			int status = 1;
			for (Map.Entry<String,Integer> entry : listmap) {
				HotWord hot = new HotWord();
				hot.setWord(entry.getKey());
				hot.setSearchCount(entry.getValue());
				hot.setWordNature("前台获取");
				hot.setOperationTime(set.getNext_publish_time());
				if (status <= 20) {
					hot.setWordStatus(1);
				} else {
					hot.setWordStatus(2);
				}
				//存入内存中，等待redis发布
				hotwordList.add(hot);
				status++;
			}
			log.info("热搜词更新时间完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("完成热搜词的统计");
	}
	/**
	 * 手动设置任务定时器
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void exechotWordManual() {
		try {
			log.info("开始执行手动设置热搜词的统计");
			//返回本机ip地址  hostip为本机ip地址
			InetAddress addr = InetAddress.getLocalHost();//本机ip
			String hostip = addr.getHostAddress();
			String execIp = XxlConfClient.get("wf-admin.hotWordExecIP", null);//热搜词执行定时器ip
			//当本机ip和热搜词执行定时器ip不相等时不执行
//			if (!StringUtils.isEmpty(execIp)&&!"127.0.0.1".equals(execIp) && !hostip.equals(execIp)) {
//				log.info("服务器" + hostip + "无需发布数据,有权限执行的ip:" + execIp);
//				return;
//			}
			//获取任务(并且任务的下次抓取时间为当天的)
			HotWordSetting set = hotWordSettingService.getHotWordManualSettingTask();
			if(set==null){
				log.info("没有手动设置任务需要执行");
				return;
			}
			//获取现在时间的整点
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int sys_hour = cal.get(Calendar.HOUR_OF_DAY);
			//获取下次抓取数据时间的日期   及  数据抓取时间
			String end_time = set.getNext_get_time().substring(0, 10) + " " + set.getGet_time();
			Date query_end = df.parse(end_time);// 结束数据抓取时间
			cal.setTime(query_end);
			int task_hour = cal.get(Calendar.HOUR_OF_DAY);
			//现在整点如果不等于设定的数据抓取时间整点  则不执行 
				if (sys_hour != task_hour) {
					log.info("未到定时器执行时间");
					return;
				}
			
			//统计要插入的数据
			cal.add(Calendar.DATE, -set.getTime_slot());
			Date query_start=cal.getTime();//开始数据抓取时间（设定的抓取几天前的时间假设设定了数据统计时间段为近3天  则query_start为12月3日的（今天6号））
			//获取开始抓取数据的时间月份
			int start_month=query_start.getMonth()+1;
			//获取结束抓取数据的时间月份
			int end_month=query_end.getMonth()+1;
			if(start_month>end_month){
				end_month=12+end_month;
			}
			cal.setTime(query_start);
			StringBuffer sql=new StringBuffer("select e.theme,count(1) frequency from (");
			//如果开始时间与结束时间的数据不在同一张表里  多张表查询
			for(int i=start_month;i<=end_month;i++){
				if(i>start_month){
					sql.append(" UNION ALL ");
				}
				sql.append("select theme from ");
				int month=cal.get(Calendar.MONTH)+1;
				String str_month=month>9?String.valueOf(month):String.valueOf("0"+month);
				sql.append("zsfx_res_table.AUTO_THEHE_"+cal.get(Calendar.YEAR)+"_"+str_month+" ");
				sql.append("where create_time BETWEEN '"+df.format(query_start)+"' and '"+df.format(query_end)+"'");
				cal.add(Calendar.MONTH, 1);
			}
			sql.append(") e group by e.theme order by frequency desc limit ");
			int pageNum=0;
			int pageSize=100;
			Map<String,Integer> countMap=new LinkedHashMap<String,Integer>();
			while(true){
				String limit=pageNum*pageSize+","+pageSize;
				String query=sql.toString()+limit;
				log.info("执行sql:"+query);
				List<Map<String,String>> list=jdbcUtils.getConnectTheme(query);
				if(list.size()==0){
					break;
				}
				//循环遍历list结果集
				for(Map<String,String> map:list){
					String theme=map.get("theme");
					int count=Integer.parseInt(map.get("frequency"));
					//过滤禁用词
					log.info("执行对比禁用词");
					int forbid=forbiddenSerivce.CheckForBiddenWord(theme);
					if(forbid>0 || isMessyCode(theme)){
						continue;
					}
					if(StringUtils.isBlank(theme)){
						continue;
					}
					String[] words=theme.trim().split(" ");
					for(String word:words){
						if(StringUtils.isBlank(word)){
							continue;
						}
						if(word.indexOf(":")!=-1){
							word=word.substring(word.indexOf(":")+1, word.length());
						}
						
						if(word.indexOf("：")!=-1){
							word=word.substring(word.indexOf("：")+1, word.length());
						}
						if (countMap.get(word) == null) {
							countMap.put(word, count);
						} else {
							countMap.put(word, countMap.get(word) + count);
						}
						if (countMap.size() >= 50) {
							break;
						}
					}
					if (countMap.size() >= 50) {
						break;
					}
				}
				if (countMap.size() >= 50) {
					break;
				}
				//下一页
				pageNum++;
			}
			
			// 然后通过比较器来实现排序
			List<Map.Entry<String, Integer>> listmap = new ArrayList<Map.Entry<String, Integer>>(countMap.entrySet());
			Collections.sort(listmap, new Comparator<Map.Entry<String, Integer>>() {
				// 升序排序
				public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
			//清理数据库
			hotWordService.deleteHotWord();
			log.info("清理热搜词数据库完成");
			// 插入数据库
			for (Map.Entry<String,Integer> entry : listmap) {
				HotWord hot = new HotWord();
				hot.setWord(entry.getKey());
				hot.setSearchCount(entry.getValue());
				hot.setWordNature("前台获取");
				hot.setOperationTime(set.getNext_get_time());
				hot.setWordStatus(2);
				//把热搜词插入到数据库hot_search_word
				log.info("保存热搜词："+entry.getKey());
				hotwordService.addWord(hot);
			}			
			// 设置本次抓取数据时间段=下次抓取时间-抓取数据时间段--下次抓取时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date =sdf.parse(set.getNext_get_time().substring(0, 10));
			cal.setTime(date); 
			int day=cal.get(Calendar.DATE); 
			cal.set(Calendar.DATE,day-set.getTime_slot()); 
			sdf=new SimpleDateFormat("yyyy年MM月dd日");
			StringBuffer sb=new StringBuffer("");
			String str=set.getNext_get_time().substring(0, 10);
			sb.append(str.replaceFirst("-", "年").replaceFirst("-", "月"));
			sb.append("日");
			String now_get_time_space=sdf.format(cal.getTime())+" "+set.getGet_time()+"───"+sb.toString()+" "+set.getGet_time();
			set.setNow_get_time_space(now_get_time_space);
			hotWordSettingService.updateWordSetting(set);
			// 更改下次抓取时间
			Calendar calendar = Calendar.getInstance();
			day=calendar.get(Calendar.DATE);
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			calendar.set(Calendar.DATE,day+set.getGet_cyc()); 
			String next_get_time=sdf.format(calendar.getTime())+" "+set.getGet_time();
			set.setNext_get_time(next_get_time);
			//是否发布，0未发布  1已发布
			set.setIs_first("0");
			hotWordSettingService.updateWordSetting(set);
			hotWordSettingService.updateAllManualNowGetTimeApace(set);
			log.info("手动设置热搜词更新时间完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("完成手动热搜词的抓取");
	} 
	
	private boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	 
	private boolean isMessyCode(String strName) {
		Pattern p = Pattern.compile("\\s*|t*|r*|n*");
		Matcher m = p.matcher(strName);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
				}
			}
		}
		float result = count / chLength;
		if (result > 0) {
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * 获取查询的热搜词
	 * 
	 * @return
	 */
	public static List<HotWord> getHotWordList() {
		return hotwordList;
	}

	/**
	 * 情况查询的热搜词
	 */
	public static void setHotWordClear() {
		hotwordList.clear();
	}

}
