package com.wf.bean;


public class Modular {
    private String id;//模块ID

    private String modularName;//模块名称

    private String linkAddress;//链接地址


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModularName() {
		return modularName;
	}

	public void setModularName(String modularName) {
		this.modularName = modularName;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", modularName=" + modularName + ", linkAddress="
				+ linkAddress + "]";
	}
    
}