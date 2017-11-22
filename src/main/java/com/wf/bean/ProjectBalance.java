package com.wf.bean;

public class ProjectBalance {
    private String id;

    private String projectId;

    private String userId;

    private String validityStartTime;

    private String validityEndTime;

    private Double totalMoney;

    private Double balanceMoney;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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
		return "ProjectBalance [id=" + id + ", projectId=" + projectId
				+ ", userId=" + userId + ", validityStartTime="
				+ validityStartTime + ", validityEndTime=" + validityEndTime
				+ ", totalMoney=" + totalMoney + ", balanceMoney="
				+ balanceMoney + "]";
	}
}