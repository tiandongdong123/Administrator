package com.wf.bean;
/**
 *授权表 
 */
public class Authorize {
	
	private Integer id;
	private String institutionId;  //机构ID
	private String username;  //用户名
	private String password;  //密码
	private Integer providerId;   //资源提供商ID
	public Integer getProviderId() {
		return providerId;
	}
	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "Authorize [id=" + id + ", institutionId=" + institutionId
				+ ", username=" + username + ", password=" + password
				+ ", providerId=" + providerId + "]";
	}
	
}
