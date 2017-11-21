package com.wf.bean;

/**
 * solr下撤的黑名单
 * 
 * @author ouyang
 * 
 */
public class DeleteArticle {

	private String id;
	private String idType;
	private String model;
	private String userIp;
	private String userId;
	private String createTime;
	//从参数表中获取字段
	private String modelName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	@Override
	public String toString() {
		return "DeleteArticle [id=" + id + ", idType=" + idType + ", model="
				+ model + ", userIp=" + userIp + ", userId=" + userId
				+ ", createTime=" + createTime + ", modelName=" + modelName
				+ "]";
	}
	
}