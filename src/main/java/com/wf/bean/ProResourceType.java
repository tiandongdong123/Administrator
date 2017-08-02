package com.wf.bean;
/**
 * 资源提供商 的资源类型表
 * */
public class ProResourceType {
	private Integer id;
	private Integer providerId;//资源提供商id
	private String resourceName;//资源类型
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProviderId() {
		return providerId;
	}
	public void setProviderId(Integer prroviderId) {
		this.providerId = prroviderId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
}
