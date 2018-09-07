package com.wf.bean.importSolr;

public class UserRoleRequest {
	
	private Integer ChildGroupLimit;
	private Integer ChildGroupConcurrent;
	private Integer GroupConcurrent;
	private Integer ChildGroupDownloadLimit;
	private Integer ChildGroupPayment;
	private String AppStartTime;
	private String AppEndTime;

	private String WeChatStartTime;
	private String WeChatEndTime;
	private String Email4WeChat;

	public Integer getChildGroupLimit() {
		return ChildGroupLimit;
	}

	public void setChildGroupLimit(Integer childGroupLimit) {
		ChildGroupLimit = childGroupLimit;
	}

	public Integer getChildGroupConcurrent() {
		return ChildGroupConcurrent;
	}

	public void setChildGroupConcurrent(Integer childGroupConcurrent) {
		ChildGroupConcurrent = childGroupConcurrent;
	}

	public Integer getGroupConcurrent() {
		return GroupConcurrent;
	}

	public void setGroupConcurrent(Integer groupConcurrent) {
		GroupConcurrent = groupConcurrent;
	}

	public Integer getChildGroupDownloadLimit() {
		return ChildGroupDownloadLimit;
	}

	public void setChildGroupDownloadLimit(Integer childGroupDownloadLimit) {
		ChildGroupDownloadLimit = childGroupDownloadLimit;
	}

	public Integer getChildGroupPayment() {
		return ChildGroupPayment;
	}

	public void setChildGroupPayment(Integer childGroupPayment) {
		ChildGroupPayment = childGroupPayment;
	}

	public String getAppStartTime() {
		return AppStartTime;
	}

	public void setAppStartTime(String appStartTime) {
		AppStartTime = appStartTime;
	}

	public String getAppEndTime() {
		return AppEndTime;
	}

	public void setAppEndTime(String appEndTime) {
		AppEndTime = appEndTime;
	}

	public String getWeChatStartTime() {
		return WeChatStartTime;
	}

	public void setWeChatStartTime(String weChatStartTime) {
		WeChatStartTime = weChatStartTime;
	}

	public String getWeChatEndTime() {
		return WeChatEndTime;
	}

	public void setWeChatEndTime(String weChatEndTime) {
		WeChatEndTime = weChatEndTime;
	}

	public String getEmail4WeChat() {
		return Email4WeChat;
	}

	public void setEmail4WeChat(String email4WeChat) {
		Email4WeChat = email4WeChat;
	}

}
