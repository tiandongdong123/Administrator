package com.wf.bean;

/**
 * @author Administrator
 *
 */
public class Authority {
	
	private String userId;
	
	private String authorityType;
	
	private String relatedIdAccountType;
	
	private String trial;
	
	private String partyAdmin;
	
	private String oldPartyAdmin;
	
	private String password;
	
	private String begintime;
	
	private String endtime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(String authorityType) {
		this.authorityType = authorityType;
	}
	
	public String getRelatedIdAccountType() {
		return relatedIdAccountType;
	}

	public void setRelatedIdAccountType(String relatedIdAccountType) {
		this.relatedIdAccountType = relatedIdAccountType;
	}

	public String getTrial() {
		return trial;
	}

	public void setTrial(String trial) {
		this.trial = trial;
	}
	
	public String getPartyAdmin() {
		return partyAdmin;
	}

	public void setPartyAdmin(String partyAdmin) {
		this.partyAdmin = partyAdmin;
	}

	public String getOldPartyAdmin() {
		return oldPartyAdmin;
	}

	public void setOldPartyAdmin(String oldPartyAdmin) {
		this.oldPartyAdmin = oldPartyAdmin;
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
	
}