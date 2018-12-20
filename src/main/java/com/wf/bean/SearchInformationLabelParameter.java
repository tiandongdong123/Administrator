package com.wf.bean;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class SearchInformationLabelParameter implements Serializable {
    /**
     * 标签
     */
    private String label;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作日期开始
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operatingTimeStart;

    /**
     * 操作日期结束
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operatingTimeEnd;

    /**
     * 当前页数
     */
    private Integer page = 1;

    /**
     * 每页显示条数
     */
    private int pageSize;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    public Date getOperatingTimeStart() {
        return operatingTimeStart;
    }

    public void setOperatingTimeStart(Date operatingTimeStart) {
        this.operatingTimeStart = operatingTimeStart;
    }

    public Date getOperatingTimeEnd() {
        return operatingTimeEnd;
    }

    public void setOperatingTimeEnd(Date operatingTimeEnd) {
        this.operatingTimeEnd = operatingTimeEnd;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "SearchInformationLabelParameter{" +
                "label='" + label + '\'' +
                ", operator='" + operator + '\'' +
                ", operatingTimeStart=" + operatingTimeStart +
                ", operatingTimeEnd=" + operatingTimeEnd +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }

    public SearchInformationLabelParameter(String label, String operator, Date operatingTimeStart, Date operatingTimeEnd, Integer page, int pageSize) {
        this.label = label;
        this.operator = operator;
        this.operatingTimeStart = operatingTimeStart;
        this.operatingTimeEnd = operatingTimeEnd;
        this.page = page;
        this.pageSize = pageSize;
    }

    public SearchInformationLabelParameter() {
    }
}
