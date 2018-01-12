package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.PageManager;

public interface PageManagerMapper {

    
	List<Object> getPage(@Param("ids") String[] ids,@Param("pageName") String pageName,@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize);

	int getPageNum(@Param("ids") String[] ids,@Param("pageName") String pageName);

	PageManager getPageManagerById(@Param("id") String id);

	int doUpdatePageManager(PageManager pm);
	
	 int deletePageManager(@Param("id") String id);

	int doAddPageManager(PageManager pm);
	
	
	List<Object> getKeyword(@Param("pageName")String pageName);
	
	String getLink_Address(String pagename);
	
	List<Object> findAllPages();
	
	List<Object>  getAllPage(@Param("ids") String[] ids,@Param("pageName") String pageName);
    
	Integer getCountBymodularId(@Param("modelarId")String modelarId);
	
}