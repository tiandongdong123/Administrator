package com.wf.bean.importSolr;

public class ImportSolrRequest {
	private AdministratorRequest admin;// 机构管理员
	private PartyAdminRequest party;// 党建管理员
	private UserRequest user;// 机构用户信息
	private UserRoleRequest userRole;// 机构角色

	public AdministratorRequest getAdmin() {
		return admin;
	}

	public void setAdmin(AdministratorRequest admin) {
		this.admin = admin;
	}

	public PartyAdminRequest getParty() {
		return party;
	}

	public void setParty(PartyAdminRequest party) {
		this.party = party;
	}

	public UserRequest getUser() {
		return user;
	}

	public void setUser(UserRequest user) {
		this.user = user;
	}

	public UserRoleRequest getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRoleRequest userRole) {
		this.userRole = userRole;
	}

}
