package com.wf.bean;

import java.io.Serializable;
import java.util.Date;

public class ResourceStatisticsHour implements Serializable {
    private Integer urlType;

    private String institutionName;

    private String sourceTypeName;

    private String sourceName;

    private Date date;

    private String hour;

    private String numbers;

    private String userId;
    
    private String CHURLTYPE;

    public String getCHURLTYPE() {
		return CHURLTYPE;
	}

	public void setCHURLTYPE(String cHURLTYPE) {
		CHURLTYPE = cHURLTYPE;
	}

	public Integer getUrlType() {
        return urlType;
    }

    public void setUrlType(Integer urlType) {
        this.urlType = urlType;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName == null ? null : institutionName.trim();
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName == null ? null : sourceTypeName.trim();
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour == null ? null : hour.trim();
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers == null ? null : numbers.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
}