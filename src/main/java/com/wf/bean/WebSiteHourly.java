package com.wf.bean;

import java.io.Serializable;

public class WebSiteHourly implements Serializable{
	
	private Integer type;
	private String date;
	private String hour;
	private String numbers;
	private long num;
	
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
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
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	@Override
	public String toString() {
		return "WebSiteHourly [type=" + type + ", date=" + date + ", hour="
				+ hour + ", numbers=" + numbers + ", num=" + num + "]";
	}
	
	
	
}
