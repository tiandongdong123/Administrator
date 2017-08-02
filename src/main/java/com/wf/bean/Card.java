package com.wf.bean;

public class Card {
    private String id;//充值卡ID

    private String batchId;//批次ID

    private String cardNum;//充值卡号

    private String password;//充值卡密码

    private Integer value;//面值

    private Integer invokeState;//激活状态

    private String invokeDate;//激活日期

    private String invokeUser;//激活用户

    private String invokeIp;//激活IP

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getInvokeState() {
		return invokeState;
	}

	public void setInvokeState(Integer invokeState) {
		this.invokeState = invokeState;
	}


	public String getInvokeDate() {
		return invokeDate;
	}

	public void setInvokeDate(String invokeDate) {
		this.invokeDate = invokeDate;
	}

	public String getInvokeUser() {
		return invokeUser;
	}

	public void setInvokeUser(String invokeUser) {
		this.invokeUser = invokeUser;
	}

	public String getInvokeIp() {
		return invokeIp;
	}

	public void setInvokeIp(String invokeIp) {
		this.invokeIp = invokeIp;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", batchId=" + batchId + ", cardNum="
				+ cardNum + ", password=" + password + ", value=" + value
				+ ", invokeState=" + invokeState + ", invokeDate=" + invokeDate
				+ ", invokeUser=" + invokeUser + ", invokeIp=" + invokeIp + "]";
	}
    
}