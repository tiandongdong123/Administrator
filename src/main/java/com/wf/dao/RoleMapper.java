package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Role;



public interface RoleMapper {
	
   List<Object> getRole(@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
   
   int getRoleNum();
   
   List<Object> checkRoleName(@Param("name") String name);
   
   int doAddRole(Role role);
   
   Role getRoleById(@Param("id") String id);
   
   int doUpdateRole(Role role);
   
   int deleteRole(@Param("id") String id);
   
   int deleteRoleByDepartment(@Param("department") String department);
   
   List<Object> getAllRole();

}