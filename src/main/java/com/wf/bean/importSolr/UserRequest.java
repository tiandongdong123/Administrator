package com.wf.bean.importSolr;

import java.util.Collection;
import java.util.List;

public class UserRequest {
	
	private String Id;
	private String UserType;
	private String Password;
	private String Institution;
	private String LoginMode;
	private String ParentId;
	private boolean IsFreeze;
	private Collection<String> PayChannelId;
	private String OrderType;
	private String OrderContent;
	private String CountryRegion;
	private String PostCode;
	private String Organization;
	private boolean HasChildGroup;
	private Collection<String> StatisticalAnalysis;
	private String GroupRole;
	private Collection<String> IsTrial;
	private List<String> OpenIP;
	private Long StartIP;
	private Long EndIP;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getUserType() {
		return UserType;
	}
	public void setUserType(String userType) {
		UserType = userType;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getInstitution() {
		return Institution;
	}
	public void setInstitution(String institution) {
		Institution = institution;
	}
	public String getLoginMode() {
		return LoginMode;
	}
	public void setLoginMode(String loginMode) {
		LoginMode = loginMode;
	}
	public String getParentId() {
		return ParentId;
	}
	public void setParentId(String parentId) {
		ParentId = parentId;
	}
	public boolean getIsFreeze() {
		return IsFreeze;
	}
	public void setIsFreeze(boolean isFreeze) {
		IsFreeze = isFreeze;
	}
	public Collection<String> getPayChannelId() {
		return PayChannelId;
	}
	public void setPayChannelId(Collection<String> payChannelId) {
		PayChannelId = payChannelId;
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
	public String getOrganization() {
		return Organization;
	}
	public void setOrganization(String organization) {
		Organization = organization;
	}
	public boolean getHasChildGroup() {
		return HasChildGroup;
	}
	public void setHasChildGroup(boolean hasChildGroup) {
		HasChildGroup = hasChildGroup;
	}
	public Collection<String> getStatisticalAnalysis() {
		return StatisticalAnalysis;
	}
	public void setStatisticalAnalysis(Collection<String> statisticalAnalysis) {
		StatisticalAnalysis = statisticalAnalysis;
	}
	public String getGroupRole() {
		return GroupRole;
	}
	public void setGroupRole(String groupRole) {
		GroupRole = groupRole;
	}
	public Collection<String> getIsTrial() {
		return IsTrial;
	}
	public void setIsTrial(Collection<String> isTrial) {
		IsTrial = isTrial;
	}
	public List<String> getOpenIP() {
		return OpenIP;
	}
	public void setOpenIP(List<String> openIP) {
		OpenIP = openIP;
	}
	public Long getStartIP() {
		return StartIP;
	}
	public void setStartIP(Long startIP) {
		StartIP = startIP;
	}
	public Long getEndIP() {
		return EndIP;
	}
	public void setEndIP(Long endIP) {
		EndIP = endIP;
	}

}
