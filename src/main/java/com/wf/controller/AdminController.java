package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
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

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.bean.Wfadmin;
import com.wf.service.AdminService;
import com.wf.service.LogService;

@Controller
@RequestMapping("admin")
public class AdminController {

	@Autowired
	private AdminService admin;
	
	@Autowired
	LogService logService;
	
	/**查询管理员
	 * @throws UnknownHostException 
	 * 
	 */
	@RequestMapping("getadmin")
	@ResponseBody
	public PageList getAdmin(HttpServletRequest request,
			@RequestParam(value="pagenum",required=false) Integer pagenum,
			@RequestParam(value="pagesize",required=false) Integer pagesize,
			@RequestParam(value="adminname",required=false) String adminname){
		PageList pl = this.admin.getAdmin(adminname, pagenum, pagesize);
		
		//记录日志
		Log log=new Log("管理员管理","查询","检索词:"+adminname,request);
		logService.addLog(log);
		
		return pl;
	}
	
	@RequestMapping("serach")
	@ResponseBody
	public object serach(@RequestParam(value="word",required=false)String word) {
	
		return null;
	}
	
	/**
	 * 删除管理员
	 * @param request 
	 * @throws Exception 
	 */
	@RequestMapping("deleteadmin")
	@ResponseBody
	public boolean deleteAdmin(@RequestParam(value="ids[]",required=false) String[] ids, HttpServletRequest request){
		boolean rt = this.admin.deleteAdmin(ids);
		
		Log log=new Log("管理员管理","删除","删除管理员ID:"+(ids==null?"":Arrays.asList(ids)),request);
		logService.addLog(log);
		
		return rt;
	}
	/**
	 * 冻结账号
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("closeadmin")
	@ResponseBody
	public boolean closeAdmin(@RequestParam(value="ids[]",required=false) String[] ids,HttpServletRequest request){
		boolean rt = this.admin.closeAdmin(ids);
		
		//记录日志
		Log log=new Log("管理员管理","冻结","冻结账号:"+(ids==null?"":Arrays.asList(ids)),request);
		logService.addLog(log);
		
		return rt;
	}
	/**
	 * 解冻账号
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("openadmin")
	@ResponseBody
	public boolean openAdmin(@RequestParam(value="ids[]",required=false) String[] ids,HttpServletRequest request){
		boolean rt = this.admin.openAdmin(ids);
		
		//记录日志
		Log log=new Log("管理员管理","冻结","解冻账号:"+(ids==null?"":Arrays.asList(ids)),request);
		logService.addLog(log);
		
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
	 * @throws Exception 
	 */
	@RequestMapping("doaddadmin")
	@ResponseBody
	public boolean doAddAdmin(@ModelAttribute Wfadmin admin,HttpServletRequest request){
		boolean rt = this.admin.doAddAdmin(admin);
		
		//记录日志
		Log log=new Log("管理员管理","增加","增加管理员信息:"+admin.toString(),request);
		logService.addLog(log);
		
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
	public boolean doUpdateAdmin(@ModelAttribute Wfadmin admin,HttpServletRequest request ){
		
		//记录日志
		Log log=new Log("管理员管理","修改","修改后管理员信息:"+admin.toString(),request);
		logService.addLog(log);
		
		boolean rt = this.admin.doUpdateAdmin(admin);
		return rt;
	}
}
