package com.wf.bean;


/**
 * 资讯管理查询条件
 */
public class MessageSearchRequest {
    /**
     * 栏目
     */
    private String colums;
    /**
     * 发布状态
     */
    private String issueState;
    /**
     * 置顶状态
     */
    private String topState;
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
     * 记录起始下标
     */
    private int startIndex;

    /**
     * 需要返回的记录数
     */
    private int count = 10;

    public String getTopState() {
        return topState;
    }

    public void setTopState(String topState) {
        this.topState = topState;
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

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
