package com.wf.bean;
public class WarningInfo {
	
	private String id;
	private Integer amountthreshold;
	private Integer datethreshold;
	private Integer remindtime;
	private String remindemail;
	private Integer countthreshold;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getAmountthreshold() {
		return amountthreshold;
	}
	public void setAmountthreshold(Integer amountthreshold) {
		this.amountthreshold = amountthreshold;
	}
	public Integer getDatethreshold() {
		return datethreshold;
	}
	public void setDatethreshold(Integer datethreshold) {
		this.datethreshold = datethreshold;
	}
	public Integer getRemindtime() {
		return remindtime;
	}
	public void setRemindtime(Integer remindtime) {
		this.remindtime = remindtime;
	}
	public String getRemindemail() {
		return remindemail;
	}
	public void setRemindemail(String remindemail) {
		this.remindemail = remindemail;
	}
	public Integer getCountthreshold() {
		return countthreshold;
	}
	public void setCountthreshold(Integer countthreshold) {
		this.countthreshold = countthreshold;
	}
	@Override
	public String toString() {
		return "WarningInfo [id=" + id + ", amountthreshold=" + amountthreshold
				+ ", datethreshold=" + datethreshold + ", remindtime="
				+ remindtime + ", remindemail=" + remindemail
				+ ", countthreshold=" + countthreshold + "]";
	}
	
}
