package com.wf.bean;
/**
 *资源提供商 
 */
public class Provider {
	private Integer id;
	private String nameZh;//提供商名称
	private String providerName;  //提供商code码
	private String providerURL;
	
	public String getProviderURL() {
		return providerURL;
	}
	public void setProviderURL(String providerURL) {
		this.providerURL = providerURL;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getNameZh() {
		return nameZh;
	}
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}
	@Override
	public String toString() {
		return "Provider [id=" + id + ", nameZh=" + nameZh + ", providerName="
				+ providerName + ", providerURL=" + providerURL + "]";
	}
	
}
