package com.wf.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ecs.xhtml.object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.bean.PageList;
import com.wf.bean.Wfadmin;
import com.wf.service.AdminService;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private AdminService admin;
	
	/**查询管理员
	 * 
	 */
	@RequestMapping("getadmin")
	@ResponseBody
	public PageList getAdmin(HttpServletRequest request,
			@RequestParam(value="pagenum",required=false) Integer pagenum,
			@RequestParam(value="pagesize",required=false) Integer pagesize,
			@RequestParam(value="adminname",required=false) String adminname){
		PageList pl = this.admin.getAdmin(adminname, pagenum, pagesize);
		return pl;
	}
	
	@RequestMapping("serach")
	@ResponseBody
	public object serach(@RequestParam(value="word",required=false)String word) {
	
		return null;
	}
	
	/**
	 * 删除管理员
	 */
	@RequestMapping("deleteadmin")
	@ResponseBody
	public boolean deleteAdmin(@RequestParam(value="ids[]",required=false) String[] ids){
		boolean rt = this.admin.deleteAdmin(ids);
		return rt;
	}
	/**
	 * 冻结账号
	 * @param ids
	 * @return
	 */
	@RequestMapping("closeadmin")
	@ResponseBody
	public boolean closeAdmin(@RequestParam(value="ids[]",required=false) String[] ids){
		boolean rt = this.admin.closeAdmin(ids);
		return rt;
	}
	/**
	 * 解冻账号
	 * @param ids
	 * @return
	 */
	@RequestMapping("openadmin")
	@ResponseBody
	public boolean openAdmin(@RequestParam(value="ids[]",required=false) String[] ids){
		boolean rt = this.admin.openAdmin(ids);
		return rt;
	}
	
	/**
	 * 添加管理员页面
	 * @param map
	 * @return
	 */
	@RequestMapping("addadmin")
	public String addAdmin(Map<String,Object> map){
		List<Object> deptname = this.admin.getDept();
		List<Object> rolename = this.admin.getRole();
		map.put("deptname", deptname);
		map.put("rolename", rolename);
		return "/page/systemmanager/add_admin";
	}
	/**
	 * 判断用户名是否重复
	 * @param id
	 * @return
	 */
	@RequestMapping("checkadminid")
	@ResponseBody
	public boolean checkAdminId(String id){
		boolean rt = this.admin.checkAdminId(id);
		return rt;
	}
	
	/**
	 * 添加管理员
	 * @param admin
	 * @return
	 */
	@RequestMapping("doaddadmin")
	@ResponseBody
	public boolean doAddAdmin(@ModelAttribute Wfadmin admin){
		boolean rt = this.admin.doAddAdmin(admin);
		return rt;
	}
	/**
	 * 修改管理员页面
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping("updateadmin")
	public String updateAdmin(Map<String,Object> map,String id,Integer pagenum){
		List<Object> deptname = this.admin.getDept();
		List<Object> rolename = this.admin.getRole();
		Wfadmin admin = this.admin.getAdminById(id);
		map.put("admin", admin);
		map.put("deptname", deptname);
		map.put("rolename", rolename);
		map.put("pagenum",pagenum);
		return "/page/systemmanager/update_admin";
	}
	
	/**
	 * 修改管理员
	 * @param admin
	 * @return
	 */
	@RequestMapping("doupdateadmin")
	@ResponseBody
	public boolean doUpdateAdmin(@ModelAttribute Wfadmin admin){
		boolean rt = this.admin.doUpdateAdmin(admin);
		return rt;
	}
}
