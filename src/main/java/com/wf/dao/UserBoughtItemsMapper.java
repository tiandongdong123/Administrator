package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.UserBoughtItems;

/**
 * 
 * 用户购买项目
 *
 */
public interface UserBoughtItemsMapper {
	
	List<UserBoughtItems> getUserBoughtItemsList(String userId);
	
    int delete(@Param("UserId")String UserId,@Param("TransteroutType")String TransteroutType,@Param("Mode")String Mode);

    int insert(UserBoughtItems item);
    
    int update(UserBoughtItems item);
}
