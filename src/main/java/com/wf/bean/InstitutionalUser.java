package com.wf.bean;

import java.io.Serializable;
import java.util.List;

/**
 *	接收页面参数临时实体 
 */
public class InstitutionalUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	// 机构账号
	private String institution;
	private String oldInstitution;
	private String userId;
	private String loginMode;
	private String password;
	private String resetMoney;
	private String resetCount;
	private String ipSegment;// 账号IP

	// 是否开通机构管理员状态
	private String checkuser;
	private String adminIsTrial;
	private String adminBegintime;//有效期-开始
	private String adminEndtime;//有效期-结束
	private String managerType;// 管理员选择状态
	private String adminOldName;// Old机构管理员id
	private String adminname;// New机构管理员信息
	private String adminpassword;
	private String adminIP;
	private String adminEmail;

	// 机构子账号
	private String checks;
	private Integer upperlimit;
	private Integer pConcurrentnumber;
	private Integer sConcurrentnumber;
	private Integer chargebacks;
	private Integer downloadupperlimit;
	private String sIsTrial;
	private String sBegintime;//有效期-开始
	private String sEndtime;//有效期-结束

	// 统计分析
	private String tongji;
	// 开通APP嵌入服务
	private String openApp;
	private String appIsTrial;
	private String appBegintime;
	private String appEndtime;
	// 开通微信公众号嵌入服务
	private String openWeChat;
	private String weChatIsTrial;
	private String weChatBegintime;
	private String weChatEndtime;
	// 开通微信公众号嵌入服务是否立即发送
	private String sendMail;
	// 开通微信公众号嵌入服务的email
	private String weChatEamil;

	// 党建管理员
	private String partyLimit;//是否有权限
	private String isTrial;//是否使用
	private String partyAdmin;//党建管理员ID
	private String partyPassword;//党建管理员密码
	private String partyBegintime;//有效期-开始
	private String partyEndtime;//有效期-结束
	
	private String CountryRegion;//国家
	private String PostCode;//区域
	private String Organization;//机构类型
	private String OrderType;//工单类型
	private String OrderContent;//工单内容
	
	private String changeFront;//购买项目转换前
    
    private List<ResourceDetailedDTO> rdlist;

    
    
	public String getWeChatIsTrial() {
		return weChatIsTrial;
	}

	public void setWeChatIsTrial(String weChatIsTrial) {
		this.weChatIsTrial = weChatIsTrial;
	}

	public String getAppIsTrial() {
		return appIsTrial;
	}

	public void setAppIsTrial(String appIsTrial) {
		this.appIsTrial = appIsTrial;
	}

	public String getsIsTrial() {
		return sIsTrial;
	}

	public void setsIsTrial(String sIsTrial) {
		this.sIsTrial = sIsTrial;
	}

	public String getsBegintime() {
		return sBegintime;
	}

	public void setsBegintime(String sBegintime) {
		this.sBegintime = sBegintime;
	}

	public String getsEndtime() {
		return sEndtime;
	}

	public void setsEndtime(String sEndtime) {
		this.sEndtime = sEndtime;
	}

	public String getAdminIsTrial() {
		return adminIsTrial;
	}

	public void setAdminIsTrial(String adminIsTrial) {
		this.adminIsTrial = adminIsTrial;
	}

	public String getAdminBegintime() {
		return adminBegintime;
	}

	public void setAdminBegintime(String adminBegintime) {
		this.adminBegintime = adminBegintime;
	}

	public String getAdminEndtime() {
		return adminEndtime;
	}

	public void setAdminEndtime(String adminEndtime) {
		this.adminEndtime = adminEndtime;
	}

	public List<ResourceDetailedDTO> getRdlist() {
		return rdlist;
	}

	public void setRdlist(List<ResourceDetailedDTO> rdlist) {
		this.rdlist = rdlist;
	}
	
    public String getResetMoney() {
		return resetMoney;
	}

	public void setResetMoney(String resetMoney) {
		this.resetMoney = resetMoney;
	}
	
	public String getResetCount() {
		return resetCount;
	}

	public void setResetCount(String resetCount) {
		this.resetCount = resetCount;
	}

	public String getChecks() {
		return checks;
	}

	public void setChecks(String checks) {
		this.checks = checks;
	}

	public Integer getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(Integer upperlimit) {
        this.upperlimit = upperlimit;
    }

    public Integer getpConcurrentnumber() {
        return pConcurrentnumber;
    }

    public void setpConcurrentnumber(Integer pConcurrentnumber) {
        this.pConcurrentnumber = pConcurrentnumber;
    }

    public Integer getsConcurrentnumber() {
        return sConcurrentnumber;
    }

    public void setsConcurrentnumber(Integer sConcurrentnumber) {
        this.sConcurrentnumber = sConcurrentnumber;
    }

    public Integer getChargebacks() {
        return chargebacks;
    }

    public void setChargebacks(Integer chargebacks) {
        this.chargebacks = chargebacks;
    }

    public Integer getDownloadupperlimit() {
        return downloadupperlimit;
    }

    public void setDownloadupperlimit(Integer downloadupperlimit) {
        this.downloadupperlimit = downloadupperlimit;
    }

	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getOldInstitution() {
		return oldInstitution;
	}
	public void setOldInstitution(String oldInstitution) {
		this.oldInstitution = oldInstitution;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginMode() {
		return loginMode;
	}
	public void setLoginMode(String loginMode) {
		this.loginMode = loginMode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIpSegment() {
		return ipSegment;
	}
	public void setIpSegment(String ipSegment) {
		this.ipSegment = ipSegment;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getAdminpassword() {
		return adminpassword;
	}
	public void setAdminpassword(String adminpassword) {
		this.adminpassword = adminpassword;
	}
	public String getAdminIP() {
		return adminIP;
	}
	public void setAdminIP(String adminIP) {
		this.adminIP = adminIP;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getManagerType() {
		return managerType;
	}

	public void setManagerType(String managerType) {
		this.managerType = managerType;
	}

	public String getAdminOldName() {
		return adminOldName;
	}

	public void setAdminOldName(String adminOldName) {
		this.adminOldName = adminOldName;
	}

	public String getCheckuser() {
		return checkuser;
	}

	public void setCheckuser(String checkuser) {
		this.checkuser = checkuser;
	}

	public String getTongji() {
		return tongji;
	}

	public void setTongji(String tongji) {
		this.tongji = tongji;
	}

	public String getOpenApp() {
		return openApp;
	}

	public void setOpenApp(String openApp) {
		this.openApp = openApp;
	}

	public String getOpenWeChat() {
		return openWeChat;
	}

	public void setOpenWeChat(String openWeChat) {
		this.openWeChat = openWeChat;
	}

	public String getSendMail() {
		return sendMail;
	}

	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	public String getWeChatEamil() {
		return weChatEamil;
	}

	public void setWeChatEamil(String weChatEamil) {
		this.weChatEamil = weChatEamil;
	}

	public String getPartyLimit() {
		return partyLimit;
	}

	public void setPartyLimit(String partyLimit) {
		this.partyLimit = partyLimit;
	}

	public String getIsTrial() {
		return isTrial;
	}

	public void setIsTrial(String isTrial) {
		this.isTrial = isTrial;
	}

	public String getPartyAdmin() {
		return partyAdmin;
	}

	public void setPartyAdmin(String partyAdmin) {
		this.partyAdmin = partyAdmin;
	}

	public String getPartyPassword() {
		return partyPassword;
	}

	public void setPartyPassword(String partyPassword) {
		this.partyPassword = partyPassword;
	}

	public String getPartyBegintime() {
		return partyBegintime;
	}

	public void setPartyBegintime(String partyBegintime) {
		this.partyBegintime = partyBegintime;
	}

	public String getPartyEndtime() {
		return partyEndtime;
	}

	public void setPartyEndtime(String partyEndtime) {
		this.partyEndtime = partyEndtime;
	}

	public String getCountryRegion() {
		return CountryRegion;
	}

	public void setCountryRegion(String countryRegion) {
		CountryRegion = countryRegion;
	}

	public String getPostCode() {
		return PostCode;
	}

	public void setPostCode(String postCode) {
		PostCode = postCode;
	}

	public String getOrganization() {
		return Organization;
	}

	public void setOrganization(String organization) {
		Organization = organization;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	public String getOrderContent() {
		return OrderContent;
	}

	public void setOrderContent(String orderContent) {
		OrderContent = orderContent;
	}

	public String getAppBegintime() {
		return appBegintime;
	}

	public void setAppBegintime(String appBegintime) {
		this.appBegintime = appBegintime;
	}

	public String getAppEndtime() {
		return appEndtime;
	}

	public void setAppEndtime(String appEndtime) {
		this.appEndtime = appEndtime;
	}

	public String getWeChatBegintime() {
		return weChatBegintime;
	}

	public void setWeChatBegintime(String weChatBegintime) {
		this.weChatBegintime = weChatBegintime;
	}

	public String getWeChatEndtime() {
		return weChatEndtime;
	}

	public void setWeChatEndtime(String weChatEndtime) {
		this.weChatEndtime = weChatEndtime;
	}

	public String getChangeFront() {
		return changeFront;
	}

	public void setChangeFront(String changeFront) {
		this.changeFront = changeFront;
	}

	@Override
	public String toString() {
		return "InstitutionalUser [institution=" + institution + ", oldInstitution=" + oldInstitution + ", userId="
				+ userId + ", loginMode=" + loginMode + ", password=" + password + ", resetMoney=" + resetMoney
				+ ", resetCount=" + resetCount + ", ipSegment=" + ipSegment + ", checkuser=" + checkuser
				+ ", adminIsTrial=" + adminIsTrial + ", adminBegintime=" + adminBegintime + ", adminEndtime="
				+ adminEndtime + ", managerType=" + managerType + ", adminOldName=" + adminOldName + ", adminname="
				+ adminname + ", adminpassword=" + adminpassword + ", adminIP=" + adminIP + ", adminEmail=" + adminEmail
				+ ", checks=" + checks + ", upperlimit=" + upperlimit + ", pConcurrentnumber=" + pConcurrentnumber
				+ ", sConcurrentnumber=" + sConcurrentnumber + ", chargebacks=" + chargebacks + ", downloadupperlimit="
				+ downloadupperlimit + ", sIsTrial=" + sIsTrial + ", sBegintime=" + sBegintime + ", sEndtime="
				+ sEndtime + ", tongji=" + tongji + ", openApp=" + openApp + ", appIsTrial=" + appIsTrial
				+ ", appBegintime=" + appBegintime + ", appEndtime=" + appEndtime + ", openWeChat=" + openWeChat
				+ ", weChatIsTrial=" + weChatIsTrial + ", weChatBegintime=" + weChatBegintime + ", weChatEndtime="
				+ weChatEndtime + ", sendMail=" + sendMail + ", weChatEamil=" + weChatEamil + ", partyLimit="
				+ partyLimit + ", isTrial=" + isTrial + ", partyAdmin=" + partyAdmin + ", partyPassword="
				+ partyPassword + ", partyBegintime=" + partyBegintime + ", partyEndtime=" + partyEndtime
				+ ", CountryRegion=" + CountryRegion + ", PostCode=" + PostCode + ", Organization=" + Organization
				+ ", OrderType=" + OrderType + ", OrderContent=" + OrderContent + ", changeFront=" + changeFront
				+ ", rdlist=" + rdlist + "]";
	}
	



	


	
}
