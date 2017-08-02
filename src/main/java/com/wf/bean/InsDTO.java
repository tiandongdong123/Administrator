package com.wf.bean;

import java.util.List;

/**
 *	机构信息列表（机构管理员修改DTO） 
 */
public class InsDTO {

	private String ins;
	
	private List<AdminInfo> adlist;

	public String getIns() {
		return ins;
	}

	public void setIns(String ins) {
		this.ins = ins;
	}

	public List<AdminInfo> getList() {
		return adlist;
	}

	public void setList(List<AdminInfo> list) {
		this.adlist = list;
	}
	
	
}
