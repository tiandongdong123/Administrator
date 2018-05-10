package com.job;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wf.bean.WarningInfo;
import com.wf.service.AheadUserService;
import com.wf.service.InsWarningService;
import com.xxl.conf.core.XxlConfClient;

@Component
public class SendEmailJob {
	
	private static Logger log = Logger.getLogger(SendEmailJob.class);
	
	@Autowired
	private InsWarningService ins;
	@Autowired
	private AheadUserService aheadUserService;
	
	//每凌晨1点执行(检查是否需要发送邮件)
	@Scheduled(cron = "0 0 1 * * ?")
	public void sendEmail() {
		try{
			log.info("开始发送账号预警邮件");
			InetAddress addr = InetAddress.getLocalHost();
			String hostip = addr.getHostAddress();
			String execIp = XxlConfClient.get("wf-admin.inswarningIP", null);
			if (!StringUtils.isEmpty(execIp)&&!"127.0.0.1".equals(execIp) && !hostip.equals(execIp)) {
				log.info("服务器" + hostip + "无需发布邮件,有权限执行的ip:" + execIp);
				return;
			}
			WarningInfo war = aheadUserService.findWarning();
			int remindTime=war.getRemindtime();
			SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.DATE, -remindTime);
			System.out.println(fm.format(cal.getTime()));
			Date now=fm.parse(fm.format(cal.getTime()));
			if(now.getTime()>=war.getExec_time().getTime()){
				boolean flag=ins.sendMail(war); 
				if(flag){
					log.info("发送成功");
				}else{
					log.info("发送失败");
				}
				//无论发送成功还是失败都要修改下次执行时间
				cal.add(Calendar.DATE, remindTime);
				war.setExec_time(cal.getTime());
				aheadUserService.updateWarning(war.getAmountthreshold(), war.getDatethreshold(), war.getRemindtime(), war.getRemindemail(), war.getCountthreshold(), war.getExec_time());
			}else{
				log.info("今天无需发送邮件提供,还差"+(war.getExec_time().getTime()-now.getTime())/(3600*24*1000)+"天");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
