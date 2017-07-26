package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.ShareTemplate;

public interface ShareTemplateMapper {
	/**
	 * 新增资源类型
	 * @param resourceType
	 * @return
	 */
	public int insertShareTemplate(ShareTemplate shareTemplate);
	/**
	 * 查询所有
	 * @return
	 */
	public List<ShareTemplate> selectAll();
	/**
	 * 按ID查询
	 * @param ids
	 * @return
	 */
	public ShareTemplate findShareTemplate(String ids);
	/**
	 * 查询资源类型
	 * @param map
	 * @return
	 */
	public List<Object> selectShareTemplateInfor(Map<String,Object> map);
	/**
	 * 查询资源类型
	 * @param map
	 * @return
	 */
	public List<Object> selectShareTemplateInforAll(Map<String,Object> map);
	/**
	 * 删除资源类型
	 * @param ids
	 * @return
	 */
	public int deleteShareTemplate(String ids);
	/**
	 * 更新资源类型
	 * @param resourceType
	 * @return
	 */
	public int updateShareTemplate(ShareTemplate shareTemplate);
	
	public ShareTemplate findShareTemplateParam(String shareType);
	
	public ShareTemplate findShareTemplateParamToUpdate(Map<String,Object> map);
}
