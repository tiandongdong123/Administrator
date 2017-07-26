package com.utils;

import java.io.OutputStream;
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

import com.wf.bean.Mail;

public class SendMail2 {
	//配置文件名称
	private static String properites="email_phone.properties";
	
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
    	boolean flag=false;
        Properties props = new Properties();
        // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", host);
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证
        props.put("mail.smtp.auth", "true");
        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);
        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(false);

        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(sender));
            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            // 加载标题
            message.setSubject(subject);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(content);
            multipart.addBodyPart(contentPart);
            // 添加附件
            if(affix!=null){
                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(affix);
                // 添加附件的内容
                messageBodyPart.setDataHandler(new DataHandler(source));
                // 添加附件的标题
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                messageBodyPart.setFileName("=?GBK?B?" + enc.encode(affixName.getBytes()) + "?=");
                multipart.addBodyPart(messageBodyPart);
            }
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 连接服务器的邮箱
            transport.connect(host, user, pwd);
            // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}