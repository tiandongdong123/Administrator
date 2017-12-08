package com.wf.service;

import java.util.List;

import com.wf.bean.PageList;
import com.wf.bean.PageManager;

public interface PageManagerService {

	/**
	 * 查询页面列表
	 * @param modularName 模块名称
	 * @param pageName 页面名称
	 * @param pageNum 页码
	 * @param pageSize 每页显示数量
	 * @return
	 */
	
	PageList getPage(String[] ids, String pageName, Integer pageNum, Integer pageSize);

	PageManager getPageManagerById(String id);

	boolean doUpdatePageManager(PageManager pm);

	boolean deletePageManager(String id);

	boolean doAddPageManager(PageManager pm);

	PageList exportpage(String[] ids, String pageName);
	
	List<Object> getKeyWord(String pageName);
}
