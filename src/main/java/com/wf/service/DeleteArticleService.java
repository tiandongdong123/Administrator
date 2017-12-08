package com.wf.service;

import java.util.List;

import com.wf.bean.DeleteArticle;
import com.wf.bean.PageList;

public interface DeleteArticleService {
	
	/**
	 * 根据条件查询下撤的数据
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	PageList getArticleList(String startTime, String endTime, String model, String id, int pageNum,
			int pageSize);

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
	 * @param model
	 * @param id
	 * @return
	 */
	Integer deleteArticleList(String startTime,String endTime,String model,String id);

	/**
	 * 根据id删除下撤数据
	 * @param id
	 * @return
	 */
	Integer deleteArticleById(String id);
	
	/**
	 * 查询solr模块参数表
	 * @return
	 */
	List<Object> getTypeList();
	
}
