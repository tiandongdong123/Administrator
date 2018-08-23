package com.utils;


import com.wanfangdata.grpcchannel.BindAccountChannel;
import com.wanfangdata.rpc.bindauthority.BindType;
import com.wanfangdata.rpc.bindauthority.CodeDetail;
import com.wanfangdata.rpc.bindauthority.GetCodeRequest;
import com.wanfangdata.rpc.bindauthority.GetCodeResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;

public class WFMailUtil {
    private static Logger log = Logger.getLogger(WFMailUtil.class);
    private String FROM;//发件人的email
    private String PWD;//发件人密码邮箱
    private String HOST;//发送邮件服务器的信息
    private String SMTP;//简单邮件传输协议
    private String TITLE;//标题

    public WFMailUtil(String FROM, String PWD, String TITLE, String HOST, String SMTP) {
        this.FROM = FROM;
        this.PWD = PWD;
        this.TITLE = TITLE;
        this.HOST = HOST;
        this.SMTP = SMTP;
    }

    /**
     * 将二维码发送给指定的邮箱
     *
     * @param bindEmail          收件人
     * @param userIdList         机构账号id集合
     * @param bindAccountChannel 个人绑定机构权限channel类（个人权限）
     * @return
     */
    public Boolean sendQRCodeMail(String bindEmail, List<String> userIdList, BindAccountChannel bindAccountChannel) {
        try {
            log.info("开始生成二维码文件：userIdList" + userIdList.toString() + ",bindEmail:" + bindEmail);
            //生成二维码文件
            List<String> qrCodeList = new ArrayList<>();
            for (String userId : userIdList) {
                CodeDetail codeDetail = CodeDetail.newBuilder().setBindId(userId).setBindType(BindType.LINE_SCAN).build();
                GetCodeRequest codeRequest = GetCodeRequest.newBuilder().addCodeDetails(codeDetail).build();
                GetCodeResponse codeResponse = bindAccountChannel.getBlockingStub().getQRCode(codeRequest);
                String url = codeResponse.getCiphertext();
                log.info("生成的二维码字符串：url" + url);
                //生成二维码转成BASE64格式
                String qrCode = ImgUtil.imgToBase64(url);
                qrCodeList.add(qrCode);
            }
            log.info("将二维码转成BASE64格式：qrCodeList" + qrCodeList.toString() + ",bindEmail:" + bindEmail
                    + ",userIdList:" + userIdList.toString());
            activateMail(bindEmail, userIdList, qrCodeList);
            log.info("二维码发送至指定的邮箱成功：userIdList" + userIdList.toString() + ",bindEmail:" + bindEmail);
            return true;
        } catch (Exception e) {
            log.error("发送邮件失败，userIdList" + userIdList.toString(), e);
            return false;
        }
    }

    /**
     * 激活邮箱
     *
     * @param bindEmail  收件人邮箱
     * @param userIdList 机构账号id集合
     * @param qrCodeList 二维码集合
     * @throws MessagingException
     */
    public void activateMail(String bindEmail, List<String> userIdList, List<String> qrCodeList) throws MessagingException {
        String pagePath = this.getClass().getResource("/").getPath() + "bindcodetemplate.html";
        String content = "";
        int number = 0;
        for (int i = 0; i < userIdList.size(); i++) {
            if (userIdList.size() > 1) {
                number = i + 1;
            }
            String currentContent = this.getMailContent(pagePath, userIdList.get(i), qrCodeList.get(i), number);
            content = content + currentContent;

        }
        sendMail(bindEmail, TITLE, content);
    }

    /**
     * mail发送内容
     *
     * @param pagePath 模板地址
     * @param userId   机构账号id
     * @param QRCode   二维码
     * @return
     */
    private String getMailContent(String pagePath, String userId, String QRCode, int number) {
        log.info("编辑邮箱发送内容：pagePath" + pagePath + ",userId:" + userId + ",QRCode:" + QRCode);
        String resultContent = this.getFileContent(pagePath, "UTF-8");
        if (StringUtils.isNotBlank(resultContent)) {
            resultContent = resultContent.replaceAll("\\$\\{userId\\}", userId);
            resultContent = resultContent.replaceAll("\\$\\{QRCode\\}", QRCode);
            if (number == 0) {
                resultContent = resultContent.replaceAll("\\$\\{number\\}", "");
                resultContent = resultContent.replaceAll("\\$\\{hello\\}", "您好!");
            }
            if (number == 1) {
                resultContent = resultContent.replaceAll("\\$\\{hello\\}", "您好!");
                resultContent = resultContent.replaceAll("\\$\\{number\\}", number + "、");
            }
            else{
                resultContent = resultContent.replaceAll("\\$\\{hello\\}", "");
                resultContent = resultContent.replaceAll("\\$\\{number\\}", number + "、");
            }
        }
        log.info("邮箱发送内容：resultContent" + resultContent);
        return resultContent;
    }


    /**
     * 发送邮件
     *
     * @param to      接收人
     * @param title   主题
     * @param content 发送内容
     * @throws MessagingException 异常
     */
    private void sendMail(String to, String title, String content) throws MessagingException {
        Properties props = new Properties();//可以加载一个配置文件
        //使用smtp 简单邮件传输协议
        props.put("mail.smtp.host", HOST);//存储发送邮件服务器的信息
        props.put("mail.smtp.auth", "true");//同时通过验证
        Session session = Session.getInstance(props);//根据属性新建一个邮件绘画
        session.setDebug(true);//打印一些调试信息
        MimeMessage message = new MimeMessage(session);//由邮件会话新建一个消息对象
        message.setFrom(new InternetAddress(FROM));//设置发件人的地址
        //设置收件人，并设置其接收类型为TO
        message.setRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(to)));
        message.setSubject(title);//设置标题
        //设置信件内容
        //message.setText(""); //发送纯文本邮件
        message.setContent(content, "text/html;charset=utf-8");//发送html邮件
        message.setSentDate(new Date());//设置发信时间
        message.saveChanges();//存储邮件信息
        //发送邮件
        Transport transport = session.getTransport(SMTP);
        transport.connect(FROM, PWD);
        //发送邮件，其中第二个参数是所有已设好的收件人地址

        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    /**
     * 获取文件内容
     *
     * @param pagePath   模板地址
     * @param fileFormat 文件格式
     * @return
     */
    private String getFileContent(String pagePath, String fileFormat) {
        String resultContent = "";

        File file = new File(pagePath);
        FileInputStream fileInputStream = null;
        InputStreamReader is = null;
        BufferedReader reader = null;
        try {
            fileInputStream = new FileInputStream(file);
            is = new InputStreamReader(fileInputStream, fileFormat);
            reader = new BufferedReader(is);
            String readLine = reader.readLine();
            while (null != readLine) {
                resultContent += readLine;
                readLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            log.error("文件未找到！", e);
        } catch (IOException e) {
            log.error("读取文件出错", e);
        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }

            } catch (IOException e) {
                log.error("流关闭失败!", e);
            }
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("流关闭失败!", e);
            }
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                log.error("流关闭失败!", e);
            }
        }
        return resultContent;
    }


}
