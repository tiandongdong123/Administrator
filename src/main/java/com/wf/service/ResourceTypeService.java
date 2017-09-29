package com.wf.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.wf.bean.PageList;
import com.wf.bean.ResourceType;

public interface ResourceTypeService {
	/**
	 * 新增资源类型
	 * @param resourceTypeService
	 * @return
	 */
	Boolean addResourceType(ResourceType resourceType);
	/**
	 * 分页查询资源类型
	 * @param pageNum
	 * @param pageSize
	 * @param typeName
	 * @return
	 */
	PageList getResourceType(int pageNum, int pageSize);
	/**
	 * ID查询
	 * @param id
	 * @return
	 */
	ResourceType findResourceType(String id);
	/**
	 * name查询
	 */
	PageList getResourceTypeByName(int pageNum, int pageSize, String typeName);
	/**
	 * 删除资源类型
	 * @param ids
	 * @return
	 */
	Boolean deleteResourceType(String ids);
	/**
	 * 更新资源类型
	 * @param resourceType
	 * @return
	 */
	Boolean updateResourceType(ResourceType resourceType);

	/**
	 * 资源类型上移
	 */

	 Boolean moveUpResource(String id);

	/**
	 * 资源类型下移
	 */

	Boolean moveDownResource(String id);
	
	/**
	* @Title: resourcePublish
	* @Description: TODO(资源类型提交功能) 
	* @return boolean 返回类型 
	* @author LiuYong 
	* @date 2 Dis 2016 5:25:25 PM
	 */
	boolean resourcePublish();
	
	List<ResourceType> getAll();
	
	JSONArray getAll1();
	
	/**
	 * 更新资源类型的状态
	 * @param typeState
	 * @param id
	 * @return
	 */
	int updateResourceTypeState(int typeState, String id);
	List<Object> exportResource(String typeName);
	
	Boolean checkRsTypeCode(String rsTypeCode);
	
	Boolean checkRsTypeName(String rsTypeName);
	
}
