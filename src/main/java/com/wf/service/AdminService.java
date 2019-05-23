package com.wf.service;

import java.util.List;

import com.wf.bean.PageList;
import com.wf.bean.Wfadmin;

public interface AdminService {

	PageList getAdmin(String adminname,Integer pagenum,Integer pagesize);
	
	boolean deleteAdmin(String[] ids);
	
	boolean closeAdmin(String[] ids);
	
	boolean openAdmin(String[] ids);
	
	List<Object> getDept();
	
	List<Object> getRole();
	
	boolean checkAdminId(String id);
	
	boolean doAddAdmin(Wfadmin admin);
	
	Wfadmin getAdminById(String id);
	
	boolean doUpdateAdmin(Wfadmin admin);

    List<String> getAdminNames(String name);
}
