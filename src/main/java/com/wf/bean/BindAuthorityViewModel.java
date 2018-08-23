package com.wf.bean;

import java.util.Date;

public class BindAuthorityViewModel {

    //是否开通个人绑定机构状态
    private Boolean openState;
    //机构账号id
    private String userId;
    //绑定模式
    private String bindType;
    //绑定个人上限
    private Integer bindLimit;
    //绑定个人账号有效期
    private Integer bindValidity;
    //绑定个人下载量上限/天
    private Integer downloadLimit;
    //绑定个人继承权限
    private String bindAuthority;

    //开通时限开始时间
    private Date openTimeLimitState;

    //开通时限结束时间
    private Date openTimeLimitEnd;

    //二维码邮箱
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

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
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

    public Date getOpenTimeLimitState() {
        return openTimeLimitState;
    }

    public void setOpenTimeLimitState(Date openTimeLimitState) {
        this.openTimeLimitState = openTimeLimitState;
    }

    public Date getOpenTimeLimitEnd() {
        return openTimeLimitEnd;
    }

    public void setOpenTimeLimitEnd(Date openTimeLimitEnd) {
        this.openTimeLimitEnd = openTimeLimitEnd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "BindAuthorityViewModel{" +
                "openState=" + openState +
                ", userId='" + userId + '\'' +
                ", bindType='" + bindType + '\'' +
                ", bindLimit=" + bindLimit +
                ", bindValidity=" + bindValidity +
                ", downloadLimit=" + downloadLimit +
                ", bindAuthority='" + bindAuthority + '\'' +
                ", openTimeLimitState=" + openTimeLimitState +
                ", openTimeLimitEnd=" + openTimeLimitEnd +
                ", email='" + email + '\'' +
                '}';
    }
}
