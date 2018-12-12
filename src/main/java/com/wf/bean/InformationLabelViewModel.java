package com.wf.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InformationLabelViewModel {

    private Integer id;

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
     * 操作时间
     */
    private String operatingTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(String operatingTime) {
        this.operatingTime = operatingTime;
    }

    @Override
    public String toString() {
        return "InformationLabelViewModel{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", totalNumber=" + totalNumber +
                ", operator='" + operator + '\'' +
                ", operatingTime=" + operatingTime +
                '}';
    }

    public InformationLabelViewModel(InformationLabel model) {
        this.id = model.getId();
        this.label = model.getLabel();
        this.totalNumber = model.getTotalNumber();
        this.operator =model.getOperator();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.operatingTime = format.format(model.getOperatingTime());
    }
}
