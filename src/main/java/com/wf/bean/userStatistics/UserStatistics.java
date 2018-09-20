package com.wf.bean.userStatistics;

import java.io.Serializable;

/**
 * @author 
 */
public class UserStatistics implements Serializable {
    private String date;

    /**
     * 新增个人用户数量
     */
    private Integer personUser;

    /**
     * 新增普通用户数量
     */
    private Integer ordinaryUser;

    /**
     * 新增认证用户数量
     */
    private Integer authenticatedUser;

    /**
     * 新增学者用户数量
     */
    private Integer scholarUser;

    /**
     * 新增个人绑定机构用户数量
     */
    private Integer personBindInstitution;

    /**
     * 新增机构数量
     */
    private Integer institution;

    /**
     * 新增机构账号数量
     */
    private Integer institutionAccount;

    /**
     * 有效机构账号数量
     */
    private Integer validInstitutionAccount;

    /**
     * 新增机构子账号数量
     */
    private Integer institutionSubaccount;

    /**
     * 新增学生账号数量
     */
    private Integer studentaccount;

    /**
     * 新增机构管理员数量
     */
    private Integer institutionAdmin;

    private static final long serialVersionUID = 1L;

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

    public Integer getOrdinaryUser() {
        return ordinaryUser;
    }

    public void setOrdinaryUser(Integer ordinaryUser) {
        this.ordinaryUser = ordinaryUser;
    }

    public Integer getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(Integer authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public Integer getScholarUser() {
        return scholarUser;
    }

    public void setScholarUser(Integer scholarUser) {
        this.scholarUser = scholarUser;
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

    public Integer getValidInstitutionAccount() {
        return validInstitutionAccount;
    }

    public void setValidInstitutionAccount(Integer validInstitutionAccount) {
        this.validInstitutionAccount = validInstitutionAccount;
    }

    public Integer getInstitutionSubaccount() {
        return institutionSubaccount;
    }

    public void setInstitutionSubaccount(Integer institutionSubaccount) {
        this.institutionSubaccount = institutionSubaccount;
    }

    public Integer getStudentaccount() {
        return studentaccount;
    }

    public void setStudentaccount(Integer studentaccount) {
        this.studentaccount = studentaccount;
    }

    public Integer getInstitutionAdmin() {
        return institutionAdmin;
    }

    public void setInstitutionAdmin(Integer institutionAdmin) {
        this.institutionAdmin = institutionAdmin;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserStatistics other = (UserStatistics) that;
        return (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()))
            && (this.getPersonUser() == null ? other.getPersonUser() == null : this.getPersonUser().equals(other.getPersonUser()))
            && (this.getOrdinaryUser() == null ? other.getOrdinaryUser() == null : this.getOrdinaryUser().equals(other.getOrdinaryUser()))
            && (this.getAuthenticatedUser() == null ? other.getAuthenticatedUser() == null : this.getAuthenticatedUser().equals(other.getAuthenticatedUser()))
            && (this.getScholarUser() == null ? other.getScholarUser() == null : this.getScholarUser().equals(other.getScholarUser()))
            && (this.getPersonBindInstitution() == null ? other.getPersonBindInstitution() == null : this.getPersonBindInstitution().equals(other.getPersonBindInstitution()))
            && (this.getInstitution() == null ? other.getInstitution() == null : this.getInstitution().equals(other.getInstitution()))
            && (this.getInstitutionAccount() == null ? other.getInstitutionAccount() == null : this.getInstitutionAccount().equals(other.getInstitutionAccount()))
            && (this.getValidInstitutionAccount() == null ? other.getValidInstitutionAccount() == null : this.getValidInstitutionAccount().equals(other.getValidInstitutionAccount()))
            && (this.getInstitutionSubaccount() == null ? other.getInstitutionSubaccount() == null : this.getInstitutionSubaccount().equals(other.getInstitutionSubaccount()))
            && (this.getStudentaccount() == null ? other.getStudentaccount() == null : this.getStudentaccount().equals(other.getStudentaccount()))
            && (this.getInstitutionAdmin() == null ? other.getInstitutionAdmin() == null : this.getInstitutionAdmin().equals(other.getInstitutionAdmin()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        result = prime * result + ((getPersonUser() == null) ? 0 : getPersonUser().hashCode());
        result = prime * result + ((getOrdinaryUser() == null) ? 0 : getOrdinaryUser().hashCode());
        result = prime * result + ((getAuthenticatedUser() == null) ? 0 : getAuthenticatedUser().hashCode());
        result = prime * result + ((getScholarUser() == null) ? 0 : getScholarUser().hashCode());
        result = prime * result + ((getPersonBindInstitution() == null) ? 0 : getPersonBindInstitution().hashCode());
        result = prime * result + ((getInstitution() == null) ? 0 : getInstitution().hashCode());
        result = prime * result + ((getInstitutionAccount() == null) ? 0 : getInstitutionAccount().hashCode());
        result = prime * result + ((getValidInstitutionAccount() == null) ? 0 : getValidInstitutionAccount().hashCode());
        result = prime * result + ((getInstitutionSubaccount() == null) ? 0 : getInstitutionSubaccount().hashCode());
        result = prime * result + ((getStudentaccount() == null) ? 0 : getStudentaccount().hashCode());
        result = prime * result + ((getInstitutionAdmin() == null) ? 0 : getInstitutionAdmin().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", date=").append(date);
        sb.append(", personUser=").append(personUser);
        sb.append(", ordinaryUser=").append(ordinaryUser);
        sb.append(", authenticatedUser=").append(authenticatedUser);
        sb.append(", scholarUser=").append(scholarUser);
        sb.append(", personBindInstitution=").append(personBindInstitution);
        sb.append(", institution=").append(institution);
        sb.append(", institutionAccount=").append(institutionAccount);
        sb.append(", validInstitutionAccount=").append(validInstitutionAccount);
        sb.append(", institutionSubaccount=").append(institutionSubaccount);
        sb.append(", studentaccount=").append(studentaccount);
        sb.append(", institutionAdmin=").append(institutionAdmin);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}