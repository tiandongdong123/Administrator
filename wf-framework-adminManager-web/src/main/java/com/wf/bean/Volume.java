package com.wf.bean;

import java.io.Serializable;

public class Volume implements Serializable{
    private String id;//文辑id
    
    private String userId;//用户id

    private String volumeNo;//文辑编号

    private String volumeName;//文辑名称

    private String volumeAbstract;//文辑简介

    private String subject;//学科
    
    private String subjectName;//学科名称

    private String coverUrl;//封面

    private String keyword;//关键词

    private Integer volumeState;//文辑状态

    private Integer volumeType;//文辑类型

    private Integer volumeChapter;//章节（有无）

    private String publishPerson;//发布人

    private String publishDate;//发布日期

    private Integer docuNumber;//文辑文献数量
    
    private Integer fulltextReadingNum;//浏览量
    
    private Integer collectionNum;//收藏量

    private Integer downloadNum;//下载量

    private String volumePrice;//文辑价格
    
    private Integer issue;//是否已发布
    
    
    public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getIssue() {
		return issue;
	}

	public void setIssue(Integer issue) {
		this.issue = issue;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getVolumeNo() {
        return volumeNo;
    }

    public void setVolumeNo(String volumeNo) {
        this.volumeNo = volumeNo == null ? null : volumeNo.trim();
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName == null ? null : volumeName.trim();
    }

    public String getVolumeAbstract() {
        return volumeAbstract;
    }

    public void setVolumeAbstract(String volumeAbstract) {
        this.volumeAbstract = volumeAbstract == null ? null : volumeAbstract.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }
    
    
    public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl == null ? null : coverUrl.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public Integer getVolumeState() {
        return volumeState;
    }

    public void setVolumeState(Integer volumeState) {
        this.volumeState = volumeState;
    }

    public Integer getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(Integer volumeType) {
        this.volumeType = volumeType;
    }

    public Integer getVolumeChapter() {
        return volumeChapter;
    }

    public void setVolumeChapter(Integer volumeChapter) {
        this.volumeChapter = volumeChapter;
    }

    public String getPublishPerson() {
        return publishPerson;
    }

    public void setPublishPerson(String publishPerson) {
        this.publishPerson = publishPerson == null ? null : publishPerson.trim();
    }

    public Integer getDocuNumber() {
        return docuNumber;
    }

    public void setDocuNumber(Integer docuNumber) {
        this.docuNumber = docuNumber;
    }

	public String getVolumePrice() {
		return volumePrice;
	}

	public void setVolumePrice(String volumePrice) {
		this.volumePrice = volumePrice;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Integer getFulltextReadingNum() {
		return fulltextReadingNum;
	}

	public void setFulltextReadingNum(Integer fulltextReadingNum) {
		this.fulltextReadingNum = fulltextReadingNum;
	}

	public Integer getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(Integer collectionNum) {
		this.collectionNum = collectionNum;
	}

	public Integer getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(Integer downloadNum) {
		this.downloadNum = downloadNum;
	}

	@Override
	public String toString() {
		return "Volume [id=" + id + ", userId=" + userId + ", volumeNo="
				+ volumeNo + ", volumeName=" + volumeName + ", volumeAbstract="
				+ volumeAbstract + ", subject=" + subject + ", subjectName="
				+ subjectName + ", coverUrl=" + coverUrl + ", keyword="
				+ keyword + ", volumeState=" + volumeState + ", volumeType="
				+ volumeType + ", volumeChapter=" + volumeChapter
				+ ", publishPerson=" + publishPerson + ", publishDate="
				+ publishDate + ", docuNumber=" + docuNumber
				+ ", fulltextReadingNum=" + fulltextReadingNum
				+ ", collectionNum=" + collectionNum + ", downloadNum="
				+ downloadNum + ", volumePrice=" + volumePrice + ", issue="
				+ issue + "]";
	}

}