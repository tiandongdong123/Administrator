package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.wf.bean.AuthorizeRelation;


public interface AuthorizeRelationMapper {
	
	/**
	 *	查询所有 授权-资源类型-学科-母体 （关联表） 信息
	 */
	List<Object> getAuthorizeRelation(@Param("ar") AuthorizeRelation ar,@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize,@Param("onPerIds") List<String> onPerIds);
	
	AuthorizeRelation getAuthorizeRelation2(@Param("id") Integer id);
	List<AuthorizeRelation> getauthorizeRelationByName(@Param("institutionId")String  institutionId);
	int getAuthorizeRelationNum(@Param("ar") AuthorizeRelation ar,@Param("onPerIds") List<String> onPerIds);
	int addAuthorizeRelation(AuthorizeRelation ar);
	
	List<Object> getAuReByPeriodical(@Param("authorizeId") Integer authorizeId);
	
	/****修改  删除   ****/
	int updateAuthorizeRelation(@Param("ar")AuthorizeRelation ar);
	int deleteAuthorizeRelation(@Param("id")Integer id);
	/***根据母体id 查寻授权数量 **/
	int getAuthorizeRelations(@Param("periodicalId")String periodicalId);
}
