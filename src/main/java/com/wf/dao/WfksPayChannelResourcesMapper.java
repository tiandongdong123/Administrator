package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.WfksPayChannelResources;

public interface WfksPayChannelResourcesMapper {
    int deleteByPrimaryKey(String id);

    int insert(WfksPayChannelResources record);

    int insertSelective(WfksPayChannelResources record);

    int updateByPrimaryKeySelective(WfksPayChannelResources record);
    
    int updateByPrimaryKeyWithBLOBs(WfksPayChannelResources record);
    
    int updateByPrimaryKey(WfksPayChannelResources record);

    /** 通过userId查询详情限定列表 */
    List<WfksPayChannelResources> selectByUserId(String userId);
    
    /** 查询一个用户购买项目下的资源库 */
    List<Map<String, Object>> selectProjectLibrary(Map<String, Object> map);
    /** 查询一个用户购买项目下的的中文名字 */
    List<Map<String, String>> selectProjectLibraryName(String userId);
    
    
    /** 查询用户购买项目下资源库的产品 */
    List<Map<String, Object>> selectProduct(Map<String, Object> map);
    /** 通过channelId查询详情限定列表 */
    List<WfksPayChannelResources> selectByChannelId(String channelId);
    
    /**通过userid获取所有的购买资源*/
    List<String> getAllResourceByUserID(@Param("users")List<String> users);
    
}