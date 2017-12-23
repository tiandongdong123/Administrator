package com.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wf.bean.HotWordSetting;
import com.wf.service.HotWordService;
import com.wf.service.HotWordSettingService;

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
	
	@Scheduled(cron = "0 0 0/1 * * ?")
	public void execHotWordSetRedis() {
		try{
			HotWordSetting set = hotWordSettingService.getHotWordSettingTask();
			if(set==null){
				log.info("没有任务需要执行");
				return;
			}
			Calendar cal = Calendar.getInstance();
			//获取系统小时
			cal.setTime(new Date());
			int sys_hour=cal.get(Calendar.HOUR_OF_DAY);
			//获取任务小时
			Date query_end = df.parse(set.getNext_publish_time());// 开始查询时间
			cal.setTime(query_end);
			int task_hour=cal.get(Calendar.HOUR_OF_DAY);
			if(sys_hour!=task_hour){
				log.info("未到定时器执行时间");
				return;
			}
			//发布redis
			boolean flag=hotWordService.publishToRedis();
			if(flag){
				log.info("发布redis成功");
			}else{
				log.info("发布redis失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
