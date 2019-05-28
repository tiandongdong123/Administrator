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
import com.wf.service.LogService;
import com.wf.service.SourceAnalysisService;

@Controller
@RequestMapping("sourceAnalysis")
public class SourceAnalysisController {

	@Autowired
	private SourceAnalysisService sourceAnalysisService;

	@Autowired
	private LogService logService;

	/**
	 * @Title: databaseAnalysis
	 * @Description: TODO(来源分析页面) 
	 * @return String 返回类型 
	 * @author LiuYong 
	 * @date 12 Dis 2016 4:44:32 PM
	 */
	@RequestMapping("sourceAnalysis")
	public String sourceAnalysis(){
			return "/page/othermanager/sourceAnalysis";
	}

	/**
	 * @Title: getPage
	 * @Description: TODO(全部来源列表数据展示数据) 
	 * @param databaseAnalysis
	 * @param date
	 * @param startTime
	 * @param endTime
	 * @param pagenum
	 * @param pagesize
	 * @return List 返回类型 
	 * @author LiuYong 
	 * @param request 
	 * @throws Exception 
	 * @date 13 Dis 2016 4:26:24 PM
	 */
	@RequestMapping("getPageList")
	@ResponseBody
	public Map<String, Object> getPageList(String flag,String date, String startTime,String endTime,Integer pageNum,Integer pageSize, HttpServletRequest request) throws Exception{

		//列表展示		
		Map<String, Object> map=sourceAnalysisService.SourceAnalysisList(flag, date, startTime, endTime, pageNum, pageSize);

		//记录日志
		Log log=new Log("来源分析","查询","",request);
		logService.addLog(log);

		return map;
	}

	/**
	 * @Title: getChars
	 * @Description: TODO(全部来源图表展示数据) 
	 * @param databaseAnalysis
	 * @param type
	 * @param date
	 * @param startTime
	 * @param endTime
	 * @return Map<String,Object> 返回类型 
	 * @author LiuYong 
	 * @date 13 Dis 2016 5:06:00 PM
	 */
	@RequestMapping("getChart")
	@ResponseBody
	public Map<String,Object> getChart(String flag, String type, String date, String startTime,String endTime){

		//图表展示
		Map<String,Object> map=sourceAnalysisService.SourceAnalysisStatistics(flag,type, date, startTime, endTime);

		return map;
	}

}
