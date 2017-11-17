package com.wf.bean;

import java.io.Serializable;

public class WebSiteAttribute implements Serializable{
	
	private Integer type;
	private String date;
	private String groupName;
	private String numbers;
	private String num;
	private String percent;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	
	@Override
	public String toString() {
		return "WebSiteAttribute [type=" + type + ", date=" + date
				+ ", groupName=" + groupName + ", numbers=" + numbers
				+ ", num=" + num + ", percent=" + percent + "]";
	}
	
}
