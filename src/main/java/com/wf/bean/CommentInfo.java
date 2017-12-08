package com.wf.bean;

import java.util.Date;

public class CommentInfo {
	private String  rand_id;
	private String	id;
	private String	perio_id;
	private String	perio_name;
	private String	user_id;
	private String	author_name;
	private String	submit_period;
	private String	audit_money;
	private String	layout_money;
	private String	creat_date;
	private String	hire_con;
	private String	comment_content;
	private String	data_state;
	private String	preliminary_opinions;
	private String	final_opinion;
	private String	publishing_discipline;
	private String	update_date;
	private String	executive_operation;
	private String	auditor;
	private String	audit_time;
	private String  isget;
	private String  goods;
	
	
	public String getRand_id() {
		return rand_id;
	}
	public void setRand_id(String rand_id) {
		this.rand_id = rand_id;
	}
	public String getIsget() {
		return isget;
	}
	public void setIsget(String isget) {
		this.isget = isget;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPerio_id() {
		return perio_id;
	}
	public void setPerio_id(String perio_id) {
		this.perio_id = perio_id;
	}
	public String getPerio_name() {
		return perio_name;
	}
	public void setPerio_name(String perio_name) {
		this.perio_name = perio_name;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	public String getSubmit_period() {
		return submit_period;
	}
	public void setSubmit_period(String submit_period) {
		this.submit_period = submit_period;
	}
	public String getAudit_money() {
		return audit_money;
	}
	public void setAudit_money(String audit_money) {
		this.audit_money = audit_money;
	}
	public String getLayout_money() {
		return layout_money;
	}
	public void setLayout_money(String layout_money) {
		this.layout_money = layout_money;
	}
	public String getCreat_date() {
		return creat_date;
	}
	public void setCreat_date(String creat_date) {
		this.creat_date = creat_date;
	}
	public String getHire_con() {
		return hire_con;
	}
	public void setHire_con(String hire_con) {
		this.hire_con = hire_con;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getData_state() {
		return data_state;
	}
	public void setData_state(String data_state) {
		this.data_state = data_state;
	}
	public String getPreliminary_opinions() {
		return preliminary_opinions;
	}
	public void setPreliminary_opinions(String preliminary_opinions) {
		this.preliminary_opinions = preliminary_opinions;
	}
	public String getFinal_opinion() {
		return final_opinion;
	}
	public void setFinal_opinion(String final_opinion) {
		this.final_opinion = final_opinion;
	}
	public String getPublishing_discipline() {
		return publishing_discipline;
	}
	public void setPublishing_discipline(String publishing_discipline) {
		this.publishing_discipline = publishing_discipline;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getExecutive_operation() {
		return executive_operation;
	}
	public void setExecutive_operation(String executive_operation) {
		this.executive_operation = executive_operation;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}
	
	@Override
	public String toString() {
		return "CommentInfo [id=" + id + ", perio_id=" + perio_id
				+ ", perio_name=" + perio_name + ", user_id=" + user_id
				+ ", author_name=" + author_name + ", submit_period="
				+ submit_period + ", audit_money=" + audit_money
				+ ", layout_money=" + layout_money + ", creat_date="
				+ creat_date + ", hire_con=" + hire_con + ", comment_content="
				+ comment_content + ", data_state=" + data_state
				+ ", preliminary_opinions=" + preliminary_opinions
				+ ", final_opinion=" + final_opinion
				+ ", publishing_discipline=" + publishing_discipline
				+ ", update_date=" + update_date + ", executive_operation="
				+ executive_operation + ", auditor=" + auditor
				+ ", audit_time=" + audit_time + "]";
	}
	
}