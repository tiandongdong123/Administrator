package com.wf.bean;

/**
 * 资源提供商 -- 学科分类
 */
public class PSubjectCategory {
	private Integer id;
	private Integer providerId; //提供商ID
	private String pCategoryCodes;  //分类号
	private String pCategoryName;   //分类名称
	private Integer parentId; //父类ID
	private Integer proResourceId;  //资源类型
	
	/***/
	private String parentName;//父类名称
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	/***/
	
	public Integer getProResourceId() {
		return proResourceId;
	}
	public void setProResourceId(Integer proResourceId) {
		this.proResourceId = proResourceId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
	public String getpCategoryCodes() {
		return pCategoryCodes;
	}
	public void setpCategoryCodes(String pCategoryCodes) {
		this.pCategoryCodes = pCategoryCodes;
	}
	public String getpCategoryName() {
		return pCategoryName;
	}
	public void setpCategoryName(String pCategoryName) {
		this.pCategoryName = pCategoryName;
	}
	
}
