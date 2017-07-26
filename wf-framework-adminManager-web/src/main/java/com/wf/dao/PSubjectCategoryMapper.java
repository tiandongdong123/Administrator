package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.PSubjectCategory;


public interface PSubjectCategoryMapper {
	
	/**
	 *	查询所有资源提供商  学科分类
	 */
	List<PSubjectCategory> getPSubjectCategory(@Param("providerId")Integer providerId,@Param("proResourceId")Integer proResourceId);
	
	PSubjectCategory getPSubjectCategoryParent(@Param("id")Integer id);
	
	//增加学科分类
	int addPSubjectCategory(@Param("providerId")Integer providerId,@Param("pCategoryCodes")String pCategoryCodes,@Param("pCategoryName")String pCategoryName,@Param("parentId")Integer parentId,@Param("proResourceId")Integer proResourceId);
	//修改学科分类
	boolean updatePSubjectCategory(@Param("providerId")Integer providerId,@Param("pCategoryCodes")String pCategoryCodes,@Param("pCategoryName")String pCategoryName,@Param("parentId")Integer parentId,@Param("id")Integer id,@Param("proResourceId")Integer proResourceId);
	//删除学科分类
	boolean deletePSubjectCategory(@Param("id")Integer id);

	
}
