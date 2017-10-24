package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.DeleteArticle;

public interface DeleteArticleMapper {

	/**
	 * 添加一条下撤的数据
	 * 
	 * @param article
	 * @return
	 */
	Integer saveDeleteArticle(DeleteArticle article);
	
	/**
	 * 批量添加一条下撤的数据
	 * 
	 * @param list
	 * @return
	 */
	Integer saveDeleteArticleList(List<DeleteArticle> list);
	
	/**
	 * 根据条件删除下撤的数据
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer deleteArticleList(@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	/**
	 * 根据id删除下撤数据
	 * 
	 * @param id
	 * @return
	 */
	Integer deleteArticleById(@Param("id")String id);
	
	/**
	 * 根据条件查询下撤的数据
	 * 
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Object> queryArticle(@Param("startTime") String startTime,
			@Param("endTime") String endTime, @Param("pageNum") int pageNum,
			@Param("pageSize") int pageSize);
	
	/**
	 * 根据条件查询下撤的数据的总数
	 * @param startTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	int queryArticleSize(@Param("startTime") String startTime, @Param("endTime") String endTime);
	
	/**
	 * 查询solr模块参数表
	 * 
	 * @return
	 */
	List<Object> getTypeList();

}
