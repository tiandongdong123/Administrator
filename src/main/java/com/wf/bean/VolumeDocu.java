package com.wf.bean;

import java.io.Serializable;

public class VolumeDocu implements Serializable{
	private String id;
    private String docuTitle;//文献标题

    private String docuAuthor;//文献作者

    private String docuId;//文献id
    
    private String randomId;
    
    private String docuType;//文献类型
    
    private String classType;//来源数据库
    
    private String chapterId;//章id

    private String volumeId;//文辑id
    
    private Integer number;//文献号
    
    private String addTime;//添加时间
    
    public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getDocuId() {
        return docuId;
    }
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDocuId(String docuId) {
        this.docuId = docuId == null ? null : docuId.trim();
    }

    public String getDocuTitle() {
        return docuTitle;
    }

    public void setDocuTitle(String docuTitle) {
        this.docuTitle = docuTitle == null ? null : docuTitle.trim();
    }

    public String getDocuAuthor() {
        return docuAuthor;
    }

    public void setDocuAuthor(String docuAuthor) {
        this.docuAuthor = docuAuthor == null ? null : docuAuthor.trim();
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId == null ? null : chapterId.trim();
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId == null ? null : volumeId.trim();
    }
    
    public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

	public String getDocuType() {
		return docuType;
	}

	public void setDocuType(String docuType) {
		this.docuType = docuType;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	@Override
	public String toString() {
		return "VolumeDocu [id=" + id + ", docuTitle=" + docuTitle
				+ ", docuAuthor=" + docuAuthor + ", docuId=" + docuId
				+ ", chapterId=" + chapterId + ", volumeId=" + volumeId + "]";
	}

	
    
}