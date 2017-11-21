package com.wf.bean;

public class ProjectNumber {
    private String resourcePurchaseId;

    private String projectId;

    private String userId;

    private String validityStartTime;

    private String validityEndTime;

    private Integer totalNumber;

    private Integer surplusNumber;
    
    private Double totalMoney;

    private Double balanceMoney;

    public String getResourcePurchaseId() {
        return resourcePurchaseId;
    }

    public void setResourcePurchaseId(String resourcePurchaseId) {
        this.resourcePurchaseId = resourcePurchaseId == null ? null : resourcePurchaseId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getValidityStartTime() {
        return validityStartTime;
    }

    public void setValidityStartTime(String validityStartTime) {
        this.validityStartTime = validityStartTime;
    }

    public String getValidityEndTime() {
        return validityEndTime;
    }

    public void setValidityEndTime(String validityEndTime) {
        this.validityEndTime = validityEndTime;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getSurplusNumber() {
        return surplusNumber;
    }

    public void setSurplusNumber(Integer surplusNumber) {
        this.surplusNumber = surplusNumber;
    }
    
    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Double getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(Double balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

	@Override
	public String toString() {
		return "ProjectNumber [resourcePurchaseId=" + resourcePurchaseId
				+ ", projectId=" + projectId + ", userId=" + userId
				+ ", validityStartTime=" + validityStartTime
				+ ", validityEndTime=" + validityEndTime + ", totalNumber="
				+ totalNumber + ", surplusNumber=" + surplusNumber
				+ ", totalMoney=" + totalMoney + ", balanceMoney="
				+ balanceMoney + "]";
	}
}