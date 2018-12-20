package com.wf.bean;

import java.io.Serializable;
import java.util.Date;

public class InformationLabelRequest implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 标签
     */
    private String label;

    /**
     * 总数
     */
    private Integer totalNumber;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作日期
     */
    private Date operatingTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(Date operatingTime) {
        this.operatingTime = operatingTime;
    }

    @Override
    public String toString() {
        return "InformationLabelRequest{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", totalNumber=" + totalNumber +
                ", operator='" + operator + '\'' +
                ", operatingTime=" + operatingTime +
                '}';
    }
}
