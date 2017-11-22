package com.wf.bean;

public class WfksPayChannelResources {
    private String id;

    private String userId;

    private String payChannelid;

    private String resourceId;

    private String productId;

    private String contract;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPayChannelid() {
        return payChannelid;
    }

    public void setPayChannelid(String payChannelid) {
        this.payChannelid = payChannelid == null ? null : payChannelid.trim();
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract == null ? null : contract.trim();
    }

	@Override
	public String toString() {
		return "WfksPayChannelResources [id=" + id + ", userId=" + userId
				+ ", payChannelid=" + payChannelid + ", resourceId="
				+ resourceId + ", productId=" + productId + ", contract="
				+ contract + "]";
	}
    
    
}