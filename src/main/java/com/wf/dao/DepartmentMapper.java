package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Department;


public interface DepartmentMapper {
	
   List<Object> getDept(@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
   
   int getDeptNum();
   
   int doAddDept(Department dept);
   
   Department getDeptById(@Param("id") Integer id);
   
   int doUpdateDept(Department dept);
   
   int deleteDept(@Param("id") Integer id);
   
   List<Object> checkDept(@Param("deptname")String deptname);
   
   List<Object> getAllDept();
   
}