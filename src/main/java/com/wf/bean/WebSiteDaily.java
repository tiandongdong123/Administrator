package com.wf.bean;

import java.io.Serializable;

public class WebSiteDaily implements Serializable{
	
	private Integer type;
	private String date;
	private String numbers;
	private long num;
	
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
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "WebSiteDaily [type=" + type + ", date=" + date + ", numbers="
				+ numbers + ", num=" + num + "]";
	}
	
	
	
	
}
