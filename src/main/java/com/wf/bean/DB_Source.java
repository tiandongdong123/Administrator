package com.wf.bean;

public class DB_Source {
    private String dbSourceId;

    private String dbSourceName;
    
    private String dbSourceCode;
    
    
    public String getDbSourceCode() {
		return dbSourceCode;
	}

	public void setDbSourceCode(String dbSourceCode) {
		this.dbSourceCode = dbSourceCode;
	}

	public String getDbSourceId() {
        return dbSourceId;
    }

    public void setDbSourceId(String dbSourceId) {
        this.dbSourceId = dbSourceId == null ? null : dbSourceId.trim();
    }

    public String getDbSourceName() {
        return dbSourceName;
    }

    public void setDbSourceName(String dbSourceName) {
        this.dbSourceName = dbSourceName == null ? null : dbSourceName.trim();
    }
}