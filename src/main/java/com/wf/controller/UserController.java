package com.wf.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
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
import com.utils.GetUuid;
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
	
	/**
	 *	查询权限接口
	 */
	@RequestMapping(value = "getadminpurview",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getAdminPurview(HttpSession session){
		Wfadmin wfadmin = ((Wfadmin)session.getAttribute("wfAdmin"));
		if(wfadmin!=null){
			String wfAdminId = wfadmin.getWangfang_admin_id();			
			Map<String,String> map = service.getAdminPurview(wfAdminId);
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
	public Map<String,String> login(String userName,String passWord,HttpSession session,HttpServletResponse response,
			HttpServletRequest request) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		//清除Session、Rides信息
		this.hdel(session, request);
		String token = null;
		RedisUtil uls = new RedisUtil();
		Wfadmin user = service.getuser(userName,passWord);
		if(user!=null){
			if(user.getStatus()==0){
				map.put("flag", "fail");
			}else{
				//封装Session数据
				session.setAttribute("wfAdmin", user);
				session.setMaxInactiveInterval(3600);//session会话维持时间1小时
				if(session!=null){				
					map.put("flag", "true");
				}
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
				//如果cookie有值则获取key覆盖Redis,否则存入新的值
				boolean b = false;
				JSONObject json = null;
				Cookie[] cookies = request.getCookies();
				if(cookies!=null && cookies.length>0){
					for(Cookie ck : cookies){
						if(ck.getName().equals("CASTGC")){
							if(StringUtils.isNotBlank(ck.getValue())){								
								token = ck.getValue();
								b = true;
								break;
							}
						}
					}
				}
				if(b == false){					
					token = "admin."+GetUuid.getId();
					//封装Cookie
					Cookie cookie = new Cookie("CASTGC", token);
					cookie.setMaxAge(3600);
					cookie.setDomain("wanfangdata.com.cn");
					cookie.setPath("/");
					response.addCookie(cookie);
				}
				//封装数据存入Redis
				json = JSONObject.fromObject(p);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				json.put("registration_time", formatter.format(m.get("registration_time")));
				uls.hset(token, "Admin."+user.getWangfang_admin_id(), json.toString());
				uls.expire(token, 3600);
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
	public ModelAndView logout(HttpSession session,HttpServletRequest request){
		this.hdel(session, request);
		return new ModelAndView("redirect:/user/toLogin.do");
	}
	
	/**
	 *	清除Session、Rides信息
	 */
	public void hdel(HttpSession session,HttpServletRequest request){
		RedisUtil uls = new RedisUtil();
		Wfadmin wfadmin = (Wfadmin)session.getAttribute("wfAdmin");
		Cookie[] cookies = request.getCookies();
		if(cookies!=null && wfadmin!=null){
			for(Cookie ck : cookies){
				if(ck.getName().equals("CASTGC")){
					uls.hdel(ck.getValue(), "Admin."+((Wfadmin)session.getAttribute("wfAdmin")).getWangfang_admin_id());
					break;
				}
			}
		}
		session.removeAttribute("wfAdmin");
	}
	
	
	/**
	 *	后台登录主页面跳转 
	 */
	@RequestMapping("toIndex")
	public ModelAndView toIndex(HttpSession session){
		ModelAndView view = new ModelAndView();
		
		/**获取该角色能够访问的菜单权限、及各菜单地址组成字符串并放入session*/
		Object role=session.getAttribute("wfAdmin");
		JSONObject obj=JSONObject.fromObject(role);
		String roleId=obj.get("role_id").toString();
		String userName=obj.getString("wangfang_admin_id").toString();
		JSONObject dept=JSONObject.fromObject(obj.getString("dept"));
		String department=dept.get("deptName").toString();
		Role rl=rs.getRoleById(roleId);
		String[] menuIds=rl.getPurview().split(",");
		List<String> menus=new ArrayList<String>();
		List<String> urls=new ArrayList<String>();
		for(int i = 0; i < menuIds.length; i++){
			SystemMenu sm=service.findPurviewById(menuIds[i]);
			String purview=sm.getMenuName();
			if(purview!=null&&!"".equals(purview)){
				menus.add(purview);
			}
			
			String url=sm.getUrl();
			if(url!=null&&!"".equals(url)){
				urls.add(url);
			}
		}
		String purviews=StringUtils.join(menus, ",");
		session.setAttribute("purviews", purviews);
		String urlss=StringUtils.join(urls, ",");
		session.setAttribute("urls", urlss);
		session.setAttribute("userName", userName);
		session.setAttribute("department", department);
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
