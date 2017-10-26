package com.wf.bean;

import java.util.Date;

public class Standard {
	private boolean isBK;//是否包库
	private boolean isZJ;//是否是元数据+全文
	private String UserId;//用户Id
	private String Username;//用户名称
	private String UserEnName;//用户英文名称
	private Date StartTime;//元数据+全文开始时间
	private Date EndTime;//元数据+全文结束时间
	private Date BK_StartTime;//包库开始时间
	private Date BK_EndTime;//包库结束时间
	private String OrgName;//单位名称
	private String OrgCode;//机构名称
	private String CompanySimp;//机构单位简写
	private String[] IPLimits;//元数据+全文ip限定
	private String[] BK_IPLimits;//包库ip限定
	private String Rdptauth;//版权阅读打印
	private int Onlines;//在线用户数
	private int Copys;//副本数
	private int Prints;//打印总份数
	private int Sigprint;//单标准打印数
	
	public boolean isBK() {
		return isBK;
	}
	public void setBK(boolean isBK) {
		this.isBK = isBK;
	}
	public boolean isZJ() {
		return isZJ;
	}
	public void setZJ(boolean isZJ) {
		this.isZJ = isZJ;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getUserEnName() {
		return UserEnName;
	}
	public void setUserEnName(String userEnName) {
		UserEnName = userEnName;
	}
	public Date getStartTime() {
		return StartTime;
	}
	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}
	public Date getEndTime() {
		return EndTime;
	}
	public void setEndTime(Date endTime) {
		EndTime = endTime;
	}
	public Date getBK_StartTime() {
		return BK_StartTime;
	}
	public void setBK_StartTime(Date bK_StartTime) {
		BK_StartTime = bK_StartTime;
	}
	public Date getBK_EndTime() {
		return BK_EndTime;
	}
	public void setBK_EndTime(Date bK_EndTime) {
		BK_EndTime = bK_EndTime;
	}
	public String getOrgCode() {
		return OrgCode;
	}
	public void setOrgCode(String orgCode) {
		OrgCode = orgCode;
	}
	public String getOrgName() {
		return OrgName;
	}
	public void setOrgName(String orgName) {
		OrgName = orgName;
	}
	public String getCompanySimp() {
		return CompanySimp;
	}
	public void setCompanySimp(String companySimp) {
		CompanySimp = companySimp;
	}
	public String[] getIPLimits() {
		return IPLimits;
	}
	public void setIPLimits(String[] iPLimits) {
		IPLimits = iPLimits;
	}
	public String[] getBK_IPLimits() {
		return BK_IPLimits;
	}
	public void setBK_IPLimits(String[] bK_IPLimits) {
		BK_IPLimits = bK_IPLimits;
	}
	public String getRdptauth() {
		return Rdptauth;
	}
	public void setRdptauth(String rdptauth) {
		Rdptauth = rdptauth;
	}
	public int getOnlines() {
		return Onlines;
	}
	public void setOnlines(int onlines) {
		Onlines = onlines;
	}
	public int getCopys() {
		return Copys;
	}
	public void setCopys(int copys) {
		Copys = copys;
	}
	public int getPrints() {
		return Prints;
	}
	public void setPrints(int prints) {
		Prints = prints;
	}
	public int getSigprint() {
		return Sigprint;
	}
	public void setSigprint(int sigprint) {
		Sigprint = sigprint;
	}
	
}