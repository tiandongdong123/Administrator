package com.wf.service;

import java.util.List;
import com.wf.bean.Modular;
import com.wf.bean.PageList;

public interface ModularService {

	/**
	 * 查询模块信息集合
	 * @param modularName 模块名称
	 * @return
	 */
	
	List<Object> getModularList();

	boolean doAddModular(Modular md);
	
	PageList getModular(String[] ids, Integer pageNum, Integer pageSize);

	Modular getModularById(String id);

	boolean doUpdateModular(Modular md);

	boolean deleteModular(String id);
	
	List<Object> exportmodular(String[]ids);
}
