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

import com.wf.bean.HotWord;
import com.wf.bean.HotWordSetting;
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
	private HotWordService hotWordService;
	@Autowired
	private IForbiddenSerivce forbiddenSerivce;
	
	@Scheduled(cron = "0 0/5 * * * ?")
	public void exechotWord() {
		try {
			log.info("开始执行热门文献的统计");
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
			int index=1;
			int pageNum=0;
			int pageSize=100;
			Map<String,Integer> countMap=new LinkedHashMap<String,Integer>();
			while(true){
				String limit=pageNum*pageSize+","+pageSize;
				String query=sql.toString()+limit;
				log.info("执行sql:"+query);
				List<Map<String,Object>> list=hotWordSettingService.getHotWordTongJi(query);
				if(list.size()==0){
					break;
				}
				for(Object obj:list){
					Map<String,Object> map=(Map<String, Object>) obj;
					String theme=map.get("theme").toString();
					int count=Integer.parseInt(map.get("frequency").toString());
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
						if(theme.indexOf(":")!=-1){
							theme=theme.substring(theme.indexOf(":")+1, theme.length());
						}
						
						if(theme.indexOf("：")!=-1){
							theme=theme.substring(theme.indexOf("：")+1, theme.length());
						}
						if (countMap.get(theme) == null) {
							countMap.put(theme, count);
							index++;
						} else {
							countMap.put(theme, countMap.get(theme) + count);
						}
						if (index > 50) {
							break;
						}
					}
					if (index > 50) {
						break;
					}
				}
				if (index > 50) {
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
		if (result > 0.1) {
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
