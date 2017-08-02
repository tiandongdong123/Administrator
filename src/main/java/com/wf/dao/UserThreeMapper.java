package com.wf.dao;

import com.wf.bean.UserThree;

public interface UserThreeMapper {
    int insert(UserThree record);

    int insertSelective(UserThree record);
}