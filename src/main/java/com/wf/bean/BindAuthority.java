package com.wf.bean;

/**
 * Created by syl on 2018/1/12.
 */

/**
 * 个人绑定机构参数类
 */
public class BindAuthority {
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
    private Integer downlaodLimit;
    //绑定个人继承权限
    private String bindAuthority;
    //修改前继承的权限（仅用于修改权限）
    private String previousAuthority;

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

    public Integer getDownlaodLimit() {
        return downlaodLimit;
    }

    public void setDownlaodLimit(Integer downlaodLimit) {
        this.downlaodLimit = downlaodLimit;
    }

    public String getBindAuthority() {
        return bindAuthority;
    }

    public void setBindAuthority(String bindAuthority) {
        this.bindAuthority = bindAuthority;
    }

    public String getPreviousAuthority() {
        return previousAuthority;
    }

    public void setPreviousAuthority(String previousAuthority) {
        this.previousAuthority = previousAuthority;
    }

    @Override
    public String toString() {
        return "BindAuthority{" +
                "openState=" + openState +
                ", userId='" + userId + '\'' +
                ", bindType=" + bindType +
                ", bindLimit=" + bindLimit +
                ", bindValidity=" + bindValidity +
                ", downlaodLimit=" + downlaodLimit +
                ", bindAuthority='" + bindAuthority + '\'' +
                ", previousAuthority='" + previousAuthority + '\'' +
                '}';
    }
}
