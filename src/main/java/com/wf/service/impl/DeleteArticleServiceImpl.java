package com.wf.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.DeleteArticle;
import com.wf.bean.PageList;
import com.wf.dao.DeleteArticleMapper;
import com.wf.service.DeleteArticleService;

@Service
public class DeleteArticleServiceImpl implements DeleteArticleService {

	@Autowired
	private DeleteArticleMapper deleteArticle;

	@Override
	public PageList getArticleList(String startTime, String endTime,int pageNum, int pageSize) {
		if(StringUtils.isEmpty(startTime)) startTime = null;
		if(StringUtils.isEmpty(endTime)){
			 endTime = null;
		}else{
			endTime=endTime+" 23:59:59";
		}
		int pageStart = (pageNum-1)*pageSize;//当前页的第一条
		List<Object> list = deleteArticle.queryArticle(startTime,endTime,pageStart,pageSize);
		int size = deleteArticle.queryArticleSize(startTime,endTime);
		PageList pl = new PageList();
		pl.setPageRow(list);//查询结果列表
		pl.setTotalRow(size);//总条数
		pl.setPageNum(pageNum);//当前页
		pl.setPageSize(pageSize);//每页显示的数量
		return pl;
	}

	@Override
	public Integer saveDeleteArticle(DeleteArticle article) {
		return deleteArticle.saveDeleteArticle(article);
	}

	@Override
	public Integer deleteArticleList(String startTime, String endTime) {
		return deleteArticle.deleteArticleList(startTime, endTime);
	}

	@Override
	public Integer saveDeleteArticleList(List<DeleteArticle> list) {
		return deleteArticle.saveDeleteArticleList(list);
	}

	@Override
	public Integer deleteArticleById(String id) {
		return deleteArticle.deleteArticleById(id);
	}

}
