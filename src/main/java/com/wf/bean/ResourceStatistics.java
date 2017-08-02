package com.wf.bean;

import java.io.Serializable;
import java.util.Date;

public class ResourceStatistics  implements Serializable {
    private String userId;

    private Integer urlType;

    private String institutionName;

    private String sourceName;

    private Date date;

    private String numbers;

    private String sourceTypeName;
    
    private String CHURLTYPE;

    public String getCHURLTYPE() {
		return CHURLTYPE;
	}

	public void setCHURLTYPE(String cHURLTYPE) {
		CHURLTYPE = cHURLTYPE;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers == null ? null : numbers.trim();
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName == null ? null : sourceTypeName.trim();
    }
}