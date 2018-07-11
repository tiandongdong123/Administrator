package com.wf.bean;

/**
 * Created by syl on 2018/1/12.
 */

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 个人绑定机构参数类
 */
public class BindAuthorityModel {
    //是否开通个人绑定机构状态
    private Boolean openState;
    //机构账号id
    private String userId;
    //开通时限起始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openBindStart;
    //开通时限结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date openBindEnd;
    //绑定模式   机构个人同时登录--0  机构登录--1  线下扫描--2
    private Integer bindType;
    //绑定个人上限
    private Integer bindLimit;
    //绑定个人账号有效期
    private Integer bindValidity;
    //绑定个人下载量上限/天
    private Integer downloadLimit;
    //绑定个人继承权限
    private String bindAuthority;
    //邮箱
    private String email;

    public Boolean getOpenState() {
        return openState;
    }

    public void setOpenState(Boolean openState) {
        this.openState = openState;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getOpenBindStart() {
        return openBindStart;
    }

    public void setOpenBindStart(Date openBindStart) {
        this.openBindStart = openBindStart;
    }

    public Date getOpenBindEnd() {
        return openBindEnd;
    }

    public void setOpenBindEnd(Date openBindEnd) {
        this.openBindEnd = openBindEnd;
    }

    public Integer getBindType() {
        return bindType;
    }

    public void setBindType(Integer bindType) {
        this.bindType = bindType;
    }

    public Integer getBindLimit() {
        return bindLimit;
    }

    public void setBindLimit(Integer bindLimit) {
        this.bindLimit = bindLimit;
    }

    public Integer getBindValidity() {
        return bindValidity;
    }

    public void setBindValidity(Integer bindValidity) {
        this.bindValidity = bindValidity;
    }

    public Integer getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(Integer downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public String getBindAuthority() {
        return bindAuthority;
    }

    public void setBindAuthority(String bindAuthority) {
        this.bindAuthority = bindAuthority;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "BindAuthorityModel{" +
                "openState=" + openState +
                ", userId='" + userId + '\'' +
                ", openBindStart=" + openBindStart +
                ", openBindEnd=" + openBindEnd +
                ", bindType=" + bindType +
                ", bindLimit=" + bindLimit +
                ", bindValidity=" + bindValidity +
                ", downloadLimit=" + downloadLimit +
                ", bindAuthority='" + bindAuthority + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
