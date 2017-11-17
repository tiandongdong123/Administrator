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
	public PageList getRole(Integer pagesize,Integer pagenum,HttpServletRequest request) throws Exception{
		PageList pl = this.role.getRole(pagenum, pagesize);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("查询");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("角色管理");
		log.setOperation_content("");
		logService.addLog(log);

		return pl;
	}
	/**
	 * 角色添加页面
	 * @param map
	 * @return
	 */
	@RequestMapping("addrole")
	public String addRole(Map<String,Object> map){
		List<Object> rt =this.role.getAllDept();
		map.put("rlmap", rt);
		return "/page/systemmanager/add_role";
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
	public boolean doAddRole(@ModelAttribute Role role,HttpServletRequest request) throws Exception{
		boolean rt = this.role.doAddRole(role);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("增加");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("角色管理");
		log.setOperation_content("增加角色信息:"+role.toString());
		logService.addLog(log);
		
		return rt ;
	}
	
	/**
	 * 修改角色页面
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping("updaterole")
	public String updateRole(String id,Map<String,Object> map){
		Role rl = this.role.getRoleById(id);
		List<Object> rt =this.role.getAllDept();
		map.put("rlmap", rt);
		map.put("role", rl);
		return "/page/systemmanager/update_role";
	}
	
	/**
	 * 角色修改
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doupdaterole")
	@ResponseBody
	public boolean doUpdateRole(@ModelAttribute Role role,HttpServletRequest request) throws Exception{
		boolean rt = this.role.doUpdateRole(role);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("修改");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("角色管理");
		log.setOperation_content("修改角色后信息:"+role.toString());
		logService.addLog(log);

		return rt ;
	}
	
	@RequestMapping("deleterole")
	@ResponseBody
	public boolean deleteRole(String id,HttpServletRequest request) throws Exception{
		boolean rt = this.role.deleteRole(id);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("删除");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("角色管理");
		log.setOperation_content("删除角色ID:"+id);
		logService.addLog(log);
		
		return rt;
	}
}
