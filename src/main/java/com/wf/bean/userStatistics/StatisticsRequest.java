package com.wf.bean.userStatistics;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class StatisticsRequest {

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
     * 对比开始时间
     */
    private String compareStartTime;
    /**
     * 对比结束时间
     */
    private String compareEndTime;
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
    /**
     * 每页显示数量
     */
    private Integer pageSize;
    /**
     * 页数
     */
    private Integer page;

    /**
     * 排序方式（时间）  1.正序 2.倒序
     * @return
     */
    private Integer sort;


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

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public String getCompareStartTime() {
        return compareStartTime;
    }

    public void setCompareStartTime(String compareStartTime) {
        this.compareStartTime = compareStartTime;
    }

    public String getCompareEndTime() {
        return compareEndTime;
    }

    public void setCompareEndTime(String compareEndTime) {
        this.compareEndTime = compareEndTime;
    }

    @Override
    public String toString() {
        return "StatisticsRequest{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", compareStartTime='" + compareStartTime + '\'' +
                ", compareEndTime='" + compareEndTime + '\'' +
                ", timeUnit=" + timeUnit +
                ", type='" + type + '\'' +
                ", pageSize=" + pageSize +
                ", page=" + page +
                ", sort=" + sort +
                '}';
    }
}
