package com.wf.bean;

import java.io.Serializable;

public class Notes implements Serializable{
	private String id;
	private String noteNum;
	private String resourceNum;
	private String resourceName;
	private String resourceType;
	private String noteContent;
	private String userName;
	private String userId;
	private String noteDate;
	private String dataState;
	private String complaintStatus;
	private String preliminaryOpinions;
	private String appealReason;
	private String finalOpinion;
	private Integer handlingStatus;
	
	public Integer getHandlingStatus() {
		return handlingStatus;
	}
	public void setHandlingStatus(Integer handlingStatus) {
		this.handlingStatus = handlingStatus;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPreliminaryOpinions() {
		return preliminaryOpinions;
	}
	public void setPreliminaryOpinions(String preliminaryOpinions) {
		this.preliminaryOpinions = preliminaryOpinions;
	}
	public String getAppealReason() {
		return appealReason;
	}
	public void setAppealReason(String appealReason) {
		this.appealReason = appealReason;
	}
	public String getFinalOpinion() {
		return finalOpinion;
	}
	public void setFinalOpinion(String finalOpinion) {
		this.finalOpinion = finalOpinion;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getNoteNum() {
		return noteNum;
	}
	public void setNoteNum(String noteNum) {
		this.noteNum = noteNum;
	}
	public String getResourceNum() {
		return resourceNum;
	}
	public void setResourceNum(String resourceNum) {
		this.resourceNum = resourceNum;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNoteDate() {
		return noteDate;
	}
	public void setNoteDate(String noteDate) {
		this.noteDate = noteDate;
	}
	public String getDataState() {
		return dataState;
	}
	public void setDataState(String dataState) {
		this.dataState = dataState;
	}
	public String getComplaintStatus() {
		return complaintStatus;
	}
	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}
	
}
