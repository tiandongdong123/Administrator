package com.wf.bean;

public class UserIp {
    private String id;

    private String userId;

    private long beginIpAddressNumber;

    private long endIpAddressNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public long getBeginIpAddressNumber() {
        return beginIpAddressNumber;
    }

    public void setBeginIpAddressNumber(long beginIpAddressNumber) {
        this.beginIpAddressNumber = beginIpAddressNumber;
    }

    public long getEndIpAddressNumber() {
        return endIpAddressNumber;
    }

    public void setEndIpAddressNumber(long endIpAddressNumber) {
        this.endIpAddressNumber = endIpAddressNumber;
    }
}