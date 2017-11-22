package com.wf.bean;

public class ResourcePrice {
	private String rid;
	
	private String sonCode;

	private String sourceCode;
	
    private String resourceTypeCode;

    private String name;

    private String unitCode;

    private Double price;
    
    private Integer applyUserType;

    public Integer getApplyUserType() {
		return applyUserType;
	}

	public void setApplyUserType(Integer applyUserType) {
		this.applyUserType = applyUserType;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSonCode() {
		return sonCode;
	}

	public void setSonCode(String sonCode) {
		this.sonCode = sonCode;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getResourceTypeCode() {
		return resourceTypeCode;
	}

	public void setResourceTypeCode(String resourceTypeCode) {
		this.resourceTypeCode = resourceTypeCode;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

	@Override
	public String toString() {
		return "ResourcePrice [rid=" + rid + ", sonCode=" + sonCode
				+ ", sourceCode=" + sourceCode + ", resourceTypeCode="
				+ resourceTypeCode + ", name=" + name + ", unitCode="
				+ unitCode + ", price=" + price + ", applyUserType="
				+ applyUserType + "]";
	}
    
}