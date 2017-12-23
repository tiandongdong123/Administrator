package com.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wf.service.HotWordService;

/**
 * 热搜词发送至redis
 * @author ouyang
 *
 */

@Component
public class HotWordSetRedisJob {
	
	@Autowired
	private HotWordService hotWordService;
	
	private static Logger log = Logger.getLogger(HotWordSetRedisJob.class);
	
	@Scheduled(cron = "0 0 8 * * ?")
	public void execHotWordSetRedis() {
		//发布redis
		boolean flag=hotWordService.publishToRedis();
		if(flag){
			log.info("发布redis成功");
		}else{
			log.info("发布redis失败");
		}
	}

}
