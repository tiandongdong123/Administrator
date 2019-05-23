package com.wf.bean;

import java.util.Date;

public class Wfadmin {

	private String id;
	private String wangfang_admin_id;
	private String user_realname;
	private String password;
	private Integer is_male;
	private String department;
	private String role_id;
	private Date registration_time;
	private String registrant;
	private Role role;
	private Integer status;
	private Department dept;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getIs_male() {
		return is_male;
	}

	public void setIs_male(Integer is_male) {
		this.is_male = is_male;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWangfang_admin_id() {
		return wangfang_admin_id;
	}

	public void setWangfang_admin_id(String wangfang_admin_id) {
		this.wangfang_admin_id = wangfang_admin_id;
	}

	public String getUser_realname() {
		return user_realname;
	}

	public void setUser_realname(String user_realname) {
		this.user_realname = user_realname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public Date getRegistration_time() {
		return registration_time;
	}

	public void setRegistration_time(Date registration_time) {
		this.registration_time = registration_time;
	}

	public String getRegistrant() {
		return registrant;
	}

	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	@Override
	public String toString() {
		return "Wfadmin [id=" + id + ", wangfang_admin_id=" + wangfang_admin_id
				+ ", user_realname=" + user_realname + ", password=" + password
				+ ", is_male=" + is_male 
				+ ", department=" + department + ", role_id=" + role_id
				+ ", registration_time=" + registration_time + ", registrant="
				+ registrant + ", role=" + role + ", status=" + status
				+ ", dept=" + dept + "]";
	}
	
	

}