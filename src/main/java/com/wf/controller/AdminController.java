package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.xhtml.object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wanfangdata.encrypt.PasswordHelper;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.bean.Role;
import com.wf.bean.Wfadmin;
import com.wf.service.AdminService;
import com.wf.service.DataManagerService;
import com.wf.service.LogService;
import com.wf.service.ResourcePriceService;
import com.wf.service.RoleService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("admin")
public class AdminController {
	@Autowired
	private ResourcePriceService rps;

	@Autowired
	private DataManagerService data;

	@Autowired
	private RoleService role;

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
			List<Object> list=pl.getPageRow();
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
	public JSONObject closeAdmin(@RequestParam(value="ids[]",required=false) String[] ids,HttpServletRequest request){
		JSONObject map = new JSONObject();
		Wfadmin wfAdmin = CookieUtil.getWfadmin(request);
		String id=this.admin.getAdminIdByName("admin");
		if(id!=null&&ids[0].equals(id)){
			map.put("flag", "fail");
			map.put("fail","超级管理员信息不能被修改");
				return map;
		}
		if(ids[0].equals(wfAdmin.getId())){
			map.put("flag", "fail");
			map.put("fail","管理员不能自己冻结自己的账号");
				return map;
		}
			boolean rt = this.admin.closeAdmin(ids);
			map.put("flag", rt);
			//记录日志
			Log log=new Log("管理员管理","冻结","冻结账号:"+(ids==null?"":Arrays.asList(ids)),request);
			logService.addLog(log);

			return map;
	}
	/**
	 * 解冻账号
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("openadmin")
	@ResponseBody
	public JSONObject openAdmin(@RequestParam(value="ids[]",required=false) String[] ids,HttpServletRequest request){
		JSONObject map = new JSONObject();
		Wfadmin wfAdmin = CookieUtil.getWfadmin(request);
		//查询超级管理员id
		String id=this.admin.getAdminIdByName("admin");
		if(id!=null&&ids[0].equals(id)){
			map.put("flag", "fail");
			map.put("fail","超级管理员信息不能被修改");
				return map;
		}
		if(ids[0].equals(wfAdmin.getId())){
			map.put("flag", "fail");
			map.put("fail","管理员不能自己冻结自己的账号");
				return map;
		}
			boolean rt = this.admin.openAdmin(ids);
			map.put("flag", rt);
			//记录日志
			Log log=new Log("管理员管理","冻结","解冻账号:"+(ids==null?"":Arrays.asList(ids)),request);
			logService.addLog(log);

			return map;
	}

	/**
	 * 添加管理员页面
	 * @param map
	 * @return
	 */
	@RequestMapping("addadmin")
	public String addAdmin(Map<String,Object> map,HttpServletRequest request){
		List<Object> rolename = this.admin.getRole();
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

		boolean rt=false;
		boolean wfAdminId=admin.getWangfang_admin_id()!=null&&StringUtils.isNotBlank(admin.getWangfang_admin_id());
		boolean password=admin.getPassword()!=null && StringUtils.isNotBlank(admin.getPassword());
		boolean realName=admin.getUser_realname()!=null && StringUtils.isNotBlank(admin.getUser_realname());
		boolean department=admin.getDepartment()!=null && StringUtils.isNotBlank(admin.getDepartment());
		boolean role=admin.getRole_id()!=null && StringUtils.isNotBlank(admin.getRole_id());
		boolean isRepeat = this.admin.checkAdminId(admin.getWangfang_admin_id());
		try {
			if(wfAdminId && password && realName && department && role && !isRepeat){
				admin.setPassword(PasswordHelper.encryptPassword(admin.getPassword()));
				rt = this.admin.doAddAdmin(admin);
				//记录日志
				Log log=new Log("管理员管理","增加","增加管理员信息:"+admin.toString(),request);
				logService.addLog(log);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rt;
	}
	/**
	 * 修改管理员页面
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping("updateadmin")
	public String updateAdmin(Map<String,Object> map,String id,Integer pagenum,HttpServletRequest request){
		try {
			List<Object> rolename = this.admin.getRole();
			Wfadmin admin = this.admin.getAdminById(id);
			admin.setPassword(PasswordHelper.decryptPassword(admin.getPassword()));
			map.put("admin", admin);
			map.put("rolename", rolename);
			map.put("pagenum",pagenum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/page/systemmanager/update_admin";
	}

	/**
	 * 修改管理员
	 * @param admin
	 * @return
	 */
	@RequestMapping("doupdateadmin")
	@ResponseBody
	public JSONObject doUpdateAdmin(@ModelAttribute Wfadmin admin,HttpServletRequest request ){
		JSONObject map = new JSONObject();
		try {
			boolean password=admin.getPassword()!=null && StringUtils.isNotBlank(admin.getPassword());
			boolean realName=admin.getUser_realname()!=null && StringUtils.isNotBlank(admin.getUser_realname());
			boolean department=admin.getDepartment()!=null && StringUtils.isNotBlank(admin.getDepartment());
			boolean role=admin.getRole_id()!=null && StringUtils.isNotBlank(admin.getRole_id());
			if(password && realName && department && role){
				Wfadmin adminStatus = this.admin.getAdminById(admin.getId());
				if(adminStatus.getWangfang_admin_id().equals("admin")){
					map.put("flag", "fail");
					map.put("fail","超级管理员账号不可被修改");
					return map;
				}
				if(adminStatus.getStatus()==0){
					map.put("flag", "fail");
					map.put("fail","冻结账户不可修改");
					return map;
				}
				//判断是否自己修改自己角色
				Wfadmin wfAdmin = CookieUtil.getWfadmin(request);
				if(wfAdmin.getId().equals(admin.getId()) && (!wfAdmin.getRole_id().equals(admin.getRole_id())  
						|| !wfAdmin.getDepartment().equals(admin.getDepartment()) 
						|| !wfAdmin.getUser_realname().equals(admin.getUser_realname()))){
					map.put("flag", "fail");
					map.put("fail","管理员自己只能修改自己的密码");
					return map;
				}else{
					admin.setPassword(PasswordHelper.encryptPassword(admin.getPassword()));
					boolean rt = this.admin.doUpdateAdmin(admin);
					map.put("flag", rt);
				}
				//记录日志
				Log log=new Log("管理员管理","修改","修改后管理员信息:"+admin.toString(),request);
				logService.addLog(log);
			}
			if(map.size()==0){
				map.put("flag", false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	/*
	 * 更改密码 页面
	 */
	@RequestMapping("updateaPassword")
	public String updateaPassword(){
		return "/page/systemmanager/update_password";
	}
	/**
	 * 更改密码
	 * @return
	 */
	@RequestMapping(value="doUpdatePassword",method = RequestMethod.POST)
	@ResponseBody
	public boolean doUpdatePassword(String oldPassword,String newPassword,String repeatPassword,HttpServletRequest request){
		boolean rt=false;
		try {
			if(StringUtils.isNoneBlank(oldPassword)&&StringUtils.isNoneBlank(newPassword)&&StringUtils.isNoneBlank(repeatPassword)
				&&passwordCheck(oldPassword)&&passwordCheck(newPassword)&&passwordCheck(repeatPassword)){
				//校验密码
				oldPassword=PasswordHelper.encryptPassword(oldPassword);
				newPassword=PasswordHelper.encryptPassword(newPassword);
				repeatPassword=PasswordHelper.encryptPassword(repeatPassword);
				Wfadmin wfAdmin = admin.getAdminById(CookieUtil.getWfadmin(request).getId());
				if(!oldPassword.equals(wfAdmin.getPassword())||!newPassword.equals(repeatPassword)){
					return false;
				}
				wfAdmin.setPassword(newPassword);
				rt = this.admin.doUpdateAdmin(wfAdmin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}
	/*
	 * 验证密码合法性
	 */
	private boolean passwordCheck(String password){
		boolean length=password.length()>=6&&password.length()<=16;
		boolean space=!password.contains(" ");
        boolean chinese=true;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(password);
        if(m.find()){
        	chinese=false;
        }
		return length&&space&&chinese;
	}
	
	
	
	/**
	 * 检查密码
	 * @return
	 */
	@RequestMapping("doUpdatePasswordcheck")
	@ResponseBody
	public boolean doUpdatePasswordcheck(String oldPassword,HttpServletRequest request){
		try {
			if(StringUtils.isNoneBlank(oldPassword)){
				//校验密码
				oldPassword=PasswordHelper.encryptPassword(oldPassword);
				Wfadmin wfAdmin=admin.getAdminById(CookieUtil.getWfadmin(request).getId());
				if(oldPassword.equals(wfAdmin.getPassword())){
					return true; 
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 管理员管理
	 * @return
	 */
	@RequestMapping("/administrator")
	public ModelAndView adminManager(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/admin_manager");
			return mav;
	}
	/**
	 * 角色管理
	 * @return
	 */
	@RequestMapping("/rolemanager")
	public ModelAndView roleManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/role_Manager");
			return mav;
	}	
	/**
	 * 角色添加页面
	 * @param map
	 * @return
	 */
	@RequestMapping("roleadd")
	public String addRole(){
		return "/page/systemmanager/add_role";
	}
	/**
	 * 修改角色页面
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping("rolemodify")
	public String updateRole(String id,Map<String,Object> map){
		Role rl = this.role.getRoleById(id);
		map.put("role", rl);
		return "/page/systemmanager/update_role";
	}
	/**
	 * 部门管理
	 * @return
	 */
	@RequestMapping("/department")
	public ModelAndView deptManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/dept_Manager");
		return mav;
	}	
	/**
	 * 数据库配置管理
	 * @return
	 */
	@RequestMapping("/dbconfig")
	public ModelAndView dataManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/data_manager");
			return mav;
	}


	/**
	 * 添加数据页
	 * @return
	 */
	@RequestMapping("dbadd")
	public String addData(Map<String, Object> map){
		Map<String,Object> mm = this.data.getResource();
		map.put("rlmap", mm);
			return "/page/systemmanager/add_data";
	}
	/**
	 * 数据库修改页面
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping("dbmodify")
	public String doUpdateData(String id,Map<String, Object> map,HttpServletRequest request){
		Map<String,Object> mm = this.data.getResource();
		Map<String,Object> check = this.data.getCheck(id);
		mm.putAll(check);
		map.put("rlmap", mm);
			return "/page/systemmanager/update_data";
	}

	/**
	 * 资源单价配置管理
	 * @return
	 */
	@RequestMapping("/pricemanage")
	public ModelAndView resourceManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/resource_price_manager");
			return mav;
	}

	/**
	 * 修改定价
	 * @return
	 */
	@RequestMapping("/pricemodify")
	public String updatePrice(String rid,Map<String,Object> map){
		Map<String,Object> resultmap = this.rps.getRP(rid);
		map.put("rlmap", resultmap);
			return "/page/systemmanager/update_price";
	}

	/**
	 * 添加定价
	 * @return
	 */
	@RequestMapping("/priceadd")
	public String addPrice(Map<String,Object> map,HttpServletRequest request){
		Map<String,Object> resultmap = this.rps.getResource();
		map.put("rlmap", resultmap);
			return "/page/systemmanager/add_price";
	}
	/**
	 * 产品类型设置
	 * @return
	 */
	@RequestMapping("/productoption")
	public ModelAndView prductType(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/product_type");
			return mav;
	}


	/**
	 * 子系统设置
	 * @return
	 */
	@RequestMapping("/platoption")
	public ModelAndView sonSystem(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/son_system");
			return mav;
	}

	/**
	 * 网站监控
	 * @return
	 */
	@RequestMapping("/monitormanage")
	public ModelAndView controlManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/control");
			return mav;
	}

	/**
	 * 单位设置
	 * @return
	 */
	@RequestMapping("/unitoption")
	public ModelAndView unitSystem(String id){
		ModelAndView mav = new ModelAndView();
		mav.addObject("goback",id);
		mav.setViewName("/page/systemmanager/unit_set");
			return mav;
	}
	// 跳转到 系统配置
	@RequestMapping("systemconfig")
	public ModelAndView settingManager() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/systemmanager/setting_Manager");
			return view;
	}

	/**
	 * 日志
	 * 
	 */
	@RequestMapping("logmanage")
	public String getLog(Map<String, Object> model) {

		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		model.put("yesterday", yesterday);
		model.put("getAllModel", logService.getAllLogModel());
			return "/page/systemmanager/log";
	}
}
