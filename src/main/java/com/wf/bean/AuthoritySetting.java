package com.wf.bean;

public class AuthoritySetting {

	private String authorityCode;// 服务权限code
	private String authorityName;// 服务权限名称
	private Integer authoritySort;// 服务权限排序

	public String getAuthorityCode() {
		return authorityCode;
	}

	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}

	public String getAuthorityName() {
		return authorityName;
	}

	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}

	public Integer getAuthoritySort() {
		return authoritySort;
	}

	public void setAuthoritySort(Integer authoritySort) {
		this.authoritySort = authoritySort;
	}

	@Override
	public String toString() {
		return "AuthoritySetting [authorityCode=" + authorityCode
				+ ", authorityName=" + authorityName + ", authoritySort="
				+ authoritySort + "]";
	}

}
