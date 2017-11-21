package com.wf.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.storage.Hash;
import org.apache.ecs.xhtml.map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exportExcel.ExportExcel;
import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.JsonUtil;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.service.LogService;

@Controller
@RequestMapping("log")
public class LogController {

	@Autowired
	private LogService logService;

	/**
	 * 日志
	 * 
	 */
	@RequestMapping("getLog")
	public String getLog(Map<String, Object> model) {
		model.put("getAllModel", logService.getAllLogModel());
		return "/page/systemmanager/log";
	}

	/**
	 * 后台日志管理分页
	 * @param request 
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("getLogJson")
	public void getLogJson(HttpServletResponse response,String username,
			String ip, String module,String behavior, String startTime, String endTime,
			Integer pageNum, HttpServletRequest request) throws Exception {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar  calendar=new  GregorianCalendar(); 
		
		if(StringUtils.isEmpty(endTime)){
			endTime=null;
		}else{
			calendar.setTime(format.parse(endTime));
			calendar.add(calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}
		  
		 
		PageList p=logService.getLog(username,ip,module,behavior,startTime,endTime, pageNum);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("查询");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("后台日志管理");
		log.setOperation_content("查询条件:操作用户："+username+",操作IP:"+ip+","
				+ "操作模块:"+behavior+",操作类型:"+behavior+",操作时间:"+startTime+"-"+"endTime");
		logService.addLog(log);
		
		response.setCharacterEncoding("UTF-8");
		JSONObject json=JSONObject.fromObject(p);
		response.getWriter().write(json.toString());
	}
	
	/**
	 * 导出日志管理
	 * @param response
	 * @param username
	 * @param ip
	 * @param behavior
	 * @param startTime
	 * @param endTime
	 * @param request 
	 * @throws Exception 
	 */
	@RequestMapping("exportLog")
	public void exportLog(HttpServletResponse response, String username,
			String ip,String module,String behavior, String startTime, String endTime, HttpServletRequest request) throws Exception{
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("导出");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("后台日志管理");
		log.setOperation_content("导出条件:操作用户："+username+",操作IP:"+ip+","
				+ "操作模块:"+module+",操作类型:"+behavior+",操作时间:"+startTime+"-"+"endTime");
		logService.addLog(log);
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar  calendar=new  GregorianCalendar(); 
		
		if(StringUtils.isEmpty(endTime)){
			endTime=null;
		}else{
			calendar.setTime(format.parse(endTime));
			calendar.add(calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}
		
		List<Object> list=logService.exportLog(username, ip, module,behavior, startTime, endTime);
		JSONArray array=JSONArray.fromObject(list);
		List<String> names=Arrays.asList(new String[]{"序号","操作用户","操作IP","操作时间","操作类型"});
		
		ExportExcel excel=new ExportExcel();
		excel.exportLog(response, array, names);
	}
	
	/**
	 * 根据日志ID删除日志
	 * @param response
	 * @param request 
	 * @param id 日志ID
	 * @throws UnknownHostException 
	 */
	@RequestMapping("deleteLog")
	@ResponseBody
	public Integer deleteLog(HttpServletResponse response,@RequestParam(value="ids[]",required=false)Integer[]ids,
			HttpServletRequest request) throws Exception{
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("删除");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("后台日志管理");
		log.setOperation_content("删除日志ID:"+(ids==null?"":Arrays.asList(ids)));
		logService.addLog(log);

		return logService.deleteLogByID(ids);
	}
	
	/**
	 * 根据模块名称获取对应的模块操作类型
	 * @param model 模块名称
	 * @return 模块操作类型
	 */
	@RequestMapping("getResTypeByModel")
	@ResponseBody
	public List<String> getResTypeByModel(@RequestParam(value="modelname",required=false)String modelname){
		return logService.getResTypeByModel(modelname);
		
	}

}
