package com.wf.bean;

import java.io.Serializable;
import java.util.Date;

public class ResourceStatisticsHour implements Serializable {
	private Integer urlType;

	private String institutionName;

	private String sourceTypeName;

	private String sourceName;

	private Date date;

	private String hour;

	private String numbers;

	private String userId;

	private String CHURLTYPE;

	private Integer operate_type;

	private String source_db;

	private String title;

	private String product_source_code;

	private String num1;//浏览数
	private String num2;//下载数
	private String num3;//检索数
	private String num4;//分享数
	private String num5;//收藏数
	private String num6;//导出数
	private String num7;//笔记数
	private String num8;//跳转数
	private String num9;//订阅数



	public String getCHURLTYPE() {
		return CHURLTYPE;
	}

	public void setCHURLTYPE(String cHURLTYPE) {
		CHURLTYPE = cHURLTYPE;
	}

	public Integer getUrlType() {
		return urlType;
	}

	public void setUrlType(Integer urlType) {
		this.urlType = urlType;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName == null ? null : institutionName
				.trim();
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public void setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName == null ? null : sourceTypeName
				.trim();
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName == null ? null : sourceName.trim();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour == null ? null : hour.trim();
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers == null ? null : numbers.trim();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public Integer getOperate_type() {
		return operate_type;
	}

	public void setOperate_type(Integer operate_type) {
		this.operate_type = operate_type;
	}

	public String getSource_db() {
		return source_db;
	}

	public void setSource_db(String source_db) {
		this.source_db = source_db;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProduct_source_code() {
		return product_source_code;
	}

	public void setProduct_source_code(String product_source_code) {
		this.product_source_code = product_source_code;
	}

	public String getNum1() {
		return num1;
	}

	public void setNum1(String num1) {
		this.num1 = num1;
	}

	public String getNum2() {
		return num2;
	}

	public void setNum2(String num2) {
		this.num2 = num2;
	}

	public String getNum3() {
		return num3;
	}

	public void setNum3(String num3) {
		this.num3 = num3;
	}

	public String getNum4() {
		return num4;
	}

	public void setNum4(String num4) {
		this.num4 = num4;
	}

	public String getNum5() {
		return num5;
	}

	public void setNum5(String num5) {
		this.num5 = num5;
	}

	public String getNum6() {
		return num6;
	}

	public void setNum6(String num6) {
		this.num6 = num6;
	}

	public String getNum7() {
		return num7;
	}

	public void setNum7(String num7) {
		this.num7 = num7;
	}

	public String getNum8() {
		return num8;
	}

	public void setNum8(String num8) {
		this.num8 = num8;
	}

	public String getNum9() {
		return num9;
	}

	public void setNum9(String num9) {
		this.num9 = num9;
	}
}