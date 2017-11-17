package com.wf.bean;

public class WfksUserSetting extends WfksUserSettingKey {
	private String propertyValue;
	private String addDateTime;
	private String lastUpdateTime;

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue == null ? null : propertyValue.trim();
	}

	public String getAddDateTime() {
		return addDateTime;
	}

	public void setAddDateTime(String addDateTime) {
		this.addDateTime = addDateTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "WfksUserSetting [propertyValue=" + propertyValue
				+ ", addDateTime=" + addDateTime + ", lastUpdateTime="
				+ lastUpdateTime + "]";
	}

	
}