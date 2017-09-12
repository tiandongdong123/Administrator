package com.wf.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.redis.RedisUtil;
import com.utils.CookieUtil;
import com.utils.GetUuid;
import com.wf.bean.Department;
import com.wf.bean.Remind;
import com.wf.bean.Role;
import com.wf.bean.SystemMenu;
import com.wf.bean.User;
import com.wf.bean.Wfadmin;
import com.wf.service.RemindService;
import com.wf.service.RoleService;
import com.wf.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	UserService service;
	@Autowired
	RoleService rs;
	@Autowired
	RemindService remindService;
	
	private RedisUtil redis = new RedisUtil();
	
	/**
	 *	查询权限接口
	 */
	@RequestMapping(value = "getadminpurview",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getAdminPurview(HttpServletRequest request) {
		Wfadmin admin = CookieUtil.getWfadmin(request);
		String wfAdminId = admin.getWangfang_admin_id();
		if (wfAdminId != null) {
			Map<String, String> map = service.getAdminPurview(wfAdminId);
			return JSONObject.fromObject(map);
		}
		return null;
	}
	
	
	/**
	 *	后台登录页面跳转 
	 */
	@RequestMapping("toLogin")
	public ModelAndView toLogin(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/login/login");
		return view;
	}
	
	
	/**
	 *	查询后台登录用户信息 
	 */
	@RequestMapping("userLogin")
	@ResponseBody
	public Map<String,String> login(String userName,String passWord,HttpServletRequest req,HttpServletResponse res) throws Exception{
		//清除cookie和redis
		this.removeCookieRedis(req,res);
		Map<String,String> map = new HashMap<String, String>();
		Wfadmin user = service.getuser(userName, passWord);
		if (user != null) {
			if (user.getStatus() == 0) {
				map.put("flag", "fail");
			} else {
				//组装数据
				Map<String, String> m = service.getAdminPurview(user.getWangfang_admin_id());
				User p = new User();
				p.setUser_id(user.getWangfang_admin_id());
				if(StringUtils.isNotBlank(m.get("user_realname"))){
					p.setUser_realname(m.get("user_realname"));
				}
				if(StringUtils.isNotBlank(m.get("mobile_phone"))){
					p.setMobile_phone(m.get("mobile_phone"));
				}
				if(StringUtils.isNotBlank(m.get("email"))){					
					p.setEmail(m.get("email"));
				}
				if(StringUtils.isNotBlank(m.get("purview"))){					
					p.setExtend(m.get("purview"));
				}
				p.setUserType(10);
				p.setLogin_mode(10);
				//存入Cookie
				String token=CookieUtil.getCookie(req);
				if(token==null){
					token = "admin."+GetUuid.getId();
					CookieUtil.addCookie(token,res);
				}
				user.setRegistration_time(null);
				CookieUtil.addWfCookie(user, res);
				//存入Redis
				JSONObject json = JSONObject.fromObject(p);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				json.put("registration_time", formatter.format(m.get("registration_time")));
				redis.hset(token, "Admin."+user.getWangfang_admin_id(), json.toString());
				redis.expire(token, 3600);
				map.put("flag", "true");
			}
		}else{
			map.put("flag", "false");
		}
		return map;
	}
	
	/**
	 *	注销登录
	 */
	@RequestMapping("logout")
	public ModelAndView logout(HttpServletRequest req,HttpServletResponse res){
		this.removeCookieRedis(req,res);
		return new ModelAndView("redirect:/user/toLogin.do");
	}
	
	/**
	 *	清除cookie、redis信息
	 */
	public void removeCookieRedis(HttpServletRequest req,HttpServletResponse res) {
		String token = CookieUtil.getCookie(req);
		if (token == null) {
			return;
		}
		Wfadmin admin = CookieUtil.getWfadmin(req);
		if (admin == null) {
			return;
		}
		// 去除redis
		redis.hdel(token, "Admin." + admin.getWangfang_admin_id());
		//去除wfcookie
		CookieUtil.removeWfadmin(req,res);
	}
	
	
	/**
	 *	后台登录主页面跳转 
	 */
	@RequestMapping("toIndex")
	public ModelAndView toIndex(HttpServletRequest req,HttpServletResponse res,HttpSession session) throws Exception{
		ModelAndView view = new ModelAndView();
		Wfadmin admin = CookieUtil.getWfadmin(req);
		Department dept=admin.getDept();
		String deptName=null;
		if(dept!=null){
			deptName=dept.getDeptName();
		}
		Role rl=rs.getRoleById(admin.getRole_id());
		String[] menuIds=rl.getPurview().split(",");
		List<String> menus=new ArrayList<String>();
		for(int i = 0; i < menuIds.length; i++){
			SystemMenu sm=service.findPurviewById(menuIds[i]);
			String purview=sm.getMenuName();
			if(purview!=null&&!"".equals(purview)){
				menus.add(purview);
			}
		}
		String purviews=StringUtils.join(menus, ",");
		String userId=admin.getWangfang_admin_id();
		session.setAttribute("purviews", purviews);
		session.setAttribute("userName", userId);
		session.setAttribute("department", deptName);
		//放入redis
		Map<String,String> map=new HashMap<String,String>();
		map.put("purviews", purviews);
		map.put("userName", userId);
		map.put("department", deptName);
		redis.set(CookieUtil.LAYOUT+userId,map.toString(), 12);
		redis.expire(CookieUtil.LAYOUT+userId, 3600, 12); //设置超时时间
		view.setViewName("/page/index");
		return view;
	}
	
	
	@RequestMapping("/getRemind")
	@ResponseBody
	public Map<String,Object> getRemind(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Remind> list = remindService.query();
		map.put("list", list);
		map.put("num", list.size());
		return map;
	}
	
	
	@RequestMapping("/updateRemind")
	@ResponseBody
	public boolean updateRemind(){
		boolean flag = remindService.update();
		return flag;
	}
}
