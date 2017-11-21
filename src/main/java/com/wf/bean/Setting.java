package com.wf.bean;
/**
 *系统配置
 */
public class Setting {
	
	private String id;
	private String settingName;  //(名称) 
	private String settingKey;	//（英文标识） 
	private String settingValue; //（值） 提示：多值请用%分隔
	private String remark; //（备注）
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public String getSettingKey() {
		return settingKey;
	}
	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}
	public String getSettingValue() {
		return settingValue;
	}
	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "Setting [id=" + id + ", settingName=" + settingName
				+ ", settingKey=" + settingKey + ", settingValue="
				+ settingValue + ", remark=" + remark + "]";
	}
	
}
