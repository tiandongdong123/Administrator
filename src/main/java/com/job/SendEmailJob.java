package com.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.wf.bean.WarningInfo;
import com.wf.service.AheadUserService;
import com.wf.service.InsWarningService;

@Component
public class SendEmailJob {
	
	private static Logger log = Logger.getLogger(SendEmailJob.class);
	
	@Autowired
	private InsWarningService ins;
	@Autowired
	private AheadUserService aheadUserService;

	private int days=1;
	
	//每凌晨1点执行(检查是否需要发送邮件)
	@Scheduled(cron = "0 0/5 * * * ?")
	public void sendEmail() {
		log.info("开始发送邮件");
		WarningInfo war = aheadUserService.findWarning();
		int remindTime=war.getRemindtime();
		if(days==remindTime){
			boolean flag=ins.sendMail(war);
			if(flag){
				log.info("发送成功");
				days=1;//发送成功后设置为1
			}else{
				log.info("发送失败");
			}
		}else{
			log.info("今天无需发送邮件提供");
			days++;
		}
	}
	
}
