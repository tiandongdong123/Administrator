package com.wf.bean;

import java.io.Serializable;

public class VolumeChapter implements Serializable{
    private String chapterId;//章id

    private String chapterTitle;//章标题

    private String volumeId;//文辑id
    
    private Integer number;//章序号
    
    private String addTime;//添加时间

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId == null ? null : chapterId.trim();
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle == null ? null : chapterTitle.trim();
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId == null ? null : volumeId.trim();
    }
    
	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "VolumeChapter [chapterId=" + chapterId + ", chapterTitle="
				+ chapterTitle + ", volumeId=" + volumeId + "]";
	}
    
}