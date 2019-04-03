package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.xhtml.object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;
import com.utils.DateTools;
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
		if(wfAdminId && password && realName && department && role && !isRepeat){
			rt = this.admin.doAddAdmin(admin);
			//记录日志
			Log log=new Log("管理员管理","增加","增加管理员信息:"+admin.toString(),request);
			logService.addLog(log);
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
	public String updateAdmin(Map<String,Object> map,String id,Integer pagenum){
		List<Object> rolename = this.admin.getRole();
		Wfadmin admin = this.admin.getAdminById(id);
		map.put("admin", admin);
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
	public JSONObject doUpdateAdmin(@ModelAttribute Wfadmin admin,HttpServletRequest request ){
		JSONObject map = new JSONObject();
		boolean password=admin.getPassword()!=null && StringUtils.isNotBlank(admin.getPassword());
		boolean realName=admin.getUser_realname()!=null && StringUtils.isNotBlank(admin.getUser_realname());
		boolean department=admin.getDepartment()!=null && StringUtils.isNotBlank(admin.getDepartment());
		boolean role=admin.getRole_id()!=null && StringUtils.isNotBlank(admin.getRole_id());
		if(password && realName && department && role){
			//判断是否自己修改自己角色
			Wfadmin wfAdmin = CookieUtil.getWfadmin(request);
			if(wfAdmin.getId().equals(admin.getId()) && (!wfAdmin.getRole_id().equals(admin.getRole_id())  
					|| !wfAdmin.getDepartment().equals(admin.getDepartment()) 
					|| !wfAdmin.getUser_realname().equals(admin.getUser_realname()))){
				map.put("flag", "fail");
				map.put("fail","管理员只能修改自己的密码");
					return map;
			}else{
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
		return map;
	}
	/**
	 * 管理员管理
	 * @return
	 */
	@RequestMapping("/administrator")
	public ModelAndView adminManager(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/admin_manager");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F1")!=-1){
			return mav;
		}else{
			return null;
		}
	}
	/**
	 * 角色管理
	 * @return
	 */
	@RequestMapping("/rolemanager")
	public ModelAndView roleManager(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/role_Manager");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F12")!=-1){
			return mav;
		}else{
			return null;
		}
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
	public ModelAndView dataManager(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/data_manager");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F21")!=-1){
			return mav;
		}else{
			return null;
		}
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
	public String doUpdateData(String id,Map<String, Object> map){
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
	public ModelAndView resourceManager(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/resource_price_manager");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F22")!=-1){
			return mav;
		}else{
			return null;
		}
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
	public String addPrice(Map<String,Object> map){
		Map<String,Object> resultmap = this.rps.getResource();
		map.put("rlmap", resultmap);
		return "/page/systemmanager/add_price";
	}
	/**
	 * 产品类型设置
	 * @return
	 */
	@RequestMapping("/productoption")
	public ModelAndView prductType(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/product_type");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F23")!=-1){
			return mav;
		}else{
			return null;
		}
	}


	/**
	 * 子系统设置
	 * @return
	 */
	@RequestMapping("/platoption")
	public ModelAndView sonSystem(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/son_system");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F24")!=-1){
			return mav;
		}else{
			return null;
		}
	}

	/**
	 * 网站监控
	 * @return
	 */
	@RequestMapping("/monitormanage")
	public ModelAndView controlManager(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/control");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F3")!=-1){
			return mav;
		}else{
			return null;
		}
	}

	/**
	 * 单位设置
	 * @return
	 */
	@RequestMapping("/unitoption")
	public ModelAndView unitSystem(String id,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.addObject("goback",id);
		mav.setViewName("/page/systemmanager/unit_set");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F25")!=-1){
			return mav;
		}else{
			return null;
		}
	}
	// 跳转到 系统配置
	@RequestMapping("systemconfig")
	public ModelAndView settingManager(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/systemmanager/setting_Manager");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F4")!=-1){
			return view;
		}else{
			return null;
		}
	}

	/**
	 * 日志
	 * 
	 */
	@RequestMapping("logmanage")
	public String getLog(Map<String, Object> model,HttpServletRequest request) {

		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		model.put("yesterday", yesterday);
		model.put("getAllModel", logService.getAllLogModel());
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("F5")!=-1){
			return "/page/systemmanager/log";
		}else{
			return null;
		}
		
	}
}
