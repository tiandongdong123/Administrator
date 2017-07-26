package com.wf.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.ecs.storage.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exportExcel.ExportExcel;
import com.utils.JsonUtil;
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
	public String getLog() {
		return "/page/systemmanager/log";
	}

	/**
	 * 后台日志管理分页
	 * 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("getLogJson")
	public void getLogJson(HttpServletResponse response, String username,
			String ip, String behavior, String startTime, String endTime,
			Integer pageNum) throws Exception {
		
		Map<String,Object> map=new HashMap<String, Object>();
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar  calendar=new  GregorianCalendar(); 
		
		if(StringUtils.isEmpty(endTime)){
			endTime=null;
		}else{
			calendar.setTime(format.parse(endTime));
			calendar.add(calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}
		
		PageList p=logService.getLog(username,ip,behavior,startTime,endTime,pageNum);
		
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
	 * @throws Exception 
	 */
	@RequestMapping("exportLog")
	public void exportLog(HttpServletResponse response, String username,
			String ip, String behavior, String startTime, String endTime) throws Exception{
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar  calendar=new  GregorianCalendar(); 
		
		if(StringUtils.isEmpty(endTime)){
			endTime=null;
		}else{
			calendar.setTime(format.parse(endTime));
			calendar.add(calendar.DATE,1);
			endTime=format.format(calendar.getTime());
		}
		
		List<Object> list=logService.exportLog(username, ip, behavior, startTime, endTime);
		JSONArray array=JSONArray.fromObject(list);
		List<String> names=Arrays.asList(new String[]{"序号","操作用户","操作IP","操作时间","操作类型"});
		
		ExportExcel excel=new ExportExcel();
		excel.exportLog(response, array, names);
	}
	
	/**
	 * 根据日志ID删除日志
	 * @param response
	 * @param id 日志ID
	 */
	@RequestMapping("deleteLog")
	@ResponseBody
	public Integer deleteLog(HttpServletResponse response,@RequestParam(value="ids[]",required=false)Integer[]ids){
		return logService.deleteLogByID(ids);
	}

}
