package com.wf.bean;

/**
 * 发布、下撤请求
 */
public class UpdateMessageIssueParameter {
    private String id;
    private String colums;
    private String issueState;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColums() {
        return colums;
    }

    public void setColums(String colums) {
        this.colums = colums;
    }

    public String getIssueState() {
        return issueState;
    }

    public void setIssueState(String issueState) {
        this.issueState = issueState;
    }
}
