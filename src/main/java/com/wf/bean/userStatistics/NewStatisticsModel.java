package com.wf.bean.userStatistics;

public class NewStatisticsModel {

    private String date;

    /**
     * 个人用户数量
     */
    private Integer personUser;

    /**
     * 认证用户数量
     */
    private Integer authenticatedUser;

    /**
     * 个人绑定机构用户数量
     */
    private Integer personBindInstitution;

    /**
     * 机构数量
     */
    private Integer institution;

    /**
     * 机构账号数量
     */
    private Integer institutionAccount;

    /**
     * 机构管理员数量
     */
    private Integer institutionAdmin;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPersonUser() {
        return personUser;
    }

    public void setPersonUser(Integer personUser) {
        this.personUser = personUser;
    }

    public Integer getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(Integer authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public Integer getPersonBindInstitution() {
        return personBindInstitution;
    }

    public void setPersonBindInstitution(Integer personBindInstitution) {
        this.personBindInstitution = personBindInstitution;
    }

    public Integer getInstitution() {
        return institution;
    }

    public void setInstitution(Integer institution) {
        this.institution = institution;
    }

    public Integer getInstitutionAccount() {
        return institutionAccount;
    }

    public void setInstitutionAccount(Integer institutionAccount) {
        this.institutionAccount = institutionAccount;
    }

    public Integer getInstitutionAdmin() {
        return institutionAdmin;
    }

    public void setInstitutionAdmin(Integer institutionAdmin) {
        this.institutionAdmin = institutionAdmin;
    }
}
