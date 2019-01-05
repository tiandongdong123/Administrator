package com.wf.bean;

import java.util.List;

public class PageList {
	private int pageNum;//当前页
	
	private int pageSize;//每页显示的数量
	
	private int totalRow;//总条数
	
	private int pageTotal;//总页数
	
	private List<Message> pageRow;//查询结果列表

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public List<Message> getPageRow() {
		return pageRow;
	}

	public void setPageRow(List<Message> pageRow) {
		this.pageRow = pageRow;
	}	
}
