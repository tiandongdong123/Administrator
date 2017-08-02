package com.wanfangdata.model;

import java.util.Date;

/**
 * 机构账户基类
 */
public class UserAccount {
    /**
     * 用户名
     */
    private String userId;
    /**
     * 账户类型
     */
    private String payChannelId;

    /**
     * 机构名称
     */
    private String organName;
    /**
     * 生效时间
     */
    private Date beginDateTime;
    /**
     * 失效时间
     */
    private Date endDateTime;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(String payChannelId) {
        this.payChannelId = payChannelId;
    }

    public Date getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(Date beginDateTime) {
        this.beginDateTime = beginDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getOrganName(){
        return organName;
    }
    public void setOrganName(String organName){
        this.organName = organName;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "userId='" + userId + '\'' +
                ", payChannelId='" + payChannelId + '\'' +
                ", organName='" + organName + '\'' +
                ", beginDateTime=" + beginDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
