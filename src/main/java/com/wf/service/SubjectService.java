package com.wf.service;

import java.util.List;

import com.wf.bean.PageList;
import com.wf.bean.Subject;

public interface SubjectService {
	/**
	 * 按条件查询学科分类信息
	 * @param pageNum 	起始页
	 * @param pageSize 	页面容量
	 * @param level    	级别
	 * @param classNum 	分类号
	 * @param className 分类名称
	 * @return
	 */
	PageList getSubject(int pageNum,int pageSize,String level,String classNum,String className);
	/**
	 * 查询单个学科分类信息
	 * @param id 查询ID
	 * @return
	 */
	Subject findSubject(String id);
	/**
	 * 增加学科分类
	 * @param subject
	 * @return
	 */
	Boolean insertSubject(Subject subject);
	/**
	 * 删除学科分类
	 * @param ids
	 * @return
	 */
	Boolean deleteSubject(String ids);
	/**
	 * 修改学科分类
	 * @param subject 
	 * @return
	 */
	Boolean updateSubject(Subject subject);
	
	/**
	* @Title: subjectPublish
	* @Description: TODO(学科发布功能) 
	* @return boolean 返回类型 
	* @author LiuYong 
	* @date 2 Dis 2016 4:29:01 PM
	 */
	boolean subjectPublish();
	
	/**
	* @Title: insertSubjects
	* @Description: TODO(向学科表批量插入数据) 
	* @return Boolean 返回类型 
	* @author LiuYong 
	* @date 3 Dis 2016 2:00:50 PM
	 */
	Boolean insertSubjects();
	
	
	List<Object> exportSubject(String level,String classNum,String className);
}
