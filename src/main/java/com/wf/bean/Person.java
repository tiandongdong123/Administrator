package com.wf.bean;

import java.util.Date;

/**
 *	用户Bean
 */
public class Person {
	
    private String userId;

    private String userRealname;

    private String userNickname;

    private String password;

    private Integer isMale;

    private String mobilePhone;

    private String idcardNumber;

    private Integer educationLevel;

    private String subject;

    private String dateBirth;

    private String email;

    private String topical;

    private String workUnit;

    private String old_work_unit;

    private Integer title;

    private Integer isFreeze;

    private String avatarUrl;

    private Integer userRoles;

    private Integer is_phone_verification;

    private Integer is_email_verification;

    private String registrationTime;

    private String institution;

    private String pid;

    private Integer loginMode;

    private Integer usertype;
    
	private String extend;
    
    private String usedName;
    
    private String award;
    
	private String thirdNum;//第三方绑定账号
    
    private String status;//账号类型
    
    private ResourceDetailed resourceDetailed;
    
	private String adminEmail;    //机构管理员email
	
	private String adminIsTrial;  //机构管理员是否试用
	
	private Date adminBegintime;  //机构管理员开始时间
	
	private Date adminEndtime;  //机构管理员结束时间
	

	
	public String getAdminIsTrial() {
		return adminIsTrial;
	}

	public void setAdminIsTrial(String adminIsTrial) {
		this.adminIsTrial = adminIsTrial;
	}

	public Date getAdminBegintime() {
		return adminBegintime;
	}

	public void setAdminBegintime(Date adminBegintime) {
		this.adminBegintime = adminBegintime;
	}

	public Date getAdminEndtime() {
		return adminEndtime;
	}

	public void setAdminEndtime(Date adminEndtime) {
		this.adminEndtime = adminEndtime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserRealname() {
		return userRealname;
	}

	public void setUserRealname(String userRealname) {
		this.userRealname = userRealname;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIsMale() {
		return isMale;
	}

	public void setIsMale(Integer isMale) {
		this.isMale = isMale;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getIdcardNumber() {
		return idcardNumber;
	}

	public void setIdcardNumber(String idcardNumber) {
		this.idcardNumber = idcardNumber;
	}

	public Integer getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(Integer educationLevel) {
		this.educationLevel = educationLevel;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(String dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTopical() {
		return topical;
	}

	public void setTopical(String topical) {
		this.topical = topical;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getOld_work_unit() {
		return old_work_unit;
	}

	public void setOld_work_unit(String old_work_unit) {
		this.old_work_unit = old_work_unit;
	}

	public Integer getTitle() {
		return title;
	}

	public void setTitle(Integer title) {
		this.title = title;
	}

	public Integer getIsFreeze() {
		return isFreeze;
	}

	public void setIsFreeze(Integer isFreeze) {
		this.isFreeze = isFreeze;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Integer userRoles) {
		this.userRoles = userRoles;
	}

	public Integer getIs_phone_verification() {
		return is_phone_verification;
	}

	public void setIs_phone_verification(Integer is_phone_verification) {
		this.is_phone_verification = is_phone_verification;
	}

	public Integer getIs_email_verification() {
		return is_email_verification;
	}

	public void setIs_email_verification(Integer is_email_verification) {
		this.is_email_verification = is_email_verification;
	}

	public String getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(String registrationTime) {
		this.registrationTime = registrationTime;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(Integer loginMode) {
		this.loginMode = loginMode;
	}

	public Integer getUsertype() {
		return usertype;
	}

	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public ResourceDetailed getResourceDetailed() {
		return resourceDetailed;
	}

	public void setResourceDetailed(ResourceDetailed resourceDetailed) {
		this.resourceDetailed = resourceDetailed;
	}
	
	 public String getUsedName() {
		return usedName;
	}

	public void setUsedName(String usedName) {
		this.usedName = usedName;
	}
	
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}
	
    public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public String getThirdNum() {
		return thirdNum;
	}

	public void setThirdNum(String thirdNum) {
		this.thirdNum = thirdNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Person [userId=" + userId + ", userRealname=" + userRealname
				+ ", userNickname=" + userNickname + ", password=" + password
				+ ", isMale=" + isMale + ", mobilePhone=" + mobilePhone
				+ ", idcardNumber=" + idcardNumber + ", educationLevel="
				+ educationLevel + ", subject=" + subject + ", dateBirth="
				+ dateBirth + ", email=" + email + ", topical=" + topical
				+ ", workUnit=" + workUnit + ", old_work_unit=" + old_work_unit
				+ ", title=" + title + ", isFreeze=" + isFreeze
				+ ", avatarUrl=" + avatarUrl + ", userRoles=" + userRoles
				+ ", is_phone_verification=" + is_phone_verification
				+ ", is_email_verification=" + is_email_verification
				+ ", registrationTime=" + registrationTime + ", institution="
				+ institution + ", pid=" + pid + ", loginMode=" + loginMode
				+ ", usertype=" + usertype + ", extend=" + extend
				+ ", usedName=" + usedName + ", award=" + award + ", thirdNum="
				+ thirdNum + ", status=" + status + ", resourceDetailed="
				+ resourceDetailed + ", adminEmail=" + adminEmail + "]";
	}
}