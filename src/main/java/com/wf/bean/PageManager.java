package com.wf.bean;


public class PageManager {
    private String id;//页面ID

    private String pageName;//页面名称

    private String linkAddress;//页面链接

    private String modularId;//所属模块的id
    
    private String addTime; //添加时间

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getModularId() {
		return modularId;
	}

	public void setModularId(String modularId) {
		this.modularId = modularId;
	}

	@Override
	public String toString() {
		return "PageManager [id=" + id + ", pageName=" + pageName + ", linkAddress="
				+ linkAddress + ", modularId=" + modularId + "]";
	}
    
}