package com.wf.bean;

import java.util.Date;

public class WfksAccountidMapping {
    private String mappingid;

    private String idAccounttype;

    private String idKey;

    private String relatedidAccounttype;

    private String relatedidKey;
    
    private String isTrial;

    private Date begintime;

    private Date endtime;

    private Date lastUpdatetime;
    
    private String begin;
    private String end;

    public String getIsTrial() {
		return isTrial;
	}

	public void setIsTrial(String isTrial) {
		this.isTrial = isTrial;
	}

	public String getMappingid() {
        return mappingid;
    }

    public void setMappingid(String mappingid) {
        this.mappingid = mappingid == null ? null : mappingid.trim();
    }

    public String getIdAccounttype() {
        return idAccounttype;
    }

    public void setIdAccounttype(String idAccounttype) {
        this.idAccounttype = idAccounttype == null ? null : idAccounttype.trim();
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey == null ? null : idKey.trim();
    }

    public String getRelatedidAccounttype() {
        return relatedidAccounttype;
    }

    public void setRelatedidAccounttype(String relatedidAccounttype) {
        this.relatedidAccounttype = relatedidAccounttype == null ? null : relatedidAccounttype.trim();
    }

    public String getRelatedidKey() {
        return relatedidKey;
    }

    public void setRelatedidKey(String relatedidKey) {
        this.relatedidKey = relatedidKey == null ? null : relatedidKey.trim();
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getLastUpdatetime() {
        return lastUpdatetime;
    }

    public void setLastUpdatetime(Date lastUpdatetime) {
        this.lastUpdatetime = lastUpdatetime;
    }

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "WfksAccountidMapping [mappingid=" + mappingid
				+ ", idAccounttype=" + idAccounttype + ", idKey=" + idKey
				+ ", relatedidAccounttype=" + relatedidAccounttype
				+ ", relatedidKey=" + relatedidKey + ", begintime=" + begintime
				+ ", endtime=" + endtime + ", lastUpdatetime=" + lastUpdatetime
				+ "]";
	}
    
    
}