package com.wf.bean;

import java.util.Date;

public class BindAccountModel {
    //机构id
    private String institutionId;
    //绑定模式
    private String bindType;
    //绑定个人上限
    private Integer bindLimit;
    //绑定有效期
    private Integer bindValidity;
    //绑定个人下载量上限/天
    private Integer downloadLimit;
    //绑定个人继承权限
    private String authority;
    //绑定个人账号
    private String personId;
    //绑定时间
    private String bindTime;
    //失效时间
    private String invalidTime;

    public Integer getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(Integer downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
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

    public String getBindTime() {
        return bindTime;
    }

    public void setBindTime(String bindTime) {
        this.bindTime = bindTime;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    @Override
    public String toString() {
        return "BindAccountModel{" +
                "institutionId='" + institutionId + '\'' +
                ", bindType='" + bindType + '\'' +
                ", bindLimit=" + bindLimit +
                ", bindValidity=" + bindValidity +
                ", downloadLimit=" + downloadLimit +
                ", authority='" + authority + '\'' +
                ", personId='" + personId + '\'' +
                ", bindTime='" + bindTime + '\'' +
                ", invalidTime='" + invalidTime + '\'' +
                '}';
    }
}
