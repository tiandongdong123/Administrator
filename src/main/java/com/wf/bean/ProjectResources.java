package com.wf.bean;

public class ProjectResources {
    private String id;

    private String userId;

    private String projectId;

    private String resourceId;

    private String contract;
    //产品ID
  	private String productid;

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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId == null ? null : projectId.trim();
	}

	public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract == null ? null : contract.trim();
    }
    
	public String getProductid() {
		return productid;
	}
	
	public void setProductid(String productid) {
		this.productid = productid;
	}

	@Override
	public String toString() {
		return "ProjectResources [id=" + id + ", userId=" + userId
				+ ", projectId=" + projectId + ", resourceId=" + resourceId
				+ ", contract=" + contract + ", productid=" + productid + "]";
	}
}