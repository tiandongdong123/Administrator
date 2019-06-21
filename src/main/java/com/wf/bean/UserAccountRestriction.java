package com.wf.bean;

import java.util.Date;

public class UserAccountRestriction {
    private String userId;

    private Integer upperlimit;

    private Integer pConcurrentnumber;

    private Integer sConcurrentnumber;

    private Integer chargebacks;

    private Integer downloadupperlimit;
    
    private String sIsTrial;  //机构子账号是否试用
	
	private Date sBegintime;  //机构子账号开始时间
	
	private Date sEndtime;  //机构子账号结束时间
	
	
	

    public String getsIsTrial() {
		return sIsTrial;
	}

	public void setsIsTrial(String sIsTrial) {
		this.sIsTrial = sIsTrial;
	}

	public Date getsBegintime() {
		return sBegintime;
	}

	public void setsBegintime(Date sBegintime) {
		this.sBegintime = sBegintime;
	}

	public Date getsEndtime() {
		return sEndtime;
	}

	public void setsEndtime(Date sEndtime) {
		this.sEndtime = sEndtime;
	}

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

	@Override
	public String toString() {
		return "UserAccountRestriction [userId=" + userId + ", upperlimit="
				+ upperlimit + ", pConcurrentnumber=" + pConcurrentnumber
				+ ", sConcurrentnumber=" + sConcurrentnumber + ", chargebacks="
				+ chargebacks + ", downloadupperlimit=" + downloadupperlimit
				+ "]";
	}
    
}