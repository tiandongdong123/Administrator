package com.wf.bean;

/**
 * 查询资讯参数
 */
public class SearchMessageParameter {
    /**
     * 栏目
     */
    private String colums;
    /**
     * 发布状态
     */
    private String issueState;
    /**
     * 标题
     */
    private String title;
    /**
     * 操作开始时间
     */
    private String startTime;
    /**
     * 操作结束时间
     */
    private String endTime;
    /**
     * 操作部门
     */
    private String branch;
    /**
     * 操作人
     */
    private String human;
    /**
     * 当前页数
     */
    private Integer pageNum;
    /**
     * 每页显示最多条目
     */
    private Integer pageSize;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getHuman() {
        return human;
    }

    public void setHuman(String human) {
        this.human = human;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
