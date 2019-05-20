package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;











import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.KylinJDBC;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.dao.PageManagerMapper;
import com.wf.service.LogService;
import com.wf.service.PageAnalysisService;
import com.wf.service.PageManagerService;
import com.wf.service.impl.PageAnalysisServiceImpl;

@Controller
@RequestMapping("pageAnalysis") 
public class PageAnalysisController {

	//	@Autowired
	//	private AdminService admin;
	@Autowired
	private PageManagerService pageManagerService;
	@Autowired
	private PageAnalysisService pageAnalysisService;
	@Autowired
	private LogService logService;

	@RequestMapping("pageAnalysis")
	public String pageAnalysis(){
			return "/page/othermanager/page_analysis";

	}

	@RequestMapping("getline")
	@ResponseBody
	public Object list_data(HttpServletRequest request) {

		String age=request.getParameter("age");
		String title=request.getParameter("title");
		String exlevel=request.getParameter("exlevel");
		String reserchdomain=request.getParameter("reserchdomain");
		String pageName=request.getParameter("pageName");
		String datetype=request.getParameter("datetype");
		String type=request.getParameter("type");
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		Integer property=Integer.valueOf(request.getParameter("property"));
		return pageAnalysisService.foemat(age, title, exlevel, reserchdomain, pageName, datetype, type, starttime, endtime,property);
	}

	@RequestMapping("getdataSource")
	@ResponseBody
	public PageList datasource(Integer pagesize,Integer pagenum,String title,
			String age,String exlevel,String datetype,
			String starttime,String pageName,
			String endtime,String domain,Integer property, 
			HttpServletRequest request){

		PageList pageList= pageAnalysisService.getdatasource(pagesize,pagenum,title, age, exlevel, datetype, domain, pageName, starttime, endtime, property);


		//记录日志
		Log log=new Log("页面分析","查询","",request);
		logService.addLog(log);

		return pageList;
	}

	@RequestMapping("head_word")
	@ResponseBody
	public Object head_word(HttpServletRequest request) {
		String head_word=request.getParameter("head_word");

		String regEx="[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";  
		Pattern p = Pattern.compile(regEx);  
		Matcher m = p.matcher(head_word);	
		List<String> list=pageAnalysisService.getAllTopic(m.replaceAll(""));

		return list;
	}


	@RequestMapping("html_word")
	@ResponseBody
	public Object html_word(HttpServletRequest request) {
		String html_word=request.getParameter("html_word");
		String regEx="[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";  
		Pattern p = Pattern.compile(regEx);  
		Matcher m = p.matcher(html_word);  
		List<Object> list=pageManagerService.getKeyWord(m.replaceAll(""));
		return list;
	}
}
