package com.wf.bean.userStatistics;

public class TableResponse {


    private String date;

    /**
     * 个人用户数量
     */
    private String personUser;

    /**
     * 认证用户数量
     */
    private String authenticatedUser;

    /**
     * 个人绑定机构用户数量
     */
    private String personBindInstitution;

    /**
     * 机构数量
     */
    private String institution;

    /**
     * 机构账号数量
     */
    private String institutionAccount;

    /**
     * 有效机构账号数量
     */
    private String validInstitutionAccount;

    /**
     * 机构管理员数量
     */
    private String institutionAdmin;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPersonUser() {
        return personUser;
    }

    public void setPersonUser(String personUser) {
        this.personUser = personUser;
    }

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(String authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public String getPersonBindInstitution() {
        return personBindInstitution;
    }

    public void setPersonBindInstitution(String personBindInstitution) {
        this.personBindInstitution = personBindInstitution;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getInstitutionAccount() {
        return institutionAccount;
    }

    public void setInstitutionAccount(String institutionAccount) {
        this.institutionAccount = institutionAccount;
    }

    public String getValidInstitutionAccount() {
        return validInstitutionAccount;
    }

    public void setValidInstitutionAccount(String validInstitutionAccount) {
        this.validInstitutionAccount = validInstitutionAccount;
    }

    public String getInstitutionAdmin() {
        return institutionAdmin;
    }

    public void setInstitutionAdmin(String institutionAdmin) {
        this.institutionAdmin = institutionAdmin;
    }

    @Override
    public String toString() {
        return "TableResponse{" +
                "date='" + date + '\'' +
                ", personUser='" + personUser + '\'' +
                ", authenticatedUser='" + authenticatedUser + '\'' +
                ", personBindInstitution='" + personBindInstitution + '\'' +
                ", institution='" + institution + '\'' +
                ", institutionAccount='" + institutionAccount + '\'' +
                ", validInstitutionAccount=" + validInstitutionAccount +
                ", institutionAdmin=" + institutionAdmin +
                '}';
    }
}
