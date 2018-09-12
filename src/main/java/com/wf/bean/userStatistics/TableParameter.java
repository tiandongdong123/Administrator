package com.wf.bean.userStatistics;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class TableParameter {
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
     * 每页显示数量
     */
    @NotNull
    private Integer pageSize;
    /**
     * 页数
     */
    @NotNull
    private Integer page;

    /**
     * 排序方式（时间）  1.正序 2.倒序
     * @return
     */
    @NotNull
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

    @Override
    public String toString() {
        return "TableParameter{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", timeUnit=" + timeUnit +
                ", pageSize=" + pageSize +
                ", page=" + page +
                ", sort=" + sort +
                '}';
    }
}
