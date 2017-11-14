package com.wf.bean;

import java.io.Serializable;
import java.util.Date;

public class ResourceStatisticsHour implements Serializable {
	private Integer urlType;

	private String institutionName;

	private String sourceTypeName;

	private String sourceName;

	private String date;

	private String hour;

	private String numbers;

	private String userId;

	private String CHURLTYPE;

	private Integer operate_type;

	private String source_db;

	private String title;

	private String product_source_code;

	private String resourceTypeCode;

	private String sum1;//浏览数
	private String sum2;//下载数
	private String sum3;//检索数
	private String sum4;//分享数
	private String sum5;//收藏数
	private String sum6;//导出数
	private String sum7;//笔记数
	private String sum8;//跳转数
	private String sum9;//订阅数



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

	public String getResourceTypeCode() {
		return resourceTypeCode;
	}

	public void setResourceTypeCode(String resourceTypeCode) {
		this.resourceTypeCode = resourceTypeCode;
	}
	public String getSum1() {
		return sum1;
	}

	public void setSum1(String sum1) {
		this.sum1 = sum1;
	}

	public String getSum2() {
		return sum2;
	}

	public void setSum2(String sum2) {
		this.sum2 = sum2;
	}

	public String getSum3() {
		return sum3;
	}

	public void setSum3(String sum3) {
		this.sum3 = sum3;
	}

	public String getSum4() {
		return sum4;
	}

	public void setSum4(String sum4) {
		this.sum4 = sum4;
	}

	public String getSum5() {
		return sum5;
	}

	public void setSum5(String sum5) {
		this.sum5 = sum5;
	}

	public String getSum6() {
		return sum6;
	}

	public void setSum6(String sum6) {
		this.sum6 = sum6;
	}

	public String getSum7() {
		return sum7;
	}

	public void setSum7(String sum7) {
		this.sum7 = sum7;
	}

	public String getSum8() {
		return sum8;
	}

	public void setSum8(String sum8) {
		this.sum8 = sum8;
	}

	public String getSum9() {
		return sum9;
	}

	public void setSum9(String sum9) {
		this.sum9 = sum9;
	}

}