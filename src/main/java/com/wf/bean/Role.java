package com.wf.bean;

import net.sf.json.JSONArray;

public class Role {
    private String roleId;

    private String roleName;

    private String description;

    private String purview;
    
    private String deptName;
    
    private JSONArray purviewTree;

    
    public JSONArray getPurviewTree() {
		return purviewTree;
	}

	public void setPurviewTree(JSONArray purviewTree) {
		this.purviewTree = purviewTree;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }


    public String getPurview() {
        return purview;
    }

    public void setPurview(String purview) {
        this.purview = purview == null ? null : purview.trim();
    }

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName
				+ ", description=" + description 
				+ ", purview=" + purview + ", deptName=" + deptName + "]";
	}
    
}