package com.wf.bean;

public class Department {
    private Integer id;

    private String deptName;

    private String deptDescribe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getDeptDescribe() {
        return deptDescribe;
    }

    public void setDeptDescribe(String deptDescribe) {
        this.deptDescribe = deptDescribe == null ? null : deptDescribe.trim();
    }

	@Override
	public String toString() {
		return "Department [id=" + id + ", deptName=" + deptName
				+ ", deptDescribe=" + deptDescribe + "]";
	}
    
}