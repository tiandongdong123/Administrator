package com.job;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wf.bean.HotWord;
import com.wf.bean.HotWordSetting;
import com.wf.service.HotWordService;
import com.wf.service.HotWordSettingService;
import com.xxl.conf.core.XxlConfClient;

/**
 * 热搜词发送至redis
 * @author ouyang
 *
 */

@Component
public class HotWordSetRedisJob {
	
	@Autowired
	private HotWordService hotWordService;
	@Autowired
	private HotWordSettingService hotWordSettingService;
	
	private static Logger log = Logger.getLogger(HotWordSetRedisJob.class);
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Scheduled(cron = "0 0/3 * * * ?")
	public void execHotWordSetRedis() {
		try{
			log.info("开始热搜词redis发布");
			InetAddress addr = InetAddress.getLocalHost();
			String hostip = addr.getHostAddress();
			String execIp = XxlConfClient.get("wf-admin.hotWordExecIP", null);
			if (!StringUtils.isEmpty(execIp)&&!"127.0.0.1".equals(execIp) && !hostip.equals(execIp)) {
				log.info("服务器" + hostip + "无需发布数据,有权限执行的ip:" + execIp);
				return;
			}
			HotWordSetting set = hotWordSettingService.getExecHotWordSetting(1);
			if(set==null){
				log.info("没有任务需要执行");
				return;
			}
			List<HotWord> list=HotWordJob.getHotWordList();
			if(list.size()==0){
				log.info("hotwordList没有要执行的数据");
				return;
			}
			Calendar cal = Calendar.getInstance();
			//获取系统小时
			cal.setTime(new Date());
			int sys_hour=cal.get(Calendar.HOUR_OF_DAY);
			//获取任务小时
			Date query_end = df.parse(set.getNext_publish_time());// 开始查询时间
			cal.setTime(query_end);
			cal.add(Calendar.DATE, -set.getTime_slot());
			int task_hour=cal.get(Calendar.HOUR_OF_DAY);
			if(sys_hour!=task_hour){
				log.info("未到定时器执行时间");
				return;
			}
			//清理数据库，去除敏感词,插入数据
			hotWordService.deleteHotWord();
			log.info("清理热搜词完成");
			for(HotWord hot:list){
				hotWordService.addWord(hot);
			}
			HotWordJob.setHotWordClear();//清空查询的热搜词
			//发布redis
			boolean flag=hotWordService.publishToRedis();
			if(flag){
				log.info("发布redis成功");
			}else{
				log.info("发布redis失败");
			}
			log.info("热搜词导入数据库完成");
			//更新发布时间
			query_end = df.parse(set.getNext_publish_time());//开始查询时间
			cal.setTime(query_end);
			cal.add(Calendar.DATE, set.getPublish_cyc());
			String puublist_time=df.format(cal.getTime()).substring(0,10)+" "+set.getPublish_date();
			set.setNext_publish_time(puublist_time);
			//判断是否是第一次
			if(!StringUtils.isEmpty(set.getFirst_publish_time())){
				set.setIs_first("1");
			}
			hotWordSettingService.updateWordSetting(set);
			hotWordSettingService.updateAllSettingTime();
			log.info("热搜词redis发布结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
