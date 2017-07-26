package com.wf.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 用户的基本属性封装类
 * @author Administrator
 *
 */
@XmlRootElement(name = "user")
public class User implements Serializable{
	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String user_realname;
	private String user_nickname;
	private String password;
	private Integer is_male;
	private String mobile_phone;
	private String idcard_number;
	private Integer education_level;
	private String subject;
	private Date date_birth;
	private String email;
	private String topical;
	private String work_unit;
	private String old_work_unit;
	private Integer title;
	private Integer is_freeze;
	private String avatar_url;
	private Integer user_roles;
	private Integer is_phone_verification;
	private Integer is_email_verification;
	private String email_url;
	private Date registration_time;
	private String institution;
	private String pid;
	private Integer login_mode;
	private Integer userType;
	private String extend;
	private String user_usedname;
	private String graduated_school;
	private List<UserIp> userip_list;
	private UserAccountRestriction userAccountRestriction;
	//private List<ThreeLogin> threeLoginList;
	//private UserLimit userlimit;
	@XmlElement
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@XmlElement
	public String getUser_realname() {
		return user_realname;
	}
	public void setUser_realname(String user_realname) {
		this.user_realname = user_realname;
	}
	@XmlElement
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	@XmlElement
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@XmlElement
	public Integer getIs_male() {
		return is_male;
	}
	public void setIs_male(Integer is_male) {
		this.is_male = is_male;
	}
	@XmlElement
	public String getMobile_phone() {
		return mobile_phone;
	}
	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}
	@XmlElement
	public String getIdcard_number() {
		return idcard_number;
	}
	public void setIdcard_number(String idcard_number) {
		this.idcard_number = idcard_number;
	}
	@XmlElement
	public Integer getEducation_level() {
		return education_level;
	}
	public void setEducation_level(Integer education_level) {
		this.education_level = education_level;
	}
	@XmlElement
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@XmlElement
	public Date getDate_birth() {
		return date_birth;
	}
	public void setDate_birth(Date date_birth) {
		this.date_birth = date_birth;
	}
	@XmlElement
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@XmlElement
	public String getTopical() {
		return topical;
	}
	public void setTopical(String topical) {
		this.topical = topical;
	}
	@XmlElement
	public String getWork_unit() {
		return work_unit;
	}
	public void setWork_unit(String work_unit) {
		this.work_unit = work_unit;
	}
	@XmlElement
	public String getOld_work_unit() {
		return old_work_unit;
	}
	public void setOld_work_unit(String old_work_unit) {
		this.old_work_unit = old_work_unit;
	}
	@XmlElement
	public Integer getTitle() {
		return title;
	}
	public void setTitle(Integer title) {
		this.title = title;
	}
	@XmlElement
	public Integer getIs_freeze() {
		return is_freeze;
	}
	public void setIs_freeze(Integer is_freeze) {
		this.is_freeze = is_freeze;
	}
	@XmlElement
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	@XmlElement
	public Integer getUser_roles() {
		return user_roles;
	}
	public void setUser_roles(Integer user_roles) {
		this.user_roles = user_roles;
	}
	
	
	@XmlElement
	public Integer getIs_phone_verification() {
		return is_phone_verification;
	}
	public void setIs_phone_verification(Integer is_phone_verification) {
		this.is_phone_verification = is_phone_verification;
	}
	@XmlElement
	public Integer getIs_email_verification() {
		return is_email_verification;
	}
	public void setIs_email_verification(Integer is_email_verification) {
		this.is_email_verification = is_email_verification;
	}
	

	@XmlElement
	public String getEmail_url() {
		return email_url;
	}
	public void setEmail_url(String email_url) {
		this.email_url = email_url;
	}
	@XmlElement
	public Date getRegistration_time() {
		return registration_time;
	}
	public void setRegistration_time(Date registration_time) {
		this.registration_time = registration_time;
	}
	@XmlElement
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	@XmlElement
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	@XmlElement
	public Integer getLogin_mode() {
		return login_mode;
	}
	public void setLogin_mode(Integer login_mode) {
		this.login_mode = login_mode;
	}
	@XmlElement
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	@XmlElement
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	@XmlElement
	public String getUser_usedname() {
		return user_usedname;
	}
	public void setUser_usedname(String user_usedname) {
		this.user_usedname = user_usedname;
	}
	@XmlElement
	public String getGraduated_school() {
		return graduated_school;
	}
	public void setGraduated_school(String graduated_school) {
		this.graduated_school = graduated_school;
	}
	@XmlElement
	public List<UserIp> getUserip_list() {
		return userip_list;
	}
	public void setUserip_list(List<UserIp> userip_list) {
		this.userip_list = userip_list;
	}
	@XmlElement
	public UserAccountRestriction getUserAccountRestriction() {
		return userAccountRestriction;
	}
	public void setUserAccountRestriction(
			UserAccountRestriction userAccountRestriction) {
		this.userAccountRestriction = userAccountRestriction;
	}
	
/*	@XmlElement
	public List<ThreeLogin> getThreeLoginList() {
		return threeLoginList;
	}
	public void setThreeLoginList(List<ThreeLogin> threeLoginList) {
		this.threeLoginList = threeLoginList;
	}
	@XmlElement
	public UserLimit getUserlimit() {
		return userlimit;
	}
	public void setUserlimit(UserLimit userlimit) {
		this.userlimit = userlimit;
	}*/
}
