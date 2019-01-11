package com.wf.bean;

/**
 * 资讯标题查重
 */
public class JudgeMessageTitleParameter {
    private String messageId;
    private String title;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
