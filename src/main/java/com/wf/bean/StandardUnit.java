package com.wf.bean;

public class StandardUnit {

	private String userId;// 用户ID
	private String orgName;// 机构名称
	private String orgCode;// 机构code
	private String company;// 单位名称
	private String companySimp;// 单位名称简写
	private String createTime;// 创建时间
	private String updateTime;// 更新时间
	private String iPLimits;//ip限定

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanySimp() {
		return companySimp;
	}

	public void setCompanySimp(String companySimp) {
		this.companySimp = companySimp;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getiPLimits() {
		return iPLimits;
	}

	public void setiPLimits(String iPLimits) {
		this.iPLimits = iPLimits;
	}
}
