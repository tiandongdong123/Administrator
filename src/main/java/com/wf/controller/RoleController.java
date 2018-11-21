package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.bean.Role;
import com.wf.service.LogService;
import com.wf.service.RoleService;

@Controller
@RequestMapping("role")
public class RoleController {

	@Autowired
	private RoleService role;
	
	
	@Autowired
	LogService logService;
	
	/**
	 * 查询管理员
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("getrole")
	@ResponseBody
	public PageList getRole(Integer pagesize,Integer pagenum,HttpServletRequest request){
		PageList pl = this.role.getRole(pagenum, pagesize);
		
		//记录日志
		Log log=new Log("角色管理","查询","",request);
		logService.addLog(log);

		return pl;
	}

	
	/**
	 * 菜单权限获取树
	 * @return
	 */
	@RequestMapping("getpurview")
	@ResponseBody
	public JSONArray getPurview(){
		JSONArray array = this.role.getPurview();
		for(int i = 0; i < array.size();i++){
			JSONObject  obj = array.getJSONObject(i);
			String name = obj.getString("menuName");
			String id = obj.getString("menuId");
			obj.element("menuName",id+"_"+name);
		}
		return array;
	}
	/**
	 * 查询角色名称是否重复
	 * @param name
	 * @return
	 */
	@RequestMapping("checkrolename")
	@ResponseBody
	public boolean checkRoleName(String name){
		boolean rt = this.role.checkRoleName(name);
		return rt;
	}
	
	/**
	 * 角色添加
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doaddrole")
	@ResponseBody
	public boolean doAddRole(@ModelAttribute Role role,HttpServletRequest request){
		boolean rt = this.role.doAddRole(role);
		
		//记录日志
		Log log=new Log("角色管理","增加",role.toString(),request);
		logService.addLog(log);
		
		return rt ;
	}
	

	
	/**
	 * 角色修改
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doupdaterole")
	@ResponseBody
	public boolean doUpdateRole(@ModelAttribute Role role,HttpServletRequest request){
		boolean rt = this.role.doUpdateRole(role);
		
		//记录日志
		Log log=new Log("角色管理","修改",role.toString(),request);
		logService.addLog(log);

		return rt ;
	}
	
	@RequestMapping("deleterole")
	@ResponseBody
	public boolean deleteRole(String id,HttpServletRequest request){
		boolean rt = this.role.deleteRole(id);
		
		//记录日志
		Log log=new Log("角色管理","删除","删除角色ID:"+id,request);
		logService.addLog(log);
		
		return rt;
	}
}
