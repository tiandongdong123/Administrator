package com.wf.bean;

import java.util.Date;

public class BindAccountModel {
    //机构id
    private String institutionId;
    //绑定模式
    private Integer bindType;
    //绑定个人上限
    private Integer bindValidity;
    //绑定个人下载量上限/天
    private Integer bindLimit;
    //绑定个人继承权限
    private String authority;
    //绑定个人账号
    private String personId;
    //绑定时间
    private Date bindTime;
    //失效时间
    private Date invalidTime;

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public Integer getBindType() {
        return bindType;
    }

    public void setBindType(Integer bindType) {
        this.bindType = bindType;
    }

    public Integer getBindValidity() {
        return bindValidity;
    }

    public void setBindValidity(Integer bindValidity) {
        this.bindValidity = bindValidity;
    }

    public Integer getBindLimit() {
        return bindLimit;
    }

    public void setBindLimit(Integer bindLimit) {
        this.bindLimit = bindLimit;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    @Override
    public String toString() {
        return "BindAccountModel{" +
                "institutionId='" + institutionId + '\'' +
                ", bindType=" + bindType +
                ", bindValidity=" + bindValidity +
                ", bindLimit=" + bindLimit +
                ", authority='" + authority + '\'' +
                ", personId='" + personId + '\'' +
                ", bindTime=" + bindTime +
                ", invalidTime=" + invalidTime +
                '}';
    }
}
