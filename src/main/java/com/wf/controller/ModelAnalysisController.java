package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
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
import com.wf.service.LogService;
import com.wf.service.ModelAnalysisService;

@Controller
@RequestMapping("modelanalysis")
public class ModelAnalysisController {

	@Autowired
	private ModelAnalysisService model;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping("modelanalysis")
	public String getModel(Map<String,Object> model){
		//Map<String,Object> map = this.model.getline();
		List<String> namelist = this.model.getmodular();
		model.put("namelist", namelist);
		return "/page/othermanager/model_use_manager";
	}
	
	@RequestMapping("getline")
	@ResponseBody
	public Map<String,Object> getLine(String title,String age,String exlevel,String datetype,String model,Integer type,String starttime,String endtime,String domain,Integer property){
		Map<String,Object> map  = new HashMap<String, Object>();
		map = this.model.getline(title,age,exlevel,datetype,model,type,starttime,endtime,domain,property);
		return map;
	}
	
	
	@RequestMapping("gettable")
	@ResponseBody
	public PageList gettable(Integer pagesize,Integer pagenum,String title,String age,String exlevel,String datetype,String model,Integer type,String starttime,String endtime,String domain,Integer property, HttpServletRequest request) throws Exception{
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("查询");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("功能模块分析");
		log.setOperation_content("");
		logService.addLog(log);
		
		PageList pl = this.model.gettable(pagesize, pagenum, title, age, exlevel, datetype, model, starttime, endtime, type, domain,property);
		return pl;
	}
}
