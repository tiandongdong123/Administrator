package com.wf.bean;

/**
 * 授权-资源类型-学科-母体 （关联表）
 */
public class AuthorizeRelation {
	
	private Integer id;
	private Integer authorizeId; //授权ID
	private String institutionId; //机构ID
	private Integer providerId;   //资源提供商ID
	private Integer subjectId;   //学科ID
	private String periodicalId; //母体ID
	private String detailsURL;   //详情页地址
	private String downloadURL;  //原文下载地址
	private Integer proResourceId; //资源类型  
	
	public Integer getProResourceId() {
		return proResourceId;
	}
	public void setProResourceId(Integer proResourceId) {
		this.proResourceId = proResourceId;
	}
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
	public Integer getAuthorizeId() {
		return authorizeId;
	}
	public void setAuthorizeId(Integer authorizeId) {
		this.authorizeId = authorizeId;
	}
	public String getInstitutionId() {
		return institutionId;
	}
	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public String getPeriodicalId() {
		return periodicalId;
	}
	public void setPeriodicalId(String periodicalId) {
		this.periodicalId = periodicalId;
	}
	public String getDetailsURL() {
		return detailsURL;
	}
	public void setDetailsURL(String detailsURL) {
		this.detailsURL = detailsURL;
	}
	public String getDownloadURL() {
		return downloadURL;
	}
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

}
