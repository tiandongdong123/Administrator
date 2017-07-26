package com.wf.bean;

public class SonSystem {

	private Integer id;
	private String sonName;
	private String sonCode;
	private String productResourceCode;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProductResourceCode() {
		return productResourceCode;
	}
	public void setProductResourceCode(String productResourceCode) {
		this.productResourceCode = productResourceCode;
	}
	public String getSonName() {
		return sonName;
	}
	public void setSonName(String sonName) {
		this.sonName = sonName == null ? null : sonName.trim();
	}
	public String getSonCode() {
		return sonCode;
	}
	public void setSonCode(String sonCode) {
		this.sonCode = sonCode == null ? null : sonCode.trim();
	}
	
	
	
}
