package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Department;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.service.DepartmentService;
import com.wf.service.LogService;

@Controller
@RequestMapping("dept")
public class DepartmentController {

	@Autowired
	private DepartmentService dept;
	
	
	@Autowired
	LogService logService;
	
	@RequestMapping("getdept")
	@ResponseBody
	public PageList getDept(Integer pagenum,Integer pagesize,HttpServletRequest request) throws Exception{
		
		PageList pl = this.dept.getDept(pagenum, pagesize);
		
		//记录日志
		Log log=new Log("部门管理","查询","",request);
		logService.addLog(log);
		
		return pl;
	}
	
	@RequestMapping("adddept")
	public String addDept(){	
		return "/page/systemmanager/add_dept_manager";
	}
	
	@RequestMapping("doadddept")
	@ResponseBody
	public boolean doAddDept(@ModelAttribute Department dept,HttpServletRequest request){
		boolean rt = this.dept.doAddDept(dept);
		
		//记录日志
		Log log=new Log("部门管理","增加",dept.toString(),request);
		logService.addLog(log);
		
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
	public boolean doUpdateDept(@ModelAttribute Department dept,HttpServletRequest request){
		boolean rt = this.dept.doUpdateDept(dept);
		
		//记录日志
		Log log=new Log("部门管理","修改",dept.toString(),request);
		logService.addLog(log);
		
		return rt;
	}
	
	@RequestMapping("deletedept")
	@ResponseBody
	public boolean deleteDept(Integer id,HttpServletRequest request){
		boolean rt = this.dept.deleteDept(id);
		
		//记录日志
		Log log=new Log("部门管理","删除","删除部门ID:"+id,request);
		logService.addLog(log);
		
		return rt;
	}
	
	@RequestMapping("checkdept")
	@ResponseBody
	public boolean checkDept(String deptname){
		boolean rt = this.dept.checkDept(deptname);
		return rt;
	}
}
