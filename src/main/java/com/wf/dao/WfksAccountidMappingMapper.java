package com.wf.dao;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.WfksAccountidMapping;

public interface WfksAccountidMappingMapper {
    int insert(WfksAccountidMapping record);
    int deleteByUserId(@Param("idKey")String idKey, @Param("relatedidAccounttype")String type);
    int deleteByUserIdAndType(@Param("idKey")String idKey, @Param("idAccounttype")String type);
    int deleteByIdKeyRelatedId(@Param("idKey")String idKey, @Param("relatedidKey")String relatedidKey);
    WfksAccountidMapping[] getWfksAccountidByIdKey(@Param("idKey")String idKey);
    WfksAccountidMapping[] getWfksAccountid(@Param("idKey")String idKey, @Param("relatedidAccounttype")String type);
    WfksAccountidMapping[] getWfksAccountidLimit(@Param("idKey")String idKey, @Param("idAccounttype")String type);
    WfksAccountidMapping[]  getWfksAccountidByRelatedidKey(@Param("relatedidKey")String relatedidKey);
}