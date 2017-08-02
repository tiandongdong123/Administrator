package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.Subject;
public interface SubjectMapper {
	/**
	 * 查询学科分类信息
	 * @return
	 */
	public List<Object>  selectSubjectInfor(Map<String,Object> map);
	/**
	 * 查询所有的学科分类信息
	 * @param map
	 * @return
	 */
	public List<Object>  selectSubjectInforAll(Map<String,Object> map);
	/**
	 * 单个查询学科分类信息
	 * @param ids
	 * @return
	 */
	public Subject findSubject(String ids);
	/**
	 * 增加学科分类
	 * @param Subject
	 * @return
	 */
	public int insertSubjects(Subject Subject);
	/**
	 * 删除学科分类
	 * @param ids
	 * @return
	 */
	public int deleteSubjects(String ids);
	/**
	 * 修改学科分类
	 * @param subject
	 * @return
	 */
	public int updateSubject(Subject subject);
	
	public Subject findSubjectParam(String className);
	
	/**
	* @Title: find
	* @Description: TODO(查询数据库相关学科数据) 
	* @return List<Object> 返回类型 
	* @author LiuYong 
	 * @param type 学科分类
	* @date 2 Dis 2016 4:34:52 PM
	 */
	public List<Object> find(String type);
	/**
	 * 根据学科编码查询中文名称
	 * @param classNum
	 * @return
	 */
	public List<String> queryNameByNum(String classNum);
}
