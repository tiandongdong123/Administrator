package com.wf.dao;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.WfksAccountidMapping;

public interface WfksAccountidMappingMapper {
    
    int insert(WfksAccountidMapping record);
    int insertSelective(WfksAccountidMapping record);
    
    int updateByPrimaryKeySelective(WfksAccountidMapping record);
    int updateByPrimaryKey(WfksAccountidMapping record);
    
    int deleteByUserId(@Param("idKey")String idKey, @Param("relatedidAccounttype")String type);
    int deleteByUserIdAndType(@Param("idKey")String idKey, @Param("idAccounttype")String type);
    
    WfksAccountidMapping[] selectAllByUserId(@Param("idKey")String idKey);
    WfksAccountidMapping[] selectByRelateIdKey(@Param("relatedidKey")String relatedidKey);
    WfksAccountidMapping[] selectByUserId(@Param("idKey")String idKey, @Param("relatedidAccounttype")String type);
    WfksAccountidMapping[] selectByUserIdAndType(@Param("idKey")String idKey, @Param("idAccounttype")String type);
}