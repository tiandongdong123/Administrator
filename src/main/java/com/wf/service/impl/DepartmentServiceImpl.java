package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.Department;
import com.wf.bean.PageList;
import com.wf.dao.DepartmentMapper;
import com.wf.dao.RoleMapper;
import com.wf.dao.WfadminMapper;
import com.wf.service.DepartmentService;
@Service
public class DepartmentServiceImpl implements DepartmentService{

	@Autowired
	private DepartmentMapper dept;
	@Autowired
	private WfadminMapper wfadminMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Override
	public PageList getDept(Integer pagenum,Integer pagesize) {
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			int startnum = (pagenum-1)*pagesize;
			r = this.dept.getDept(startnum,pagesize);
			int num = this.dept.getDeptNum();
			p.setPageRow(r);
			p.setPageNum(pagenum);
			p.setPageTotal(num);
			p.setPageSize(pagesize);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	@Override
	public boolean doAddDept(Department dept) {
		boolean rt = false;
		int num = 0;
		try {
			num = this.dept.doAddDept(dept);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public Department getDeptById(Integer id) {
		Department dp = new Department();
		try {
			dp = this.dept.getDeptById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dp;
	}
	@Override
	public boolean doUpdateDept(Department dept) {
		boolean rt = false;
		int num = 0;
		try {
			num = this.dept.doUpdateDept(dept);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public boolean deleteDept(Integer id) {
		boolean rt = false;
		int num = 0;
		//根据部门id查询部门名称
		String department = this.dept.getDeptById(id).getDeptName();
		//根据部门名称查询该部门是否有管理员和该部门对应的角色是否有管理员
		int adminNum = this.wfadminMapper.selectByDepartment(department);
		if(adminNum == 0 ){//没有管理员  执行删除操作
			
			try {
				//根据部门名称删除对应的角色
				this.roleMapper.deleteRoleByDepartment(department);
				//删除部门
				num = this.dept.deleteDept(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public boolean checkDept(String deptname){
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li= this.dept.checkDept(deptname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		return rt;
	}
}
