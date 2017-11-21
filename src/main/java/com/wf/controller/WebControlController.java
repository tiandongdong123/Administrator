package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Control;
import com.wf.bean.Log;
import com.wf.service.LogService;
import com.wf.service.WebControlService;

@RequestMapping("control")
@Controller
public class WebControlController {
	
	
	@Autowired
	private WebControlService control;
	
	@Autowired
	LogService logService;
	
	/**
	 * 节点监测查询
	 * @param request 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("getcontorl")
	@ResponseBody
	public List<Control> getControl(HttpServletRequest request) throws Exception{
		List<Control> li = this.control.getControl();
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("查询");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("网站监控管理");
		log.setOperation_content("");
		logService.addLog(log);

		return li;
	}
	/**
	 * 节点修改页面
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping("updatecontrol")
	public String updateControl(String id,Map<String,Object> map){
		Control c = this.control.getControlById(id);
		map.put("control", c);
		return "/page/systemmanager/updatecontrol";
	}
	
	/**
	 * 节点修改
	 * @param id
	 * @param ip
	 * @param request 
	 * @return
	 * @throws  Exception 
	 */
	@RequestMapping("doupdatecontorl")
	@ResponseBody
	public boolean doUpdateControl(String id,String ip, HttpServletRequest request) throws  Exception{
		boolean rt = this.control.doUpdateControl(id,ip);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("修改");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("网站监控管理");
		log.setOperation_content("修改后的站点信息:{id:"+id+",ip:"+ip+"}");
		logService.addLog(log);

		return rt;
	}
	/**
	 * 节点删除
	 * @param id
	 * @param request 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("dodeletecontrol")
	@ResponseBody
	public boolean doDeleteControl(String id, HttpServletRequest request) throws Exception{
		boolean rt = this.control.doDeleteControl(id);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("删除");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("网站监控管理");
		log.setOperation_content("删除的站点ID:"+id);
		logService.addLog(log);

		return rt ;
	}
	
	/**
	 * 节点添加页面
	 * @param pid
	 * @param map
	 * @return
	 */
	@RequestMapping("addcontrol")
	public String addcontrol(String pid,Map<String ,Object> map){
		map.put("pid", pid);
		return "/page/systemmanager/addcontrol";
	}
	
	/**
	 * 节点添加
	 * @param c
	 * @param request 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doaddcontrol")
	@ResponseBody
	public boolean doaddcontrol(@ModelAttribute Control c, HttpServletRequest request) throws Exception{
		boolean rt = this.control.doAddControl(c);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("增加");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("网站监控管理");
		log.setOperation_content("增加的站点信息"+c.toString());
		logService.addLog(log);

		return rt ;
	}

}
