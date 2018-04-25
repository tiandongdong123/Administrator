package com.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.wf.bean.Mail;

public class SendMail2 {
	//配置文件名称
	private static String properites="email_phone.properties";
	private static Logger log = Logger.getLogger(SendMail2.class);
	
	private String host = ""; // smtp服务器
	private String sender=""; //发件人地址
	private String receiver = ""; // 收件人地址
	private String affixName = ""; // 附件名称
	private String user = ""; // 发件人地址/用户名
	private String pwd = ""; // 密码
	private String subject = ""; // 邮件标题
	private String content = "";// 邮箱内容
	private String affix ="";//附件路径
	
	public boolean sendEmail(Mail mail) {
		// 1、配置参数
		host = SettingUtil.getPros(properites).getProperty("email.host");
		sender = SettingUtil.getPros(properites).getProperty("email.sender");
		user = SettingUtil.getPros(properites).getProperty("email.username");
		pwd = SettingUtil.getPros(properites).getProperty("email.password");
		receiver = mail.getReceiver();
		subject = mail.getSubject();
		affix=mail.getAffix();
		affixName = mail.getAffixName();
		content = mail.getMessage();
		// 2、发送邮件
		return this.send();
	}
	
    public boolean send() {
    	boolean flag=true;
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);
        String[] receivers=receiver.replace(" ", "").split(";");
        for(String rec:receivers){
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(sender));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(rec));
                message.setSubject(subject);
                Multipart multipart = new MimeMultipart();
                BodyPart contentPart = new MimeBodyPart();
                contentPart.setText(content);
                multipart.addBodyPart(contentPart);
                // 添加附件
                if(affix!=null){
                	DataSource source = new FileDataSource(affix);
                    BodyPart mbp = new MimeBodyPart();
                    // 添加附件的内容
                    mbp.setDataHandler(new DataHandler(source));
                    // 添加附件的标题
                    // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                    mbp.setFileName("=?UTF-8?B?" + enc.encode(affixName.getBytes("UTF-8")) + "?=");
                    multipart.addBodyPart(mbp);
                }
                message.setContent(multipart);
                message.saveChanges();
                Transport transport = session.getTransport("smtp");
                transport.connect(host, user, pwd);
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                log.info(rec+"邮件发送成功");
            } catch (Exception e) {
            	if(!flag){
            		flag=false;
            	}
            	log.error("邮件发送失败", e);
            }
        }
        return flag;
    }
}