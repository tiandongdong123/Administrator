package com.wf.bean;

/**
 *	资源详细信息Bean
 */
public class ResourceDetailed {
	
	private String id;
	
    private String accountId;

    private String resourcePurchaseId;

    private String journalClc;

    private String journalIdno;

    private String journalStarttime;

    private String journalEndtime;

    private String degreeClc;

    private String degreeTypes;

    private String degreeStarttime;

    private String degreeEndtime;

    private String conferenceClc;

    private String conferenceNo;

    private String patentIpc;

    private String booksClc;

    private String booksIdno;

    private String standardTypes;

    private String companyName;

    private String fullIpRange;

    private String limitedParcelStarttime;

    private String limitedParcelEndtime;

    private Integer readingPrint;

    private Integer onlineVisitor;

    private Integer copyNo;

    private Integer totalPrintNo;

    private Integer singlePrintNo;

    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

    public String getJournalClc() {
        return journalClc;
    }

    public void setJournalClc(String journalClc) {
        this.journalClc = journalClc == null ? null : journalClc.trim();
    }

    public String getJournalIdno() {
        return journalIdno;
    }

    public void setJournalIdno(String journalIdno) {
        this.journalIdno = journalIdno == null ? null : journalIdno.trim();
    }

    public String getJournalStarttime() {
        return journalStarttime;
    }

    public void setJournalStarttime(String journalStarttime) {
        this.journalStarttime = journalStarttime;
    }

    public String getJournalEndtime() {
        return journalEndtime;
    }

    public void setJournalEndtime(String journalEndtime) {
        this.journalEndtime = journalEndtime;
    }

    public String getDegreeClc() {
        return degreeClc;
    }

    public void setDegreeClc(String degreeClc) {
        this.degreeClc = degreeClc == null ? null : degreeClc.trim();
    }

    public String getDegreeTypes() {
        return degreeTypes;
    }

    public void setDegreeTypes(String degreeTypes) {
        this.degreeTypes = degreeTypes == null ? null : degreeTypes.trim();
    }

    public String getDegreeStarttime() {
        return degreeStarttime;
    }

    public void setDegreeStarttime(String degreeStarttime) {
        this.degreeStarttime = degreeStarttime;
    }

    public String getDegreeEndtime() {
        return degreeEndtime;
    }

    public void setDegreeEndtime(String degreeEndtime) {
        this.degreeEndtime = degreeEndtime;
    }

    public String getConferenceClc() {
        return conferenceClc;
    }

    public void setConferenceClc(String conferenceClc) {
        this.conferenceClc = conferenceClc == null ? null : conferenceClc.trim();
    }

    public String getConferenceNo() {
        return conferenceNo;
    }

    public void setConferenceNo(String conferenceNo) {
        this.conferenceNo = conferenceNo == null ? null : conferenceNo.trim();
    }

    public String getPatentIpc() {
        return patentIpc;
    }

    public void setPatentIpc(String patentIpc) {
        this.patentIpc = patentIpc == null ? null : patentIpc.trim();
    }

    public String getBooksClc() {
        return booksClc;
    }

    public void setBooksClc(String booksClc) {
        this.booksClc = booksClc == null ? null : booksClc.trim();
    }

    public String getBooksIdno() {
        return booksIdno;
    }

    public void setBooksIdno(String booksIdno) {
        this.booksIdno = booksIdno == null ? null : booksIdno.trim();
    }

    public String getStandardTypes() {
        return standardTypes;
    }

    public void setStandardTypes(String standardTypes) {
        this.standardTypes = standardTypes == null ? null : standardTypes.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getFullIpRange() {
        return fullIpRange;
    }

    public void setFullIpRange(String fullIpRange) {
        this.fullIpRange = fullIpRange == null ? null : fullIpRange.trim();
    }

    public String getLimitedParcelStarttime() {
        return limitedParcelStarttime;
    }

    public void setLimitedParcelStarttime(String limitedParcelStarttime) {
        this.limitedParcelStarttime = limitedParcelStarttime;
    }

    public String getLimitedParcelEndtime() {
        return limitedParcelEndtime;
    }

    public void setLimitedParcelEndtime(String limitedParcelEndtime) {
        this.limitedParcelEndtime = limitedParcelEndtime;
    }

    public Integer getReadingPrint() {
        return readingPrint;
    }

    public void setReadingPrint(Integer readingPrint) {
        this.readingPrint = readingPrint;
    }

    public Integer getOnlineVisitor() {
        return onlineVisitor;
    }

    public void setOnlineVisitor(Integer onlineVisitor) {
        this.onlineVisitor = onlineVisitor;
    }

    public Integer getCopyNo() {
        return copyNo;
    }

    public void setCopyNo(Integer copyNo) {
        this.copyNo = copyNo;
    }

    public Integer getTotalPrintNo() {
        return totalPrintNo;
    }

    public void setTotalPrintNo(Integer totalPrintNo) {
        this.totalPrintNo = totalPrintNo;
    }

    public Integer getSinglePrintNo() {
        return singlePrintNo;
    }

    public void setSinglePrintNo(Integer singlePrintNo) {
        this.singlePrintNo = singlePrintNo;
    }

	@Override
	public String toString() {
		return "ResourceDetailed [id=" + id + ", accountId=" + accountId
				+ ", resourcePurchaseId=" + resourcePurchaseId
				+ ", journalClc=" + journalClc + ", journalIdno=" + journalIdno
				+ ", journalStarttime=" + journalStarttime
				+ ", journalEndtime=" + journalEndtime + ", degreeClc="
				+ degreeClc + ", degreeTypes=" + degreeTypes
				+ ", degreeStarttime=" + degreeStarttime + ", degreeEndtime="
				+ degreeEndtime + ", conferenceClc=" + conferenceClc
				+ ", conferenceNo=" + conferenceNo + ", patentIpc=" + patentIpc
				+ ", booksClc=" + booksClc + ", booksIdno=" + booksIdno
				+ ", standardTypes=" + standardTypes + ", companyName="
				+ companyName + ", fullIpRange=" + fullIpRange
				+ ", limitedParcelStarttime=" + limitedParcelStarttime
				+ ", limitedParcelEndtime=" + limitedParcelEndtime
				+ ", readingPrint=" + readingPrint + ", onlineVisitor="
				+ onlineVisitor + ", copyNo=" + copyNo + ", totalPrintNo="
				+ totalPrintNo + ", singlePrintNo=" + singlePrintNo + "]";
	}
    
}