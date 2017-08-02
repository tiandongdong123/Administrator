package com.wf.service;

import java.util.HashMap;
import java.util.List;

import com.wf.bean.PSubjectCategory;

public interface PSubjectCategoryService {
	
	List<HashMap<String, Object>> getPSubjectCategory(Integer providerId,Integer proResourceId);
	
	/**增加学科分类*/
	int addPSubjectCategory(Integer providerId,String pCategoryCodes,String pCategoryName,Integer parentId,Integer proResourceId);
	/**修改学科分类*/
	boolean updatePSubjectCategory(Integer providerId,String pCategoryCodes,String pCategoryName,Integer parentId,Integer id,Integer proResourceId);
	/**删除学科分类*/
	boolean deletePSubjectCategory(Integer id);
	/** 查询单个资源提供商**/
	PSubjectCategory getPSubjectCategoryParent(Integer id);
	
	
}
