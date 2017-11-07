package com.wf.dao;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.WfksAccountidMapping;

public interface WfksAccountidMappingMapper {
    int deleteByUserId(@Param("idKey")String idKey, @Param("relatedidAccounttype")String msg);

    int insert(WfksAccountidMapping record);

    int insertSelective(WfksAccountidMapping record);

    WfksAccountidMapping selectByUserId(@Param("idKey")String idKey, @Param("relatedidAccounttype")String msg);

    WfksAccountidMapping[] selectAllByUserId(@Param("idKey")String idKey);

    int updateByPrimaryKeySelective(WfksAccountidMapping record);

    int updateByPrimaryKey(WfksAccountidMapping record);
    
    WfksAccountidMapping[] selectByRelateIdKey(@Param("relatedidKey")String relatedidKey);
}