package com.wf.service;

import java.util.HashMap;
import java.util.List;
import com.wf.bean.Authorize;
import com.wf.bean.PageList;

/** 
 * 
 */
public interface AuthorizeService {
	
	PageList findAuthorizePage(String institutionId,Integer pagenum,Integer pagesize);
	/**
	 * 获取授权信息
	 * @param a
	 * @param pagenum
	 * @param pagesize
	 * @param institutionIds
	 * @return
	 */
	List<HashMap<String, Object>> getAuthorizelist(String institutionId,Integer pagenum,Integer pagesize);
	List<HashMap<String, Object>> getAuthorize(Authorize authorize);
	HashMap<String, Object> getAuthorize(Integer id);
	/** 添加 */
	Authorize addAuthorize(String institutionId, Integer providerId, String username, String password);
	
	}
