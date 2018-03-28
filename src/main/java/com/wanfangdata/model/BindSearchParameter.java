package com.wanfangdata.model;

public class BindSearchParameter {
    //查询账号id
    private String userId;
    //机构名称
    private String institutionName;
    //查询开始日期
    private String startDay;
    //查询结束日期
    private String endDay;
    //每页显示数量
    private Integer pageSize;
    //页数
    private Integer page;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
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

    @Override
    public String toString() {
        return "BindSearchParameter{" +
                "userId='" + userId + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", startDay='" + startDay + '\'' +
                ", endDay='" + endDay + '\'' +
                ", pageSize=" + pageSize +
                ", page=" + page +
                '}';
    }
}
