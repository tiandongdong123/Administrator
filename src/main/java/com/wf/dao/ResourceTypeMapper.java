package com.wf.dao;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ResourceType;

public interface ResourceTypeMapper {
	/**
	 * 新增资源类型
	 * @param resourceType
	 * @return
	 */
	public int insertResourceType(ResourceType resourceType);
	/**
	 * 按ID查询
	 * @param ids
	 * @return
	 */
	public ResourceType findResourceType(String ids);
	/**
	 * 查询资源类型
	 * @param map
	 * @return
	 */
	public List<Object> selectResourceTypeInfor(Map<String,Object> map);
	/**
	 * 查询资源类型
	 * @param map
	 * @return
	 */
	public List<Object> selectResourceTypeInforAll(@Param("typeName") String typeName);
	/**
	 * 删除资源类型
	 * @param ids
	 * @return
	 */
	public int deleteResourceType(String ids);
	/**
	 * 更新资源类型
	 * @param resourceType
	 * @return
	 */
	public int updateResourceType(ResourceType resourceType);
	/**
	 * 更新资源类型状态 
	 * @param typeState
	 * @param id
	 * @return
	 */
	public int updateResourceTypeState(@Param("typeState") int typeState,@Param("id") String id);																					  
	
	public ResourceType findResourceTypeParam(Map<String , Object> map);
	
	public List<ResourceType> getRtype();
	   
	public List<ResourceType> getRtypeName();
	
	/**
	* @Title: find
	* @Description: TODO(查询数据库相关资源分类数据) 
	* @return List<Object> 返回类型 
	* @author LiuYong 
	* @date 2 Dis 2016 4:34:52 PM
	 */
	public List<Object> find();
	
	ResourceType getOne(String code);
	
	List<ResourceType> getAll();

	List<String> getAllName();

	List<String> getResourceByCode(@Param("code") String[] database_name);
	
	JSONArray getAll1();
	
	String checkRsTypeCode(String rsTypeCode);
	
	String checkRsTypeName(String rsTypeName);
}
