package com.wf.dao;

import com.wf.bean.UserAccountRestriction;

/**
 *	账号限制 Mapper 
 */
public interface UserAccountRestrictionMapper {
    int deleteAccountRestriction(String userId);

    int insert(UserAccountRestriction record);
    
    int update(UserAccountRestriction record);
}