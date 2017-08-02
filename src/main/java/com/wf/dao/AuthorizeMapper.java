package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Authorize;


public interface AuthorizeMapper {
	
	/**
	 *添加 授权
	 * @param authorize
	 * @return
	 */
	int addAuthorize(Authorize authorize);
	int getAuthorizeNum(@Param("authorize") Authorize authorize);
	List<Object> getAuthorize(@Param("authorize") Authorize authorize,@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
	Authorize getAuthorize2(@Param("id")Integer id);
	//通过机构名称查信息
	Authorize getAuthorizeByName(@Param("institutionId")String  institutionId);
	/*********删除  修改************/
	int deleteAuthorize(@Param("id")Integer id);
	int updateAuthorize(@Param("institutionId")String institutionId,@Param("providerId")Integer providerId,@Param("username")String username,@Param("password")String password,@Param("id")Integer id);
	

}
