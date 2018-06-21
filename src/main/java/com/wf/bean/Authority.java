package com.wf.bean;

/**
 * @author Administrator
 *
 */
public class Authority {
	
	private String userId;
	
	private boolean trial;
	
	private String partyAdmin;
	
	private String password;
	
	private String begintime;
	
	private String endtime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean getTrial() {
		return trial;
	}

	public void setTrial(boolean trial) {
		this.trial = trial;
	}
	
	public String getPartyAdmin() {
		return partyAdmin;
	}

	public void setPartyAdmin(String partyAdmin) {
		this.partyAdmin = partyAdmin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	@Override
	public String toString() {
		return "Authority [userId=" + userId + ", trial=" + trial + ", partyAdmin="
				+ partyAdmin + ", password=" + password + ", begintime=" + begintime
				+ ", endtime=" + endtime + "]";
	}
	
}
