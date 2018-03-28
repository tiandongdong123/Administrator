package com.wf.bean;

/**
 * Created by syl on 2018/1/12.
 */

/**
 * 个人绑定机构参数类
 */
public class BindAuthorityModel {
    //是否开通个人绑定机构状态
    private Boolean openState;
    //机构账号id
    private String userId;
    //绑定模式
    private Integer bindType;
    //绑定个人上限
    private Integer bindLimit;
    //绑定个人账号有效期
    private Integer bindValidity;
    //绑定个人下载量上限/天
    private Integer downloadLimit;
    //绑定个人继承权限
    private String bindAuthority;

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

    @Override
    public String toString() {
        return "BindAuthorityModel{" +
                "openState=" + openState +
                ", userId='" + userId + '\'' +
                ", bindType=" + bindType +
                ", bindLimit=" + bindLimit +
                ", bindValidity=" + bindValidity +
                ", downloadLimit=" + downloadLimit +
                ", bindAuthority='" + bindAuthority + '\'' +
                '}';
    }
}
