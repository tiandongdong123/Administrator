package com.wf.dao;

import java.util.List;
import java.util.ListIterator;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Wfadmin;


public interface WfadminMapper {
    
	List<Object> getAdmin(@Param("param1")String adminname,@Param("param2")String param2,@Param("param3")String param3,@Param("param4")String param4,@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
	
	int getAdminNum(@Param("param1")String adminname,@Param("param2")String param2,@Param("param3")String param3,@Param("param4")String param4);
	
	int deleteAdmin(@Param("ids") String[] ids);
	
	int selectByDepartment(@Param("department") String department);
	
	int selectByRoleId(@Param("role_id") String role_id);
	
	int closeAdmin(@Param("ids") String[] ids);
	
	int openAdmin(@Param("ids") String[] ids);
	
	List<Object> checkAdminId(@Param("id") String id);
	
	int doAddAdmin(Wfadmin admin);
	
	Wfadmin getAdminById(@Param("id")String id);
	
	int doUpdateAdmin(Wfadmin admin);
	
	List<String> getAdminByAdminIds(@Param("ids")String[] ids);

	List<String> getAdminNames(@Param("name")String name);

}