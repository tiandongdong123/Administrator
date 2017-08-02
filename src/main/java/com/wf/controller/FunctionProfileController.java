package com.wf.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.bean.PageList;
import com.wf.service.FunctionProfileService;

@Controller
@RequestMapping("functionProfile")
public class FunctionProfileController {

	@Autowired
	private FunctionProfileService function;
	
	@RequestMapping("functionProfile")
	public String functionProfile(){
		
		return "/page/othermanager/function_profile";
	}
	
	@RequestMapping("getline")
	@ResponseBody
	public Map<String,Object> getline(String title,String age,String exlevel,String datetype,Integer type,String starttime,String endtime,String domain,Integer property){
		Map<String,Object> map  = this.function.getline(title, age, exlevel, datetype, type, starttime, endtime, domain, property);
		return map;
	}
	
	@RequestMapping("indexanalysis")
	@ResponseBody
	public Map<String,Object> indexanalysis(String title,String age,String exlevel,String datetype,String type,String starttime,String endtime,String domain,Integer property){
		Map<String,Object> map  = this.function.indexanalysis(title, age, exlevel, datetype, type, starttime, endtime, domain, property);
		return map;
	}
	
	@RequestMapping("gettable")
	@ResponseBody
	public PageList gettable(Integer pagesize,Integer pagenum,String title,String age,String exlevel,String datetype,Integer type,String starttime,String endtime,String domain,Integer property){
		PageList rs = this.function.gettable(pagesize,pagenum,title, age, exlevel, datetype, type, starttime, endtime, domain, property);
		return rs;
	}
	
	
	
}
