package com.wf.bean;

import java.io.Serializable;
/**
 * 查询对象
 * @author oygy
 *
 */
public class Query implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String userId;// 用户iD
	private String ipSegment;// ip
	private String institution;// 机构名称
	private String openLimit;//权限
	private String Organization;// 机构类型
	private String PostCode;//地区
	private String OrderType;//工单类型
	private String proType;//项目大类
	private String resource;//够买项目
	private String OrderContent;//申请部门
	private String pageNum;// 第几页
	private String pageSize;// 每页条数
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIpSegment() {
		return ipSegment;
	}
	public void setIpSegment(String ipSegment) {
		this.ipSegment = ipSegment;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getOrganization() {
		return Organization;
	}
	public String getOpenLimit() {
		return openLimit;
	}
	public void setOpenLimit(String openLimit) {
		this.openLimit = openLimit;
	}
	public void setOrganization(String organization) {
		Organization = organization;
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
	public String getProType() {
		return proType;
	}
	public void setProType(String proType) {
		this.proType = proType;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getOrderContent() {
		return OrderContent;
	}
	public void setOrderContent(String orderContent) {
		OrderContent = orderContent;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

}
