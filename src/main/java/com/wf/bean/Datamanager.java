package com.wf.bean;

import java.sql.Date;

public class Datamanager {
    private String id;

    private String productSourceCode;
    
    private String tableName;
    
    private String abbreviation;
    
    private String tableDescribe;

    private String resType;

    private String sourceDb;
    
    private String language;

    private String customPolicy;
    
    private String dbtype;
    
    private Integer status;

    private Integer state;
    
    private String sourceUrl;
    
    private String sourceType="zy";
    
    private String addTime;

    private String imgLogoSrc;

    private String link;

    /**
     * 链接地址是否增加id
     */
    private String isIdAdded;

    /**
     * 图片地址是否增加id
     */
    private String isPngIdAdded;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }
    
    public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getTableDescribe() {
        return tableDescribe;
    }

    public void setTableDescribe(String tableDescribe) {
        this.tableDescribe = tableDescribe == null ? null : tableDescribe.trim();
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType == null ? null : resType.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }
   
    public String getProductSourceCode() {
		return productSourceCode;
	}

	public void setProductSourceCode(String productSourceCode) {
		this.productSourceCode = productSourceCode;
	}

	public String getSourceDb() {
		return sourceDb;
	}

	public void setSourceDb(String sourceDb) {
		this.sourceDb = sourceDb;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getCustomPolicy() {
        return customPolicy;
    }

    public void setCustomPolicy(String customPolicy) {
        this.customPolicy = customPolicy == null ? null : customPolicy.trim();
    }
    
	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getImgLogoSrc() {
		return imgLogoSrc;
	}

	public void setImgLogoSrc(String imgLogoSrc) {
		this.imgLogoSrc = imgLogoSrc;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}


    public String getIsIdAdded() {
        return isIdAdded;
    }

    public void setIsIdAdded(String isIdAdded) {
        this.isIdAdded = isIdAdded;
    }

    public String getIsPngIdAdded() {
        return isPngIdAdded;
    }

    public void setIsPngIdAdded(String isPngIdAdded) {
        this.isPngIdAdded = isPngIdAdded;
    }

    @Override
    public String toString() {
        return "Datamanager{" +
                "id='" + id + '\'' +
                ", productSourceCode='" + productSourceCode + '\'' +
                ", tableName='" + tableName + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", tableDescribe='" + tableDescribe + '\'' +
                ", resType='" + resType + '\'' +
                ", sourceDb='" + sourceDb + '\'' +
                ", language='" + language + '\'' +
                ", customPolicy='" + customPolicy + '\'' +
                ", dbtype='" + dbtype + '\'' +
                ", status=" + status +
                ", state=" + state +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", sourceType='" + sourceType + '\'' +
                ", addTime='" + addTime + '\'' +
                ", imgLogoSrc='" + imgLogoSrc + '\'' +
                ", link='" + link + '\'' +
                ", isIdAdded='" + isIdAdded + '\'' +
                ", isPngIdAdded='" + isPngIdAdded + '\'' +
                '}';
    }
}