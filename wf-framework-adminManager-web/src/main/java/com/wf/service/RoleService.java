package com.wf.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.wf.bean.PageList;
import com.wf.bean.Role;

public interface RoleService {

	PageList getRole(Integer pagenum,Integer pagesize);
	
	JSONArray getPurview(); 
	
	boolean checkRoleName(String name);
	
	boolean doAddRole(Role role);
	
	List<Object> getAllDept();
	
	Role getRoleById(String id);
	
	boolean doUpdateRole(Role role);
	
	boolean deleteRole(String id);
}
