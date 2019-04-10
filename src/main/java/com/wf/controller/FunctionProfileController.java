package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.service.FunctionProfileService;
import com.wf.service.LogService;

@Controller
@RequestMapping("functionProfile")
public class FunctionProfileController {

	@Autowired
	private FunctionProfileService function;

	@Autowired
	private LogService logService;

	@RequestMapping("functionProfile")
	public String functionProfile(){
			return "/page/othermanager/function_profile";
	}

	@RequestMapping("getline")
	@ResponseBody
	public Map<String,Object> getline(String title,String age,String exlevel,String datetype,String type,String starttime,String endtime,String domain,Integer property){
		return this.function.getline(title, age, exlevel, datetype, type, starttime, endtime, domain, property);
	}

	@RequestMapping("indexanalysis")
	@ResponseBody
	public Map<String,Object> indexanalysis(String title,String age,String exlevel,String datetype,String type,String starttime,String endtime,String domain,Integer property){
		Map<String,Object> map  = this.function.indexanalysis(title, age, exlevel, datetype, type, starttime, endtime, domain, property);
		return null;
	}

	@RequestMapping("gettable")
	@ResponseBody
	public PageList gettable(Integer pagesize,Integer pagenum,String title,String age,String exlevel,String datetype,Integer type,String starttime,String endtime,String domain,Integer property, HttpServletRequest request){

		PageList rs = this.function.gettable(pagesize,pagenum,title, age, exlevel, datetype, type, starttime, endtime, domain, property);

		//记录日志
		Log log=new Log("功能概况","查询","",request);
		logService.addLog(log);

		return rs;
	}



}
