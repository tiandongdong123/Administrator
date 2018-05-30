package com.wf.bean;

public class UserBoughtItems {
	
	private Integer Id; // ID
	private String UserId; // 用户ID
	private String TransteroutType; // 购买项目资源类型
	private String Mode; // 购买项目类型：trical--试用，formal--正常
	private String Feature; // 功能分类：resource--资源，function--功能

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getTransteroutType() {
		return TransteroutType;
	}

	public void setTransteroutType(String transteroutType) {
		TransteroutType = transteroutType;
	}

	public String getMode() {
		return Mode;
	}

	public void setMode(String mode) {
		Mode = mode;
	}

	public String getFeature() {
		return Feature;
	}

	public void setFeature(String feature) {
		Feature = feature;
	}

}
