package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.MenuXml;
import com.wf.bean.Log;
import com.wf.bean.Menu;
import com.wf.bean.PageList;
import com.wf.bean.Role;
import com.wf.bean.Wfadmin;
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
	 * 查询角色
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
		List<Menu> liseMenu=MenuXml.getListMenu();
		JSONArray array = JSONArray.fromObject(liseMenu);
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
		boolean rt=false;
		boolean roleName=role.getRoleName()!=null && StringUtils.isNotBlank(role.getRoleName());
		boolean purview=role.getPurview()!=null && StringUtils.isNotBlank(role.getPurview());
		boolean roleRt = this.role.checkRoleName(role.getRoleName());
		if(roleName && purview &&!roleRt){
			rt = this.role.doAddRole(role);
		}
		
		
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
	public JSONObject doUpdateRole(@ModelAttribute Role role,HttpServletRequest request,HttpSession session,HttpServletResponse response){
		JSONObject map = new JSONObject();
		boolean rt=false;
		boolean roleName=role.getRoleName()!=null && StringUtils.isNotBlank(role.getRoleName());
		boolean purviewIsNull=role.getPurview()!=null && StringUtils.isNotBlank(role.getPurview());
		if(role.getRoleName().equals("admin")){
			map.put("flag", "fail");
			map.put("fail","超级管理员信息不可以被修改");
				return map;
		}
		if(roleName && purviewIsNull){
			rt = this.role.doUpdateRole(role);
			map.put("flag", rt);
		}
		//记录日志
		Log log=new Log("角色管理","修改",role.toString(),request);
		logService.addLog(log);

		if(rt){
			Wfadmin admin = CookieUtil.getWfadmin(request);
			Role rl=this.role.getRoleById(admin.getRole_id());
			String[] menuIds=rl.getPurview().split(",");
			List<String> menus=new ArrayList<String>();
			for (String menuId : menuIds) {
				menus.add(menuId);
			}
			String purviewsPlay=StringUtils.join(menus, "|");
			CookieUtil.addPrivilegeCookie(purviewsPlay, response);
			for(int i = 0; i < menuIds.length; i++){
				String purview=MenuXml.getMenuName().get(menuIds[i]);
				if(purview!=null&&!"".equals(purview)){
					menus.add(purview);
				}
			}
			String purviews=StringUtils.join(menus, "|");
			session.setAttribute("purviews", purviews);
		}
		return map ;
	}
	
	/**
	 * 删除角色
	 * @param id
	 * @param request
	 * @return
	 */
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
