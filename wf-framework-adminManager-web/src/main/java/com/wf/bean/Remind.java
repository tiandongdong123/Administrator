package com.wf.bean;

public class Remind {
    private String id;

    private String batchName;//批次

    private String type;//充值卡类型

    private String applyDepartment;//申请部门

    private String applyPerson;//申请人

    private String applyDate;//申请日期

    private Integer isSeen;//是否查看

    private String remindTime;//提醒时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName == null ? null : batchName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getApplyDepartment() {
        return applyDepartment;
    }

    public void setApplyDepartment(String applyDepartment) {
        this.applyDepartment = applyDepartment == null ? null : applyDepartment.trim();
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson == null ? null : applyPerson.trim();
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate == null ? null : applyDate.trim();
    }

    public Integer getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Integer isSeen) {
        this.isSeen = isSeen;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime == null ? null : remindTime.trim();
    }

	@Override
	public String toString() {
		return "Remind [id=" + id + ", batchName=" + batchName + ", type="
				+ type + ", applyDepartment=" + applyDepartment
				+ ", applyPerson=" + applyPerson + ", applyDate=" + applyDate
				+ ", isSeen=" + isSeen + ", remindTime=" + remindTime + "]";
	}
    
}