package com.wf.bean;

public class Subject {
	private String id;
	private String level;
	private String classNum;
	private String className;
	private String pid;
	private String type; //学科分类（1：论文2：期刊3：视频4：专利5：会议6：标准）
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getClassNum() {
		return classNum;
	}
	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Subject [id=" + id + ", level=" + level + ", classNum="
				+ classNum + ", className=" + className + ", pid=" + pid
				+ ", type=" + type + "]";
	}
	
	
	
}
