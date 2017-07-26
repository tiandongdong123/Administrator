package com.wf.bean;

public class UserAccountRestriction {
    private String userId;

    private Integer upperlimit;

    private Integer pConcurrentnumber;

    private Integer sConcurrentnumber;

    private Integer chargebacks;

    private Integer downloadupperlimit;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(Integer upperlimit) {
        this.upperlimit = upperlimit;
    }

    public Integer getpConcurrentnumber() {
        return pConcurrentnumber;
    }

    public void setpConcurrentnumber(Integer pConcurrentnumber) {
        this.pConcurrentnumber = pConcurrentnumber;
    }

    public Integer getsConcurrentnumber() {
        return sConcurrentnumber;
    }

    public void setsConcurrentnumber(Integer sConcurrentnumber) {
        this.sConcurrentnumber = sConcurrentnumber;
    }

    public Integer getChargebacks() {
        return chargebacks;
    }

    public void setChargebacks(Integer chargebacks) {
        this.chargebacks = chargebacks;
    }

    public Integer getDownloadupperlimit() {
        return downloadupperlimit;
    }

    public void setDownloadupperlimit(Integer downloadupperlimit) {
        this.downloadupperlimit = downloadupperlimit;
    }
}