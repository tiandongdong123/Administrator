package com.wf.service;

import java.util.List;

import com.wf.bean.ShareTemplateNamesFileds;

public interface ShareTemplateNamesFiledsService {

	/**
	 * 根据分享类型ID获取该类型的所有字段
	 * 
	 * @param id
	 *            分享类型名称ID
	 * @return
	 */
	List<ShareTemplateNamesFileds> getFiledsByShareNameId(Integer id);
	
	
	/**
	 * 根据分享类型中文获取该类型的所有字段
	 * @param id 分享类型名称
	 * @return
	 */
	List<ShareTemplateNamesFileds> getFiledsByShareNameType(String type);
}
