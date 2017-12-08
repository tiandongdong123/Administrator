package com.wf.bean;

import java.util.Date;
/**
 * 机构用户权限表
 * @author ouyang
 *
 */
public class UserInstitution {
	
	private String userId;
    private String statisticalAnalysis;
    private Date addTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

	public String getStatisticalAnalysis() {
		return statisticalAnalysis;
	}

	public void setStatisticalAnalysis(String statisticalAnalysis) {
		this.statisticalAnalysis = statisticalAnalysis;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Override
	public String toString() {
		return "UserThree [userId=" + userId + ", statisticalAnalysis=" + statisticalAnalysis + ", addTime="
				+ addTime + "]";
	}
    
    
}