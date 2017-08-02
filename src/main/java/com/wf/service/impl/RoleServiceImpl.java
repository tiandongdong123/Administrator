package com.wf.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.bean.Role;
import com.wf.bean.SystemMenu;
import com.wf.dao.DepartmentMapper;
import com.wf.dao.RoleMapper;
import com.wf.dao.SystemMenuMapper;
import com.wf.dao.WfadminMapper;
import com.wf.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleMapper role;
	@Autowired
	private SystemMenuMapper menu;
	@Autowired
	private DepartmentMapper dept;
	@Autowired
	private WfadminMapper wfadminMapper;
	@Override
	public PageList getRole(Integer pagenum, Integer pagesize) {
		List<Object> rl = new ArrayList<Object>();
		PageList pl = new PageList();
		int num = 0;
		int pm = (pagenum-1)*pagesize;
		String roles;
		try {
			rl = this.role.getRole(pm, pagesize);
			num = this.role.getRoleNum();
			for(int i=0;i<rl.size();i++){
				roles=((Role)rl.get(i)).getPurview();
				
				if(StringUtils.isNoneBlank(roles)){
					List<String> list=Arrays.asList(roles.split(","));
					String sys1="";
					for(int j=0;j<list.size();j++){
						SystemMenu sys=menu.findPurviewById(list.get(j));
						if(j==0){
							sys1=sys.getMenuName();
						}else{
							sys1=sys1+","+sys.getMenuName();
						}
					}
					((Role)rl.get(i)).setPurview(sys1);
				}
			}
			pl.setPageRow(rl);
			pl.setPageNum(pagenum);
			pl.setPageSize(pagesize);
			pl.setPageTotal(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pl;
	}
	@Override
	public JSONArray getPurview() {
		List<SystemMenu> rl= new ArrayList<SystemMenu>();
		JSONArray  json = new JSONArray();
		try {
			rl = this.menu.getPurview();
			json = JSONArray.fromObject(rl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	@Override
	public boolean checkRoleName(String name) {
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li = this.role.checkRoleName(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		
		return rt;
	}
	@Override
	public boolean doAddRole(Role role) {
		boolean rt = false;
		int x = 0;
		try {
			role.setRoleId(UUID.randomUUID().toString());
			x = this.role.doAddRole(role);
		} catch (Exception e) {
			e.printStackTrace();
    }
		if(x>0){
			rt=true;
		}
		return rt;
	}
	@Override
	public List<Object> getAllDept() {
		List<Object> li = new ArrayList<Object>();
		try {
			li = this.dept.getAllDept();
		} catch (Exception e) {
		}
		return li;
	}
	@Override
	public Role getRoleById(String id) {
		Role role = new Role();
		try {
			role = this.role.getRoleById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return role;
	}
	@Override
	public boolean doUpdateRole(Role role) {
		boolean rt = false;
		int x = 0;
		try {
			x = this.role.doUpdateRole(role);
		} catch (Exception e) {
			e.printStackTrace();
    }
		if(x>0){
			rt=true;
		}
		return rt;
	}
	@Override
	public boolean deleteRole(String id) {
		boolean rt = false;
		int x = 0;
		//查询该角色下有没有管理员
		int role = wfadminMapper.selectByRoleId(id);
		if(role == 0){//没有管理员 执行删除操作
			
			try {
				x = this.role.deleteRole(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(x>0){
			rt = true;
		}
		return rt;
	}

}
