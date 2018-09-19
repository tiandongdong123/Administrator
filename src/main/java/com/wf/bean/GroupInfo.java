package com.wf.bean;

/**
 * 机构信息表
 * 
 * @author oygy
 * 
 */
public class GroupInfo {
	private String UserId; // 机构用户ID
	private String Institution; // 机构名
	private String Organization; // 机构类型
	private String Pid; // 机构管理员ID
	private String CountryRegion; // 国家或地区(China:中国 foreign:国外)
	private String PostCode; // 区域编码(省、自治区、直辖市),国内有值，国外的为"无"
	private String OrderType; // 工单类型:(crm:CRM工单，inner:内部工单)
	private String OrderContent; // 工单内容(CRM编号或者部门)

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getInstitution() {
		return Institution;
	}

	public void setInstitution(String institution) {
		Institution = institution;
	}

	public String getOrganization() {
		return Organization;
	}

	public void setOrganization(String organization) {
		Organization = organization;
	}

	public String getPid() {
		return Pid;
	}

	public void setPid(String pid) {
		Pid = pid;
	}

	public String getCountryRegion() {
		return CountryRegion;
	}

	public void setCountryRegion(String countryRegion) {
		CountryRegion = countryRegion;
	}

	public String getPostCode() {
		return PostCode;
	}

	public void setPostCode(String postCode) {
		PostCode = postCode;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	public String getOrderContent() {
		return OrderContent;
	}

	public void setOrderContent(String orderContent) {
		OrderContent = orderContent;
	}

}
