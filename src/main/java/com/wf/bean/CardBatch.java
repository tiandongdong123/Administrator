package com.wf.bean;

public class CardBatch {

	private String batchId;//批次id

    private String batchName;//批次号

    private String  type;//万方卡类型

    private String valueNumber;//面值/数量

    private String validStart;//有效期开始

    private String validEnd;//有效期结束

    private String createDate;//生成日期

    private Integer amount;//总金额

    private String applyDepartment;//申请部门

    private String applyPerson;//申请人

    private String applyDate;//申请日期

    private String adjunct;//附件

    private Integer checkState;//审核状态

    private String checkDepartment;//审核部门

    private String checkPerson;//审核人

    private String checkDate;//审核日期

    private String pullDepartment;//领取部门

    private String pullPerson;//领取人

    private String pullDate;//领取日期

    private Integer batchState;//批次状态

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValueNumber() {
		return valueNumber;
	}

	public void setValueNumber(String valueNumber) {
		this.valueNumber = valueNumber;
	}

	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getApplyDepartment() {
		return applyDepartment;
	}

	public void setApplyDepartment(String applyDepartment) {
		this.applyDepartment = applyDepartment;
	}

	public String getApplyPerson() {
		return applyPerson;
	}

	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}

	

	public String getAdjunct() {
		return adjunct;
	}

	public void setAdjunct(String adjunct) {
		this.adjunct = adjunct;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public String getCheckDepartment() {
		return checkDepartment;
	}

	public void setCheckDepartment(String checkDepartment) {
		this.checkDepartment = checkDepartment;
	}

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	

	public String getValidStart() {
		return validStart;
	}

	public void setValidStart(String validStart) {
		this.validStart = validStart;
	}

	public String getValidEnd() {
		return validEnd;
	}

	public void setValidEnd(String validEnd) {
		this.validEnd = validEnd;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getPullDate() {
		return pullDate;
	}

	public void setPullDate(String pullDate) {
		this.pullDate = pullDate;
	}

	public String getPullDepartment() {
		return pullDepartment;
	}

	public void setPullDepartment(String pullDepartment) {
		this.pullDepartment = pullDepartment;
	}

	public String getPullPerson() {
		return pullPerson;
	}

	public void setPullPerson(String pullPerson) {
		this.pullPerson = pullPerson;
	}


	public Integer getBatchState() {
		return batchState;
	}

	public void setBatchState(Integer batchState) {
		this.batchState = batchState;
	}

	@Override
	public String toString() {
		return "CardBatch [batchId=" + batchId + ", batchName=" + batchName
				+ ", type=" + type 
				+ ", valueNumber=" + valueNumber + ", validStart=" + validStart
				+ ", validEnd=" + validEnd + ", createDate=" + createDate
				+ ", amount=" + amount + ", applyDepartment=" + applyDepartment
				+ ", applyPerson=" + applyPerson + ", applyDate=" + applyDate
				+ ", adjunct=" + adjunct + ", checkState=" + checkState
				+ ", checkDepartment=" + checkDepartment + ", checkPerson="
				+ checkPerson + ", checkDate=" + checkDate
				+ ", pullDepartment=" + pullDepartment + ", pullPerson="
				+ pullPerson + ", pullDate=" + pullDate + ", batchState="
				+ batchState + "]";
	}
	

}