package com.wf.service;

import com.wf.bean.Department;
import com.wf.bean.PageList;

public interface DepartmentService {

	PageList getDept(Integer pagenum,Integer pagesize);
	
	boolean doAddDept(Department dept);
	
	Department getDeptById(Integer id);
	
	boolean doUpdateDept(Department dept);
	
	boolean deleteDept(Integer id);
	
	boolean checkDept(String deptname);
}
