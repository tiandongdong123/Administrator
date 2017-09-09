package com.wf.bean;

public class OperationLogs {
	
	
	private Integer id;
	private String userId;//  用户id
	private String crateTime;// 创建记录时间
	private String opreation;// 操作类型
	private String person;
	private String reason;// 简述
	private String projectId;// 项目Id
	private String projectName;//项目名称
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCrateTime() {
		return crateTime;
	}
	public void setCrateTime(String crateTime) {
		this.crateTime = crateTime;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getOpreation() {
		return opreation;
	}
	public void setOpreation(String opreation) {
		this.opreation = opreation;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
