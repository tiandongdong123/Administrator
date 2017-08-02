package com.wf.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.bean.Department;
import com.wf.bean.PageList;
import com.wf.service.DepartmentService;

@Controller
@RequestMapping("dept")
public class DepartmentController {

	@Autowired
	private DepartmentService dept;
	@RequestMapping("getdept")
	@ResponseBody
	public PageList getDept(Integer pagenum,Integer pagesize){
		PageList pl = this.dept.getDept(pagenum, pagesize);
		return pl;
	}
	
	@RequestMapping("adddept")
	public String addDept(){	
		return "/page/systemmanager/add_dept_manager";
	}
	
	@RequestMapping("doadddept")
	@ResponseBody
	public boolean doAddDept(@ModelAttribute Department dept){
		boolean rt = this.dept.doAddDept(dept);
		return rt;
	}
	
	@RequestMapping("updatedept")
	public String updateDept(Integer id,Map<String,Object> map){
		Department dp = this.dept.getDeptById(id);
		map.put("dept", dp);
		return "/page/systemmanager/update_dept_manager";
	}
	
	@RequestMapping("doupdatedept")
	@ResponseBody
	public boolean doUpdateDept(@ModelAttribute Department dept){
		boolean rt = this.dept.doUpdateDept(dept);
		return rt;
	}
	
	@RequestMapping("deletedept")
	@ResponseBody
	public boolean deleteDept(Integer id){
		boolean rt = this.dept.deleteDept(id);
		return rt;
	}
	
	@RequestMapping("checkdept")
	@ResponseBody
	public boolean checkDept(String deptname){
		boolean rt = this.dept.checkDept(deptname);
		return rt;
	}
}
