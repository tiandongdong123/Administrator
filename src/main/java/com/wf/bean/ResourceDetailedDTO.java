package com.wf.bean;

import java.util.List;

public class ResourceDetailedDTO {

	//资源购买信息
	private String projectid;
	
	private String projectname;
	
	private String projectType;
	
	private String resourceType;

	private String validityStarttime;
	
	private String validityEndtime;
	
	private Double totalMoney;
	
	private String relatedIdAccountType;;
	
	private Integer purchaseNumber;
	
	private String validityStarttime2;
	
	private String validityEndtime2;
	// 是否试用 trical--试用，formal--正常
	private String mode;
	private String ischange;//购买资源是否转化
	
	private List<ResourceLimitsDTO> rldto;
	
	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	
	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getValidityStarttime() {
		return validityStarttime;
	}

	public void setValidityStarttime(String validityStarttime) {
		this.validityStarttime = validityStarttime;
	}

	public String getValidityEndtime() {
		return validityEndtime;
	}

	public String getValidityStarttime2() {
		return validityStarttime2;
	}

	public void setValidityStarttime2(String validityStarttime2) {
		this.validityStarttime2 = validityStarttime2;
	}

	public String getValidityEndtime2() {
		return validityEndtime2;
	}

	public void setValidityEndtime2(String validityEndtime2) {
		this.validityEndtime2 = validityEndtime2;
	}

	public void setValidityEndtime(String validityEndtime) {
		this.validityEndtime = validityEndtime;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getRelatedIdAccountType() {
		return relatedIdAccountType;
	}

	public void setRelatedIdAccountType(String relatedIdAccountType) {
		this.relatedIdAccountType = relatedIdAccountType;
	}

	public Integer getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(Integer purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getIschange() {
		return ischange;
	}

	public void setIschange(String ischange) {
		this.ischange = ischange;
	}

	public List<ResourceLimitsDTO> getRldto() {
		return rldto;
	}

	public void setRldto(List<ResourceLimitsDTO> rldto) {
		this.rldto = rldto;
	}

	@Override
	public String toString() {
		return "ResourceDetailedDTO [projectid=" + projectid + ", projectname="
				+ projectname + ", projectType=" + projectType
				+ ", resourceType=" + resourceType + ", validityStarttime="
				+ validityStarttime + ", validityEndtime=" + validityEndtime
				+ ", totalMoney=" + totalMoney + ", relatedIdAccountType="
				+ relatedIdAccountType + ", purchaseNumber=" + purchaseNumber
				+ ", validityStarttime2=" + validityStarttime2
				+ ", validityEndtime2=" + validityEndtime2
				+ ", mode=" + mode + ", ischange=" + ischange
				+ ", rldto=" + rldto+"]";
	}
}
