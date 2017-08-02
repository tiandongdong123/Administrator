package com.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.wf.bean.Mail;
/**
 * 邮件发送工具类
 * @author Administrator
 *
 */
public class MailUtil {
	//smtp
	private static String email_host;
	//发送人邮箱
	private static String email_Sender;
	//用户名
	private static String username;
	//密码
	private static String password;
	//配置文件名称
	private static String properites="email_phone.properties";
	
	static{
		//读取配置文件设置参数信息
		setEmail_host(SettingUtil.getPros(properites).getProperty("email.host"));
		setEmail_Sender(SettingUtil.getPros(properites).getProperty("email.sender"));
		setUsername(SettingUtil.getPros(properites).getProperty("email.username"));
		setPassword(SettingUtil.getPros(properites).getProperty("email.password"));
	}
	  
    public boolean send(Mail mail) {  
        // 发送email  
        HtmlEmail email = new HtmlEmail();
        try {  
            // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"  
            email.setHostName(getEmail_host());  
            // 字符编码集的设置  
            email.setCharset(Mail.ENCODEING);  
            // 收件人的邮箱  
            email.addTo(mail.getReceiver());  
            // 发送人的邮箱  
            email.setFrom(getEmail_Sender(), mail.getName());  
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码  
            email.setAuthentication(getUsername(), getPassword());  
            // 要发送的邮件主题  
            email.setSubject(mail.getSubject());  
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签  
            email.setMsg(mail.getMessage());  
            // 发送  
            email.send();  
            return true;  
        } catch (EmailException e) {  
            e.printStackTrace();  
            return false;  
        }
    }

	public static String getEmail_host() {
		return MailUtil.email_host;
	}

	public static void setEmail_host(String email_host) {
		MailUtil.email_host = email_host;
	}

	public static String getEmail_Sender() {
		return MailUtil.email_Sender;
	}

	public static void setEmail_Sender(String email_Sender) {
		MailUtil.email_Sender = email_Sender;
	}

	public static String getUsername() {
		return MailUtil.username;
	}

	public static void setUsername(String username) {
		MailUtil.username = username;
	}

	public static String getPassword() {
		return MailUtil.password;
	}

	public static void setPassword(String password) {
		MailUtil.password = password;
	}

    
}
