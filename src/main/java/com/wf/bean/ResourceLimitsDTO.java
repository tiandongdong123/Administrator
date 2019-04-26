package com.wf.bean;

import java.util.Arrays;

public class ResourceLimitsDTO {

	//资源库ID
	private String resourceid;
	
	//产品ID
	private String[] productid;
	
	//资源购买详细信息
	private String journalClc;
	private String perioInfoClc;
	
	private String journalIdno;
	
	private String journal_startTime;
	
	private String journal_endTime;
	
	private String degreeClc;
	
	private String [] degreeTypes;
	
	private String degreeStarttime;
	
	private String degreeEndtime;
	
	private String conferenceClc;
	
	private String conferenceNo;
	
	private String patentIpc;
	
	private String booksClc;
	
	private String booksIdno;
	//标准
	private String [] standardTypes;
	private String company;
	private String orgName;
	private String companySimp;
	private String [] fullIpRange;
	private String limitedParcelStarttime;
	private String limitedParcelEndtime;
	private Integer readingPrint;
	private Integer onlineVisitor;
	private Integer copyNo;
	private Integer totalPrintNo;
	private Integer singlePrintNo;
	//方志
	private String localType;
	private String gazetteersId;
	private String gazetteersType;
	private String gazetteersLevel;
	private String gazetteersArea;
	private String gazetteersAlbum;
	private String gazetteersStartTime;
	private String gazetteersEndTime;
	private String itemId;
	private String gazetteersOldType;
	private String gazetteersOldArea;
	private String gazetteersOldStartTime;
	private String gazetteersOldEndTime;
	
	
	
	public String getLocalType() {
		return localType;
	}

	public void setLocalType(String localType) {
		this.localType = localType;
	}

	public String getGazetteersOldType() {
		return gazetteersOldType;
	}

	public void setGazetteersOldType(String gazetteersOldType) {
		this.gazetteersOldType = gazetteersOldType;
	}

	public String getGazetteersOldArea() {
		return gazetteersOldArea;
	}

	public void setGazetteersOldArea(String gazetteersOldArea) {
		this.gazetteersOldArea = gazetteersOldArea;
	}

	public String getGazetteersOldStartTime() {
		return gazetteersOldStartTime;
	}

	public void setGazetteersOldStartTime(String gazetteersOldStartTime) {
		this.gazetteersOldStartTime = gazetteersOldStartTime;
	}

	public String getGazetteersOldEndTime() {
		return gazetteersOldEndTime;
	}

	public void setGazetteersOldEndTime(String gazetteersOldEndTime) {
		this.gazetteersOldEndTime = gazetteersOldEndTime;
	}

	public String getGazetteersStartTime() {
		return gazetteersStartTime;
	}

	public void setGazetteersStartTime(String gazetteersStartTime) {
		this.gazetteersStartTime = gazetteersStartTime;
	}

	public String getGazetteersEndTime() {
		return gazetteersEndTime;
	}

	public void setGazetteersEndTime(String gazetteersEndTime) {
		this.gazetteersEndTime = gazetteersEndTime;
	}

	public String getResourceid() {
		return resourceid;
	}

	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}

	public String[] getProductid() {
		return productid;
	}

	public void setProductid(String[] productid) {
		this.productid = productid;
	}

	public String getJournalClc() {
		return journalClc;
	}

	public void setJournalClc(String journalClc) {
		this.journalClc = journalClc;
	}

	public String getPerioInfoClc() {
		return perioInfoClc;
	}

	public void setPerioInfoClc(String perioInfoClc) {
		this.perioInfoClc = perioInfoClc;
	}

	public String getJournalIdno() {
		return journalIdno;
	}

	public void setJournalIdno(String journalIdno) {
		this.journalIdno = journalIdno;
	}

	public String getJournal_startTime() {
		return journal_startTime;
	}

	public void setJournal_startTime(String journal_startTime) {
		this.journal_startTime = journal_startTime;
	}

	public String getJournal_endTime() {
		return journal_endTime;
	}

	public void setJournal_endTime(String journal_endTime) {
		this.journal_endTime = journal_endTime;
	}

	public String getDegreeClc() {
		return degreeClc;
	}

	public void setDegreeClc(String degreeClc) {
		this.degreeClc = degreeClc;
	}

	public String[] getDegreeTypes() {
		return degreeTypes;
	}

	public void setDegreeTypes(String[] degreeTypes) {
		this.degreeTypes = degreeTypes;
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
		this.conferenceClc = conferenceClc;
	}

	public String getConferenceNo() {
		return conferenceNo;
	}

	public void setConferenceNo(String conferenceNo) {
		this.conferenceNo = conferenceNo;
	}

	public String getPatentIpc() {
		return patentIpc;
	}

	public void setPatentIpc(String patentIpc) {
		this.patentIpc = patentIpc;
	}

	public String getBooksClc() {
		return booksClc;
	}

	public void setBooksClc(String booksClc) {
		this.booksClc = booksClc;
	}

	public String getBooksIdno() {
		return booksIdno;
	}

	public void setBooksIdno(String booksIdno) {
		this.booksIdno = booksIdno;
	}

	public String[] getStandardTypes() {
		return standardTypes;
	}

	public void setStandardTypes(String[] standardTypes) {
		this.standardTypes = standardTypes;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanySimp() {
		return companySimp;
	}

	public void setCompanySimp(String companySimp) {
		this.companySimp = companySimp;
	}

	public String[] getFullIpRange() {
		return fullIpRange;
	}

	public void setFullIpRange(String[] fullIpRange) {
		this.fullIpRange = fullIpRange;
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

	public String getGazetteersId() {
		return gazetteersId;
	}

	public void setGazetteersId(String gazetteersId) {
		this.gazetteersId = gazetteersId;
	}

	public String getGazetteersType() {
		return gazetteersType;
	}

	public void setGazetteersType(String gazetteersType) {
		this.gazetteersType = gazetteersType;
	}

	public String getGazetteersLevel() {
		return gazetteersLevel;
	}

	public void setGazetteersLevel(String gazetteersLevel) {
		this.gazetteersLevel = gazetteersLevel;
	}

	public String getGazetteersArea() {
		return gazetteersArea;
	}

	public void setGazetteersArea(String gazetteersArea) {
		this.gazetteersArea = gazetteersArea;
	}

	public String getGazetteersAlbum() {
		return gazetteersAlbum;
	}

	public void setGazetteersAlbum(String gazetteersAlbum) {
		this.gazetteersAlbum = gazetteersAlbum;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "ResourceLimitsDTO [resourceid=" + resourceid + ", productid=" + Arrays.toString(productid)
				+ ", journalClc=" + journalClc + ", perioInfoClc=" + perioInfoClc + ", journalIdno=" + journalIdno
				+ ", journal_startTime=" + journal_startTime + ", journal_endTime=" + journal_endTime + ", degreeClc="
				+ degreeClc + ", degreeTypes=" + Arrays.toString(degreeTypes) + ", degreeStarttime=" + degreeStarttime
				+ ", degreeEndtime=" + degreeEndtime + ", conferenceClc=" + conferenceClc + ", conferenceNo="
				+ conferenceNo + ", patentIpc=" + patentIpc + ", booksClc=" + booksClc + ", booksIdno=" + booksIdno
				+ ", standardTypes=" + Arrays.toString(standardTypes) + ", company=" + company + ", orgName=" + orgName
				+ ", companySimp=" + companySimp + ", fullIpRange=" + Arrays.toString(fullIpRange)
				+ ", limitedParcelStarttime=" + limitedParcelStarttime + ", limitedParcelEndtime="
				+ limitedParcelEndtime + ", readingPrint=" + readingPrint + ", onlineVisitor=" + onlineVisitor
				+ ", copyNo=" + copyNo + ", totalPrintNo=" + totalPrintNo + ", singlePrintNo=" + singlePrintNo
				+ ", gazetteersId=" + gazetteersId + ", gazetteersType=" + gazetteersType + ", gazetteersLevel="
				+ gazetteersLevel + ", gazetteersArea=" + gazetteersArea + ", gazetteersAlbum=" + gazetteersAlbum
				+ ", gazetteersStartTime=" + gazetteersStartTime + ", gazetteersEndTime=" + gazetteersEndTime
				+ ", itemId=" + itemId + ", gazetteersOldType=" + gazetteersOldType + ", gazetteersOldArea="
				+ gazetteersOldArea + ", gazetteersOldStartTime=" + gazetteersOldStartTime + ", gazetteersOldEndTime="
				+ gazetteersOldEndTime + "]";
	}

	
	
}
