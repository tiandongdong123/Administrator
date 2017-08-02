package com.wf.controller;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.bean.PageList;
import com.wf.bean.Role;
import com.wf.service.RoleService;

@Controller
@RequestMapping("role")
public class RoleController {

	@Autowired
	private RoleService role;
	/**
	 * 查询管理员
	 * @return
	 */
	@RequestMapping("getrole")
	@ResponseBody
	public PageList getRole(Integer pagesize,Integer pagenum){
		PageList pl = this.role.getRole(pagenum, pagesize);
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
	 */
	@RequestMapping("doaddrole")
	@ResponseBody
	public boolean doAddRole(@ModelAttribute Role role){
		boolean rt = this.role.doAddRole(role);
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
	 */
	@RequestMapping("doupdaterole")
	@ResponseBody
	public boolean doUpdateRole(@ModelAttribute Role role){
		boolean rt = this.role.doUpdateRole(role);
		return rt ;
	}
	
	@RequestMapping("deleterole")
	@ResponseBody
	public boolean deleteRole(String id){
		boolean rt = this.role.deleteRole(id);
		return rt;
	}
}
