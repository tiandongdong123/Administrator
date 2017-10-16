package com.wf.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wf.bean.PageList;
import com.wf.bean.Person;

public interface PersonService {

	/**
	* @Title: QueryPersion
	* @Description: TODO(查询页面列表数据) 
	* @param p
	* @param pagenum 点击前台，预查询数据的页码
	* @param pagesize 每页预显示的数据条数
	* @param roles 预查询的角色
	* @return PageList 返回类型 
	* @author LiuYong 
	* @date 7 Dis 2016 7:01:26 PM
	 */
	Map<String,Object> QueryPersion(Person p,Integer pagenum,Integer pagesize,String[] roles);
	/**
	 * 查询机构用过户
	 * @param userId
	 * @param institution
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	PageList QueryIns(String userId,String institution,Integer pagenum,Integer pagesize);

	/**
	* @Title: findById
	* @Description: TODO(通过id查询person数据) 
	* @param userId
	* @return Person 返回类型 
	* @author LiuYong 
	* @date 10 Dis 2016 10:48:09 AM
	 */
	Person findById(String userId);
	
	/**
	 * 根据机构名称获取机构下的所有管理员
	 * @param institution 机构名称
	 * @return
	 */
	List<Map<String, Object>> getAllInstitutional(String institution);
	
	
	/**
	 * 获取所有机构名称
	 * @param institution 机构名称
	 * @return
	 */
	List<String> getAllInstitution(String institution);
	
}
