package com.job;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.bigdata.framework.forbidden.iservice.IForbiddenSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wf.bean.HotWord;
import com.wf.bean.HotWordSetting;
import com.wf.service.HotWordService;
import com.wf.service.HotWordSettingService;

@Component
public class HotWordJob {
	
	private static Logger log = Logger.getLogger(HotWordJob.class);
	
	@Autowired
	private HotWordSettingService hotWordSettingService;
	@Autowired
	private HotWordService hotWordService;
	@Autowired
	private IForbiddenSerivce forbiddenSerivce;
	
	//每凌晨1点执行(检查是否需要发送邮件)
	@Scheduled(cron = "0 0 19 * * ?")
	public void exechotWord() {
		//首先考虑获取数据时间
		try {
			log.info("开始执行热门文献的发布");
			HotWordSetting set = hotWordSettingService.getHotWordSettingTask();
			if(set==null){
				log.info("没有任务需要执行");
				return;
			}
			Calendar cal=Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date query_start = df.parse(set.getNext_publish_time());//开始查询时间
			cal.setTime(query_start);
			cal.add(Calendar.DATE, -set.getTime_slot());
			Date query_end=cal.getTime();//结束查询时间
			//查数据库统计数据
			int start_month=query_start.getMonth()+1;
			int end_month=query_end.getMonth()+1;
			cal.setTime(query_start);
			StringBuffer sql=new StringBuffer("select e.theme,count(1) frequency from (");
			for(int i=start_month;i<=end_month;i++){
				if(i>start_month){
					sql.append("UNION ALL ");
				}
				sql.append("select theme,count(1) frequency from ");
				int month=Calendar.MONTH+1;
				String str_month=month>9?String.valueOf(month):String.valueOf("0"+month);
				sql.append("zsfx_res_table.AUTO_THEHE_"+Calendar.YEAR+"_"+str_month+" ");
				sql.append("where create_time>='"+df.format(query_start)+"' and create_time<='"+df.format(query_end)+"' ");
				cal.add(Calendar.MONTH, 1);
			}
			sql.append(") e group by e.theme order by frequency desc limit 100");
			List<Object> list=hotWordSettingService.getHotWordTongJi(sql.toString());
			//清理数据库，去除敏感词,插入数据
			hotWordService.deleteHotWord();
			log.info("清理热搜词完成");
			int index=1;
			for(Object obj:list){
				Map<String,Object> map=(Map<String, Object>) obj;
				String theme=map.get("theme").toString();
				int count=Integer.parseInt(map.get("frequency").toString());
				int forbid=forbiddenSerivce.CheckForBiddenWord(theme);
				if(forbid>0 || !isMessyCode(theme)){
					continue;
				}
				
				if(theme.indexOf(":")!=-1 || theme.indexOf("：")!=-1){
					theme=theme.substring(theme.indexOf(":")+1, theme.length());
				}
				
				if(theme.indexOf("：")!=-1){
					theme=theme.substring(theme.indexOf("：")+1, theme.length());
				}
				
				HotWord word=new HotWord();
				word.setWord(theme);
				word.setSearchCount(count);
				word.setWordNature("前台获取");
				if(index>=20){
					word.setWordStatus(1);
				}else{
					word.setWordStatus(2);
				}
				if(index<=50){
					hotWordService.addWord(word);
				}
				index++;
			}
			log.info("cha");
			//发布redis
			boolean flag=hotWordService.publishToRedis();
			if(flag){
				log.info("发布redis成功");
			}else{
				log.info("发布redis失败");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log.info("完成热门文献的发布");
	}
	
	 public  boolean isChinese(char c) {
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
	 
	 public  boolean isMessyCode(String strName) {
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
	        if (result > 0.4) {
	            return true;
	        } else {
	            return false;
	        }
	 
	    }
	 
}
