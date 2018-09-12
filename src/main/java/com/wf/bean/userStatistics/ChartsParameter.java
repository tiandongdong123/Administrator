package com.wf.bean.userStatistics;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ChartsParameter implements Serializable{
    /**
     * 开始时间
     */
    @NotBlank
    private String startTime;
    /**
     * 结束时间
     */
    @NotBlank
    private String endTime;
    /**
     * 统计单位
     */
    @NotNull
    private Integer timeUnit;
    /**
     * 统计指标
     */
    @NotBlank
    private String type;


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

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChartsParameter{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", timeUnit=" + timeUnit +
                ", type='" + type + '\'' +
                '}';
    }
}
