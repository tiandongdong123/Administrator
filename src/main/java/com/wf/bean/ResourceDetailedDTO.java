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
	
	private Integer purchaseNumber;
	
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

	public void setValidityEndtime(String validityEndtime) {
		this.validityEndtime = validityEndtime;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(Integer purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public List<ResourceLimitsDTO> getRldto() {
		return rldto;
	}

	public void setRldto(List<ResourceLimitsDTO> rldto) {
		this.rldto = rldto;
	}
    
}
