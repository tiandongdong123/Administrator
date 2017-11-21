package com.wf.bean;

/**
 *	资源购买信息Bean
 */
public class Resource {
	
	private String resourcePurchaseId;

	private String accountId;

    private Integer resourcePurchaseType;

    private String validityStarttime;

    private String validityEndtime;

    private String operationTime;

    private String operator;

    private Double totalMoney;

    private Double balanceMoney;

    private String purchaseRepository;
    
    private Integer purchaseNumber;
    
    
	public Integer getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(Integer purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getResourcePurchaseId() {
        return resourcePurchaseId;
    }

    public void setResourcePurchaseId(String resourcePurchaseId) {
        this.resourcePurchaseId = resourcePurchaseId == null ? null : resourcePurchaseId.trim();
    }

    public Integer getResourcePurchaseType() {
        return resourcePurchaseType;
    }

    public void setResourcePurchaseType(Integer resourcePurchaseType) {
        this.resourcePurchaseType = resourcePurchaseType;
    }

    public String getValidityStarttime() {
        return validityStarttime;
    }

    public void setValidityStarttime(String validityStarttime) {
        this.validityStarttime = validityStarttime;
    }

    public String getValidityEndtime() {
        return validityEndtime;
    }

    public void setValidityEndtime(String validityEndtime) {
        this.validityEndtime = validityEndtime;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Double getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(Double balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public String getPurchaseRepository() {
        return purchaseRepository;
    }

    public void setPurchaseRepository(String purchaseRepository) {
        this.purchaseRepository = purchaseRepository == null ? null : purchaseRepository.trim();
    }

	@Override
	public String toString() {
		return "Resource [resourcePurchaseId=" + resourcePurchaseId
				+ ", accountId=" + accountId + ", resourcePurchaseType="
				+ resourcePurchaseType + ", validityStarttime="
				+ validityStarttime + ", validityEndtime=" + validityEndtime
				+ ", operationTime=" + operationTime + ", operator=" + operator
				+ ", totalMoney=" + totalMoney + ", balanceMoney="
				+ balanceMoney + ", purchaseRepository=" + purchaseRepository
				+ ", purchaseNumber=" + purchaseNumber + "]";
	}
    
}