package com.job;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
	
	@Autowired
	private HotWordSettingService hotWordSettingService;
	@Autowired
	private HotWordService hotWordService;
	@Autowired
	private IForbiddenSerivce forbiddenSerivce;
	
	@Scheduled(cron = "0 0 0/1 * * ?")
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
			
			//清理数据库，去除敏感词,插入数据
			hotWordService.deleteHotWord();
			log.info("清理热搜词完成");
			
			//统计要插入的数据
			cal.add(Calendar.DATE, -set.getTime_slot());
			Date query_start=cal.getTime();//结束查询时间
			int start_month=query_start.getMonth()+1;
			int end_month=query_end.getMonth()+1;
			cal.setTime(query_end);
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
			sql.append(") e group by e.theme order by frequency desc limit 100");
			log.info("执行sql:"+sql.toString());
			List<Map<String,Object>> list=hotWordSettingService.getHotWordTongJi(sql.toString());
			int index=1;
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
					HotWord hot=new HotWord();
					hot.setWord(theme);
					hot.setSearchCount(count);
					hot.setWordNature("前台获取");
					hot.setOperationTime(set.getNext_publish_time());
					if(index<=20){
						hot.setWordStatus(1);
					}else{
						hot.setWordStatus(2);
					}
					hotWordService.addWord(hot);
					if(index>=50){
						break;
					}
					index++;
				}
			}
			log.info("热搜词导入数据库完成");
			//更新发布时间
			query_end = df.parse(set.getNext_publish_time());//开始查询时间
			cal.setTime(query_end);
			cal.add(Calendar.DATE, set.getPublish_cyc());
			String puublist_time=df.format(cal.getTime()).substring(0,10)+" "+set.getPublish_date();
			set.setNext_publish_time(puublist_time);
			hotWordSettingService.updateWordSetting(set);
			hotWordSettingService.updateAllSettingTime();
			log.info("热搜词更新时间完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("完成热搜词的发布");
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
		System.out.println(strName);
		float result = count / chLength;
		if (result > 0.1) {
			return true;
		} else {
			return false;
		}

	}
	 
}
