package com.wf.bean;

import java.util.List;
public class Menu {

	private String id;
	private String name;
	private boolean haschild;
	private List<Menu> children;
	private String url;
	public Menu() {
		super();
	}
	public Menu(String id, String name, boolean haschild, List<Menu> children, String url) {
		super();
		this.id = id;
		this.name = name;
		this.haschild = haschild;
		this.children = children;
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getHaschild() {
		return haschild;
	}
	public void setHaschild(boolean haschild) {
		this.haschild = haschild;
	}
	public List<Menu> getChildren() {
		return children;
	}
	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", haschild=" + haschild + ", children=" + children + ", url="
				+ url + "]";
	}
	
	
}
