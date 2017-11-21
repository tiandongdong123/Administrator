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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;






import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.KylinJDBC;
import com.wf.bean.Log;
import com.wf.dao.PageManagerMapper;
import com.wf.service.LogService;
import com.wf.service.PageAnalysisService;
import com.wf.service.impl.PageAnalysisServiceImpl;

@Controller
@RequestMapping("pageAnalysis")
public class PageAnalysisController {

//	@Autowired
//	private AdminService admin;
	@Autowired
	private PageManagerMapper pageManagerMapper;
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
//		PageAnalysisService pah=new PageAnalysisServiceImpl();
		return pageAnalysisService.foemat(age, title, exlevel, reserchdomain, pageName, datetype, type, starttime, endtime);
	}
	
	@RequestMapping("getdataSource")
	@ResponseBody
	public Object datasource(HttpServletRequest request) throws Exception {
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("查询");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("页面分析");
		log.setOperation_content("");
		logService.addLog(log);
		
		String age=request.getParameter("age");
		String title=request.getParameter("title");
		String exlevel=request.getParameter("exlevel");
		String reserchdomain=request.getParameter("reserchdomain");
		String pageName=request.getParameter("pageName");
		String datetype=request.getParameter("datetype");
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		String type="123456789";
//		PageAnalysisService pass=new  PageAnalysisServiceImpl();
		Object json= pageAnalysisService.getdatasource(title, age, exlevel, datetype, reserchdomain, type, pageName, starttime, endtime);
		return json;
	}
	
	@RequestMapping("getonedataSource")
	@ResponseBody
	public Object onedataSource(HttpServletRequest request) {
		String age=request.getParameter("age");
		String title=request.getParameter("title");
		String exlevel=request.getParameter("exlevel");
		String reserchdomain=request.getParameter("reserchdomain");
		String pageName=request.getParameter("pageName");
		String datetype=request.getParameter("datetype");
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		String type="12345678";
//		PageAnalysisService pass=new  PageAnalysisServiceImpl();
		Object json=pageAnalysisService.getonedatasource(title, age, exlevel, datetype, reserchdomain, type, pageName, starttime, endtime);	
		return json;
	}
	
	
	@RequestMapping("head_word")
	@ResponseBody
	public Object head_word(HttpServletRequest request) {
		String head_word=request.getParameter("head_word");
		
		String regEx="[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(head_word);        
		String sql="select reserch_domain from kylin_analysis where reserch_domain like '%"+m.replaceAll("")+"%' group by reserch_domain";
		KylinJDBC kdbc=new KylinJDBC();
		JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));
		List<String > word=new ArrayList<String>();
		for(int i=0;i<json.size();i++)
		{
					
			List<String> list = Arrays.asList(json.get(i).toString().split("%")) ;
			for(int j=0;j<list.size();j++)
			{
				word.add(list.get(j));
			}
		}
		
		for(int i=0;i<word.size();i++)
		{
			for(int j=word.size()-1;j>i;j--)
			{
				if(word.get(i).equals(word.get(j)))			
				{
					word.remove(j);
				}
			}
			
		}
		
		for(int i=word.size()-1;i>=0;i--)
		{
			if(!(word.get(i).split(m.replaceAll("")).length>1))				
			{
				word.remove(i);
			}
		
		}
		
		for(int i=word.size()-1;i>=0;i--)
		{
			if(i>9)
			{
				word.remove(i);
			}
		}
		return word;
	}
	

	@RequestMapping("html_word")
	@ResponseBody
	public Object html_word(HttpServletRequest request) {
		String html_word=request.getParameter("html_word");
		String regEx="[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(html_word);  
        List<Object> list=pageManagerMapper.getKeyword("%"+m.replaceAll("")+"%");
        JSONArray json=JSONArray.fromObject(list);
        List<String > word=new ArrayList<String>();
        for(int i=0;i<json.size();i++)
        {
        	word.add(json.getJSONObject(i).get("pageName").toString());
        }   
        for(int i=word.size()-1;i>=0;i--)
		{
			if(i>9)
			{
				word.remove(i);
			}
		}       
		/*String sql="select mokuai from kylin_analysis where mokuai like '%"+m.replaceAll("")+"%' group by mokuai";
		KylinJDBC kdbc=new KylinJDBC();
		JSONArray json =JSONArray.fromObject(kdbc.findToList(sql));
		List<String > word=new ArrayList<String>();
		for(int i=0;i<json.size();i++)
		{
					
			List<String> list = Arrays.asList(json.get(i).toString().split("%")) ;
						for(int j=0;j<list.size();j++)
						{
							word.add(list.get(j));
						}
		}
		
		for(int i=0;i<word.size();i++)
		{
			for(int j=word.size()-1;j>i;j--)
			{
				if(word.get(i).equals(word.get(j)))			
				{
					word.remove(j);
				}
			}
			
		}
		
		for(int i=word.size()-1;i>=0;i--)
		{
			if(!(word.get(i).split(m.replaceAll("")).length>1))				
			{
				word.remove(i);
			}
		
		}*/
		return word;
	}
}
