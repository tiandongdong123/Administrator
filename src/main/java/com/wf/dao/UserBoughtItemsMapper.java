package com.wf.dao;

import java.util.List;

import com.wf.bean.UserBoughtItems;

/**
 * 
 * 用户购买项目
 *
 */
public interface UserBoughtItemsMapper {
	
	List<UserBoughtItems> getUserBoughtItemsList(String userId);
	
    int delete(String userId);

    int insert(UserBoughtItems item);
    
    int update(UserBoughtItems item);
}
