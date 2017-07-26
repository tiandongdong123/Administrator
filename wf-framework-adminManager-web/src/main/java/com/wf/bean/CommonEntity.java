package com.wf.bean;

import java.io.Serializable;
import java.util.List;

/**
 *	接收页面参数临时实体 
 */
public class CommonEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//机构账号
	private String institution;
	
	private String userId;
	
	private String loginMode;
	
	private String password;
	
	private String resetMoney;
	
	private String resetCount;
	
	//是否开通机构管理员状态
	private String checkuser;
	
	//管理员选择状态
	private String managerType;
	
	//Old机构管理员id
	private String adminOldName;
	
	//New机构管理员信息
	private String adminname;
	
	private String adminpassword;
	
	private String adminIP;
	
	private String adminEmail;
	
	//账号IP
	private String ipSegment;
	
	//账号限制条件
	private String checks;
	
    private Integer upperlimit;

    private Integer pConcurrentnumber;

    private Integer sConcurrentnumber;

    private Integer chargebacks;

    private Integer downloadupperlimit;
	
    private List<ResourceDetailedDTO> rdlist;

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
	
}
