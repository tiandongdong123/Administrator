package com.wf.bean.userStatistics;

import java.util.ArrayList;
import java.util.List;

public class UserStatisticsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public UserStatisticsExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andDateIsNull() {
            addCriterion("date is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("date is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(String value) {
            addCriterion("date =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(String value) {
            addCriterion("date <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(String value) {
            addCriterion("date >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(String value) {
            addCriterion("date >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(String value) {
            addCriterion("date <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(String value) {
            addCriterion("date <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLike(String value) {
            addCriterion("date like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotLike(String value) {
            addCriterion("date not like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<String> values) {
            addCriterion("date in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<String> values) {
            addCriterion("date not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(String value1, String value2) {
            addCriterion("date between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(String value1, String value2) {
            addCriterion("date not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andPersonUserIsNull() {
            addCriterion("person_user is null");
            return (Criteria) this;
        }

        public Criteria andPersonUserIsNotNull() {
            addCriterion("person_user is not null");
            return (Criteria) this;
        }

        public Criteria andPersonUserEqualTo(Integer value) {
            addCriterion("person_user =", value, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserNotEqualTo(Integer value) {
            addCriterion("person_user <>", value, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserGreaterThan(Integer value) {
            addCriterion("person_user >", value, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("person_user >=", value, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserLessThan(Integer value) {
            addCriterion("person_user <", value, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserLessThanOrEqualTo(Integer value) {
            addCriterion("person_user <=", value, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserIn(List<Integer> values) {
            addCriterion("person_user in", values, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserNotIn(List<Integer> values) {
            addCriterion("person_user not in", values, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserBetween(Integer value1, Integer value2) {
            addCriterion("person_user between", value1, value2, "personUser");
            return (Criteria) this;
        }

        public Criteria andPersonUserNotBetween(Integer value1, Integer value2) {
            addCriterion("person_user not between", value1, value2, "personUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserIsNull() {
            addCriterion("ordinary_user is null");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserIsNotNull() {
            addCriterion("ordinary_user is not null");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserEqualTo(Integer value) {
            addCriterion("ordinary_user =", value, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserNotEqualTo(Integer value) {
            addCriterion("ordinary_user <>", value, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserGreaterThan(Integer value) {
            addCriterion("ordinary_user >", value, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("ordinary_user >=", value, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserLessThan(Integer value) {
            addCriterion("ordinary_user <", value, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserLessThanOrEqualTo(Integer value) {
            addCriterion("ordinary_user <=", value, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserIn(List<Integer> values) {
            addCriterion("ordinary_user in", values, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserNotIn(List<Integer> values) {
            addCriterion("ordinary_user not in", values, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserBetween(Integer value1, Integer value2) {
            addCriterion("ordinary_user between", value1, value2, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andOrdinaryUserNotBetween(Integer value1, Integer value2) {
            addCriterion("ordinary_user not between", value1, value2, "ordinaryUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserIsNull() {
            addCriterion("authenticated_user is null");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserIsNotNull() {
            addCriterion("authenticated_user is not null");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserEqualTo(Integer value) {
            addCriterion("authenticated_user =", value, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserNotEqualTo(Integer value) {
            addCriterion("authenticated_user <>", value, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserGreaterThan(Integer value) {
            addCriterion("authenticated_user >", value, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("authenticated_user >=", value, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserLessThan(Integer value) {
            addCriterion("authenticated_user <", value, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserLessThanOrEqualTo(Integer value) {
            addCriterion("authenticated_user <=", value, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserIn(List<Integer> values) {
            addCriterion("authenticated_user in", values, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserNotIn(List<Integer> values) {
            addCriterion("authenticated_user not in", values, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserBetween(Integer value1, Integer value2) {
            addCriterion("authenticated_user between", value1, value2, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andAuthenticatedUserNotBetween(Integer value1, Integer value2) {
            addCriterion("authenticated_user not between", value1, value2, "authenticatedUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserIsNull() {
            addCriterion("scholar_user is null");
            return (Criteria) this;
        }

        public Criteria andScholarUserIsNotNull() {
            addCriterion("scholar_user is not null");
            return (Criteria) this;
        }

        public Criteria andScholarUserEqualTo(Integer value) {
            addCriterion("scholar_user =", value, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserNotEqualTo(Integer value) {
            addCriterion("scholar_user <>", value, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserGreaterThan(Integer value) {
            addCriterion("scholar_user >", value, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserGreaterThanOrEqualTo(Integer value) {
            addCriterion("scholar_user >=", value, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserLessThan(Integer value) {
            addCriterion("scholar_user <", value, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserLessThanOrEqualTo(Integer value) {
            addCriterion("scholar_user <=", value, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserIn(List<Integer> values) {
            addCriterion("scholar_user in", values, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserNotIn(List<Integer> values) {
            addCriterion("scholar_user not in", values, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserBetween(Integer value1, Integer value2) {
            addCriterion("scholar_user between", value1, value2, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andScholarUserNotBetween(Integer value1, Integer value2) {
            addCriterion("scholar_user not between", value1, value2, "scholarUser");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionIsNull() {
            addCriterion("person_bind_institution is null");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionIsNotNull() {
            addCriterion("person_bind_institution is not null");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionEqualTo(Integer value) {
            addCriterion("person_bind_institution =", value, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionNotEqualTo(Integer value) {
            addCriterion("person_bind_institution <>", value, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionGreaterThan(Integer value) {
            addCriterion("person_bind_institution >", value, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionGreaterThanOrEqualTo(Integer value) {
            addCriterion("person_bind_institution >=", value, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionLessThan(Integer value) {
            addCriterion("person_bind_institution <", value, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionLessThanOrEqualTo(Integer value) {
            addCriterion("person_bind_institution <=", value, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionIn(List<Integer> values) {
            addCriterion("person_bind_institution in", values, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionNotIn(List<Integer> values) {
            addCriterion("person_bind_institution not in", values, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionBetween(Integer value1, Integer value2) {
            addCriterion("person_bind_institution between", value1, value2, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andPersonBindInstitutionNotBetween(Integer value1, Integer value2) {
            addCriterion("person_bind_institution not between", value1, value2, "personBindInstitution");
            return (Criteria) this;
        }

        public Criteria andInstitutionIsNull() {
            addCriterion("institution is null");
            return (Criteria) this;
        }

        public Criteria andInstitutionIsNotNull() {
            addCriterion("institution is not null");
            return (Criteria) this;
        }

        public Criteria andInstitutionEqualTo(Integer value) {
            addCriterion("institution =", value, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionNotEqualTo(Integer value) {
            addCriterion("institution <>", value, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionGreaterThan(Integer value) {
            addCriterion("institution >", value, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionGreaterThanOrEqualTo(Integer value) {
            addCriterion("institution >=", value, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionLessThan(Integer value) {
            addCriterion("institution <", value, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionLessThanOrEqualTo(Integer value) {
            addCriterion("institution <=", value, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionIn(List<Integer> values) {
            addCriterion("institution in", values, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionNotIn(List<Integer> values) {
            addCriterion("institution not in", values, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionBetween(Integer value1, Integer value2) {
            addCriterion("institution between", value1, value2, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionNotBetween(Integer value1, Integer value2) {
            addCriterion("institution not between", value1, value2, "institution");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountIsNull() {
            addCriterion("institution_account is null");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountIsNotNull() {
            addCriterion("institution_account is not null");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountEqualTo(Integer value) {
            addCriterion("institution_account =", value, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountNotEqualTo(Integer value) {
            addCriterion("institution_account <>", value, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountGreaterThan(Integer value) {
            addCriterion("institution_account >", value, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountGreaterThanOrEqualTo(Integer value) {
            addCriterion("institution_account >=", value, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountLessThan(Integer value) {
            addCriterion("institution_account <", value, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountLessThanOrEqualTo(Integer value) {
            addCriterion("institution_account <=", value, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountIn(List<Integer> values) {
            addCriterion("institution_account in", values, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountNotIn(List<Integer> values) {
            addCriterion("institution_account not in", values, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountBetween(Integer value1, Integer value2) {
            addCriterion("institution_account between", value1, value2, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAccountNotBetween(Integer value1, Integer value2) {
            addCriterion("institution_account not between", value1, value2, "institutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountIsNull() {
            addCriterion("valid_institution_account is null");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountIsNotNull() {
            addCriterion("valid_institution_account is not null");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountEqualTo(Integer value) {
            addCriterion("valid_institution_account =", value, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountNotEqualTo(Integer value) {
            addCriterion("valid_institution_account <>", value, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountGreaterThan(Integer value) {
            addCriterion("valid_institution_account >", value, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountGreaterThanOrEqualTo(Integer value) {
            addCriterion("valid_institution_account >=", value, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountLessThan(Integer value) {
            addCriterion("valid_institution_account <", value, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountLessThanOrEqualTo(Integer value) {
            addCriterion("valid_institution_account <=", value, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountIn(List<Integer> values) {
            addCriterion("valid_institution_account in", values, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountNotIn(List<Integer> values) {
            addCriterion("valid_institution_account not in", values, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountBetween(Integer value1, Integer value2) {
            addCriterion("valid_institution_account between", value1, value2, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andValidInstitutionAccountNotBetween(Integer value1, Integer value2) {
            addCriterion("valid_institution_account not between", value1, value2, "validInstitutionAccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountIsNull() {
            addCriterion("institution_subaccount is null");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountIsNotNull() {
            addCriterion("institution_subaccount is not null");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountEqualTo(Integer value) {
            addCriterion("institution_subaccount =", value, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountNotEqualTo(Integer value) {
            addCriterion("institution_subaccount <>", value, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountGreaterThan(Integer value) {
            addCriterion("institution_subaccount >", value, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountGreaterThanOrEqualTo(Integer value) {
            addCriterion("institution_subaccount >=", value, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountLessThan(Integer value) {
            addCriterion("institution_subaccount <", value, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountLessThanOrEqualTo(Integer value) {
            addCriterion("institution_subaccount <=", value, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountIn(List<Integer> values) {
            addCriterion("institution_subaccount in", values, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountNotIn(List<Integer> values) {
            addCriterion("institution_subaccount not in", values, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountBetween(Integer value1, Integer value2) {
            addCriterion("institution_subaccount between", value1, value2, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionSubaccountNotBetween(Integer value1, Integer value2) {
            addCriterion("institution_subaccount not between", value1, value2, "institutionSubaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountIsNull() {
            addCriterion("studentaccount is null");
            return (Criteria) this;
        }

        public Criteria andStudentaccountIsNotNull() {
            addCriterion("studentaccount is not null");
            return (Criteria) this;
        }

        public Criteria andStudentaccountEqualTo(Integer value) {
            addCriterion("studentaccount =", value, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountNotEqualTo(Integer value) {
            addCriterion("studentaccount <>", value, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountGreaterThan(Integer value) {
            addCriterion("studentaccount >", value, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountGreaterThanOrEqualTo(Integer value) {
            addCriterion("studentaccount >=", value, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountLessThan(Integer value) {
            addCriterion("studentaccount <", value, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountLessThanOrEqualTo(Integer value) {
            addCriterion("studentaccount <=", value, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountIn(List<Integer> values) {
            addCriterion("studentaccount in", values, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountNotIn(List<Integer> values) {
            addCriterion("studentaccount not in", values, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountBetween(Integer value1, Integer value2) {
            addCriterion("studentaccount between", value1, value2, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andStudentaccountNotBetween(Integer value1, Integer value2) {
            addCriterion("studentaccount not between", value1, value2, "studentaccount");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminIsNull() {
            addCriterion("institution_admin is null");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminIsNotNull() {
            addCriterion("institution_admin is not null");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminEqualTo(Integer value) {
            addCriterion("institution_admin =", value, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminNotEqualTo(Integer value) {
            addCriterion("institution_admin <>", value, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminGreaterThan(Integer value) {
            addCriterion("institution_admin >", value, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminGreaterThanOrEqualTo(Integer value) {
            addCriterion("institution_admin >=", value, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminLessThan(Integer value) {
            addCriterion("institution_admin <", value, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminLessThanOrEqualTo(Integer value) {
            addCriterion("institution_admin <=", value, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminIn(List<Integer> values) {
            addCriterion("institution_admin in", values, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminNotIn(List<Integer> values) {
            addCriterion("institution_admin not in", values, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminBetween(Integer value1, Integer value2) {
            addCriterion("institution_admin between", value1, value2, "institutionAdmin");
            return (Criteria) this;
        }

        public Criteria andInstitutionAdminNotBetween(Integer value1, Integer value2) {
            addCriterion("institution_admin not between", value1, value2, "institutionAdmin");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}