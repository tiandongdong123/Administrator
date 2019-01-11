package com.wf.bean;

import java.io.Serializable;

public class Message implements Serializable{
	private String id;
	private String channel;
	private String colums;
	private String title;
	private String abstracts;
	private String content;
	private String linkAddress;
	private String createTime;
	private String author;
	private String organName;
	private String imageUrl;
	private String branch;
	private String human;
	private String stick;
	private Integer issueState;
	private String isTop;
	private String label;
	public String getStick() {
		return stick;
	}
	public void setStick(String stick) {
		this.stick = stick;
	}

	@Override
	public String toString() {
		return "Message{" +
				"id='" + id + '\'' +
				", channel='" + channel + '\'' +
				", colums='" + colums + '\'' +
				", title='" + title + '\'' +
				", abstracts='" + abstracts + '\'' +
				", content='" + content + '\'' +
				", linkAddress='" + linkAddress + '\'' +
				", createTime='" + createTime + '\'' +
				", author='" + author + '\'' +
				", organName='" + organName + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", branch='" + branch + '\'' +
				", human='" + human + '\'' +
				", stick='" + stick + '\'' +
				", issueState=" + issueState +
				", isTop='" + isTop + '\'' +
				", label='" + label + '\'' +
				'}';
	}

	public Integer getIssueState() {
		return issueState;
	}
	public void setIssueState(Integer issueState) {
		this.issueState = issueState;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getColums() {
		return colums;
	}
	public void setColums(String colums) {
		this.colums = colums;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLinkAddress() {
		return linkAddress;
	}
	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getHuman() {
		return human;
	}
	public void setHuman(String human) {
		this.human = human;
	}
	public String getIsTop() {
		return isTop;
	}
	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
