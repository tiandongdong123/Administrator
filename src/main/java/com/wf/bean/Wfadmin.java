package com.wf.bean;

import java.util.Date;

public class Wfadmin {

	private String id;
	private String wangfang_admin_id;
	private String user_realname;
	private String password;
	private Integer is_male;
	private String mobile_phone;
	private String tel_phone;
	private Integer department;
	private String email;
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

	public String getMobile_phone() {
		return mobile_phone;
	}

	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}

	public String getTel_phone() {
		return tel_phone;
	}

	public void setTel_phone(String tel_phone) {
		this.tel_phone = tel_phone;
	}

	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}