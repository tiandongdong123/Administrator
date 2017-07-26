package com.wf.service;

import java.util.HashMap;
import java.util.List;

import com.wf.bean.AuthorizeRelation;
import com.wf.bean.PageList;

public interface AuthorizeRelationService {
	List<Object> getAuthorizeRelationfind(AuthorizeRelation ar);
	PageList getAuthorizeRelation(AuthorizeRelation ar,Integer pagenum,Integer pagesize,String startDate,String endDate);
	List<HashMap<String, Object>> getAuthorizeRelationlist(AuthorizeRelation ar,Integer pagenum,Integer pagesize,String startDate,String endDate);
	HashMap<String, Object> getAuthorizeRelation2(Integer id);
	AuthorizeRelation addAuthorizeRelation(Integer authorizeId,String institutionId,Integer providerId,Integer proResourceId,Integer subjectId,String periodicalId,String detailsURL,String downloadURL);

	HashMap<String,Object> getgetAuthorizeRelationInfo(String user_id);
}
