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

	@Override
	public String toString() {
		return "ResourceStatisticsHour [urlType=" + urlType
				+ ", institutionName=" + institutionName + ", sourceTypeName="
				+ sourceTypeName + ", sourceName=" + sourceName + ", date="
				+ date + ", hour=" + hour + ", numbers=" + numbers
				+ ", userId=" + userId + ", CHURLTYPE=" + CHURLTYPE
				+ ", operate_type=" + operate_type + ", source_db=" + source_db
				+ ", title=" + title + ", product_source_code="
				+ product_source_code + "]";
	}
	
}