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
	
	private String totalMoney;

	private String beforeTotalMoney;
	
	private String relatedIdAccountType;;
	
	private String purchaseNumber;

	private String beforePurchaseNumber;
	
	private String validityStarttime2;
	
	private String validityEndtime2;
	// 是否试用 trical--试用，formal--正常
	private String mode;

	private String beforeMode;

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

	public String getRelatedIdAccountType() {
		return relatedIdAccountType;
	}

	public void setRelatedIdAccountType(String relatedIdAccountType) {
		this.relatedIdAccountType = relatedIdAccountType;
	}

	public String getMode() {
		return mode;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(String purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getBeforeMode() {
		return beforeMode;
	}

	public void setBeforeMode(String beforeMode) {
		this.beforeMode = beforeMode;
	}

	public List<ResourceLimitsDTO> getRldto() {
		return rldto;
	}

	public void setRldto(List<ResourceLimitsDTO> rldto) {
		this.rldto = rldto;
	}

	public String getBeforeTotalMoney() {
		return beforeTotalMoney;
	}

	public void setBeforeTotalMoney(String beforeTotalMoney) {
		this.beforeTotalMoney = beforeTotalMoney;
	}

	public String getBeforePurchaseNumber() {
		return beforePurchaseNumber;
	}

	public void setBeforePurchaseNumber(String beforePurchaseNumber) {
		this.beforePurchaseNumber = beforePurchaseNumber;
	}

	@Override
	public String toString() {
		return "ResourceDetailedDTO{" +
				"projectid='" + projectid + '\'' +
				", projectname='" + projectname + '\'' +
				", projectType='" + projectType + '\'' +
				", resourceType='" + resourceType + '\'' +
				", validityStarttime='" + validityStarttime + '\'' +
				", validityEndtime='" + validityEndtime + '\'' +
				", totalMoney='" + totalMoney + '\'' +
				", beforeTotalMoney='" + beforeTotalMoney + '\'' +
				", relatedIdAccountType='" + relatedIdAccountType + '\'' +
				", purchaseNumber='" + purchaseNumber + '\'' +
				", beforePurchaseNumber='" + beforePurchaseNumber + '\'' +
				", validityStarttime2='" + validityStarttime2 + '\'' +
				", validityEndtime2='" + validityEndtime2 + '\'' +
				", mode='" + mode + '\'' +
				", beforeMode='" + beforeMode + '\'' +
				", rldto=" + rldto +
				'}';
	}
}
