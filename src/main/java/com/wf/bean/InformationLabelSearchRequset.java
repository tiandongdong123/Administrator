package com.wf.bean;

import java.io.Serializable;
import java.util.Date;

public class InformationLabelSearchRequset implements Serializable {
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
    private Date operatingTimeStart;

    /**
     * 操作日期结束
     */
    private Date operatingTimeEnd;

    /**
     * 记录起始下标
     */
    private int startIndex;

    /**
     * 需要返回的记录数
     */
    private int count = 10;

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

    public InformationLabelSearchRequset(String label, String operator, Date operatingTimeStart, Date operatingTimeEnd, int startIndex, int count) {
        this.label = label;
        this.operator = operator;
        this.operatingTimeStart = operatingTimeStart;
        this.operatingTimeEnd = operatingTimeEnd;
        this.startIndex = startIndex;
        this.count = count;
    }

    @Override
    public String toString() {
        return "InformationLabelSearchRequset{" +
                "label='" + label + '\'' +
                ", operator='" + operator + '\'' +
                ", operatingTimeStart=" + operatingTimeStart +
                ", operatingTimeEnd=" + operatingTimeEnd +
                ", startIndex=" + startIndex +
                ", count=" + count +
                '}';
    }

    public InformationLabelSearchRequset() {
    }
}
