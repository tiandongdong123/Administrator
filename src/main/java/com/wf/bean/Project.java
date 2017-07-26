package com.wf.bean;

public class Project {
	
    private String id;

    private String name;

    private Integer authorizedUserType;

    private String payWallet;

    private String detailSetting;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAuthorizedUserType() {
        return authorizedUserType;
    }

    public void setAuthorizedUserType(Integer authorizedUserType) {
        this.authorizedUserType = authorizedUserType;
    }

    public String getPayWallet() {
        return payWallet;
    }

    public void setPayWallet(String payWallet) {
        this.payWallet = payWallet == null ? null : payWallet.trim();
    }

    public String getDetailSetting() {
        return detailSetting;
    }

    public void setDetailSetting(String detailSetting) {
        this.detailSetting = detailSetting == null ? null : detailSetting.trim();
    }
}