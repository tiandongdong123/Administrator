package com.wf.bean;

import java.util.Date;

public class CommentInfo {
	
    private String id;

    private String perioId;

    private String userId;

    private String perioName;

    private String authorName;

    private String subTime;

    private String auditMoney;

    private String layoutMoney;

    private Date creatDate;

    private String hireCon;

    private String dataState;

    private String complaintStatus;
   
	private int handlingStatus;

    private int isSubmit;

    private String commentContent;

    private String preliminaryOpinions;

    private String appealReason;

    private String finalOpinion;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPerioId() {
        return perioId;
    }

    public void setPerioId(String perioId) {
        this.perioId = perioId == null ? null : perioId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPerioName() {
        return perioName;
    }

    public void setPerioName(String perioName) {
        this.perioName = perioName == null ? null : perioName.trim();
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName == null ? null : authorName.trim();
    }

    public String getSubTime() {
        return subTime;
    }

    public void setSubTime(String subTime) {
        this.subTime = subTime == null ? null : subTime.trim();
    }

    public String getAuditMoney() {
        return auditMoney;
    }

    public void setAuditMoney(String auditMoney) {
        this.auditMoney = auditMoney == null ? null : auditMoney.trim();
    }

    public String getLayoutMoney() {
        return layoutMoney;
    }

    public void setLayoutMoney(String layoutMoney) {
        this.layoutMoney = layoutMoney == null ? null : layoutMoney.trim();
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public String getHireCon() {
        return hireCon;
    }

    public void setHireCon(String hireCon) {
        this.hireCon = hireCon == null ? null : hireCon.trim();
    }

    public String getDataState() {
        return dataState;
    }

    public void setDataState(String dataState) {
        this.dataState = dataState == null ? null : dataState.trim();
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus == null ? null : complaintStatus.trim();
    }

    public int getHandlingStatus() {
		return handlingStatus;
	}

	public void setHandlingStatus(int handlingStatus) {
		this.handlingStatus = handlingStatus;
	}

	public int getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(int isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getPreliminaryOpinions() {
		return preliminaryOpinions;
	}

	public void setPreliminaryOpinions(String preliminaryOpinions) {
		this.preliminaryOpinions = preliminaryOpinions;
	}

	public String getAppealReason() {
		return appealReason;
	}

	public void setAppealReason(String appealReason) {
		this.appealReason = appealReason;
	}

	public String getFinalOpinion() {
		return finalOpinion;
	}

	public void setFinalOpinion(String finalOpinion) {
		this.finalOpinion = finalOpinion;
	}

	@Override
	public String toString() {
		return "CommentInfo [id=" + id + ", perioId=" + perioId + ", userId="
				+ userId + ", perioName=" + perioName + ", authorName="
				+ authorName + ", subTime=" + subTime + ", auditMoney="
				+ auditMoney + ", layoutMoney=" + layoutMoney + ", creatDate="
				+ creatDate + ", hireCon=" + hireCon + ", dataState="
				+ dataState + ", complaintStatus=" + complaintStatus
				+ ", handlingStatus=" + handlingStatus + ", isSubmit="
				+ isSubmit + ", commentContent=" + commentContent
				+ ", preliminaryOpinions=" + preliminaryOpinions
				+ ", appealReason=" + appealReason + ", finalOpinion="
				+ finalOpinion + "]";
	}
	
}