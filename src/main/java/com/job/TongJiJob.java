package com.job;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.utils.SendMail2;
import com.utils.SettingUtil;
import com.utils.jdbcUtils;
import com.wf.bean.Mail;
import com.wf.service.AheadUserService;
import com.wf.service.InsWarningService;
import com.xxl.conf.core.XxlConfClient;

@Component
public class TongJiJob {
	
	private static Logger log = Logger.getLogger(TongJiJob.class);
	private static String properites="email_phone.properties";
	
	@Autowired
	private InsWarningService ins;
	@Autowired
	private AheadUserService aheadUserService;

	
	//每凌晨1点执行(检查是否需要发送邮件)
	@Scheduled(cron = "0 0 8 * * ?")
	public void sendEmail() {
		try{
			String execIp = XxlConfClient.get("wf-admin.hotWordExecIP", null);
			InetAddress addr = InetAddress.getLocalHost();
			String hostip = addr.getHostAddress();
			if (!StringUtils.isEmpty(execIp)&&!"127.0.0.1".equals(execIp) && !hostip.equals(execIp)) {
				log.info("服务器" + hostip + "无需发布数据,有权限执行的ip:" + execIp);
				return;
			}
			log.info("开始发送统计邮件");
			String usersql="select userType,times,count(1) nums from (select userType,DATE_FORMAT(registration_time,'%Y-%m-%d') times from `user` where registration_time>='";
			usersql+=this.getYesterDay()+ "' and registration_time<'"+ this.getDay()+ "') e group by e.userType,times";
			List<Map<String, String>> userList=jdbcUtils.getConnect(usersql);
			Map<String,String> userMap=new HashMap<String,String>();
			userMap.put("日期", this.getYesterDay());
			userMap.put("个人注册数量", "0");
			userMap.put("机构注册数量", "0");
			userMap.put("访客数", "0");
			userMap.put("检索量", "0");
			userMap.put("浏览量", "0");
			userMap.put("下载量", "0");
			//1、统计个人用户数、机构用户数量
			for(Map<String,String> map:userList){
				String userType=map.get("userType");
				String nums=map.get("nums");
				if("0".equals(userType)){
					userMap.put("个人注册数量", nums);
				}else if("2".equals(userType)||"3".equals(userType)){
					userMap.put("机构注册数量", String.valueOf(Integer.parseInt(userMap.get("机构注册数量"))+Integer.parseInt(nums)));
				}
			}
			//2、查询访客数量
			String viewsql="select sum(numbers) nums from rs_website_survey_basic_quota_daily where type=3 and date ='"+this.getYesterDay()+"'";
			List<Map<String, String>> viewList=jdbcUtils.getConnectStatistics(viewsql);
			for(Map<String,String> map:viewList){
				userMap.put("访客数", map.get("nums"));
			}
			//3、查询下载量、浏览量、检索量
			String downsql="select date,sum(search_cnt) search,sum(browse_cnt) browse,sum(down_cnt) down from rs_search_browse_cnt_daily where date>='"+this.getYesterDay()+"' and date<'"+this.getDay()+"' group by date";
			List<Map<String, String>> downList=jdbcUtils.getConnectStatistics(downsql);
			for(Map<String,String> map:downList){
				userMap.put("检索量", map.get("search"));
				userMap.put("浏览量", map.get("browse"));
				userMap.put("下载量", map.get("down"));
			}
			// 4、发送预警邮件
			Mail mail=new Mail();
			String revicer = SettingUtil.getPros(properites).getProperty("remail.revicer");
			mail.setReceiver(revicer);
			mail.setName("后台管理");
			mail.setSubject("新平台访问次数统计");
			StringBuffer msg=new StringBuffer("{");
			msg.append("日期="+userMap.get("日期")+",");
			msg.append("个人注册数量="+userMap.get("个人注册数量")+",");
			msg.append("机构注册数量="+userMap.get("机构注册数量")+",");
			msg.append("访客数="+userMap.get("访客数")+",");
			msg.append("检索量="+userMap.get("检索量")+",");
			msg.append("浏览量="+userMap.get("浏览量")+",");
			msg.append("下载量="+userMap.get("下载量")+",");
			msg.append("}");
			log.info("统计数据是:"+msg.toString());
			mail.setMessage(msg.toString());
			SendMail2 util=new SendMail2();
			boolean flag=util.sendEmail(mail);
			if(flag){
				log.info("发送成功");
			}else{
				log.info("发送失败");
			}
			log.info("统计邮件发送完毕");
		}catch(Exception e){
			log.error("邮件发送异常", e);
		}
	}
	
	
	// 获取昨天的日期
	private String getYesterDay() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return fm.format(cal.getTime());
	}
	
	// 获取昨天的日期
	private String getDay() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
		Calendar cal=Calendar.getInstance();
		return fm.format(cal.getTime());
	}
	
}
