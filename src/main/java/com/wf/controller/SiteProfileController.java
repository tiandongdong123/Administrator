package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.service.LogService;
import com.wf.service.WebSiteAttributeService;
import com.wf.service.WebSiteDailyService;
import com.wf.service.WebSiteHourlyService;

@Controller
@RequestMapping("siteProfile")
public class SiteProfileController {
	@Autowired
	private WebSiteDailyService webSiteDailyService;
	@Autowired
	private WebSiteHourlyService webSiteHourlyService;
	@Autowired
	private WebSiteAttributeService attributeService;

	@Autowired
	private LogService logService;
	/**
	 *  网站概况页面 
	 * @return
	 */
	@RequestMapping("othermanager-siteProfile")
	public ModelAndView qikanaddMatrixLiterature(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/othermanager/site_profile");
			return view;
	}

	/**
	 * 天表 E-char图 
	 * @param type
	 * @param dateType
	 * @return
	 */
	@RequestMapping("findPageView")
	@ResponseBody
	public HashMap<String, Object> findPageView(Integer type, String dateType) {

		HashMap<String, Object> map = webSiteDailyService.findPageView(type,
				dateType);

		return map;
	}

	/**
	 * 小时表 E-char图 
	 * @param type
	 * @param dateType
	 * @return
	 */
	@RequestMapping("findPageViewHourly")
	@ResponseBody
	public Object findPageViewHourly(Integer type, String dateType) {

		return webSiteHourlyService.findPageViewHourly(type, dateType);
	}

	/**
	 * 对比折线图 小时表 contrastPageViewHourly
	 * @param type
	 * @param dateType
	 * @param contrastDate
	 * @return
	 */
	@RequestMapping("contrastPageViewHourly")
	@ResponseBody
	public Object contrastPageViewHourly(Integer type, String dateType,
			String contrastDate) {

		return webSiteHourlyService.contrastPageViewHourly(dateType,
				contrastDate, type);
	}

	/**
	 * 对比时间折线图 天表 contrastPageView
	 * @param dateType
	 * @param contrastDate
	 * @param type
	 * @return
	 */
	@RequestMapping("contrastPageView")
	@ResponseBody
	public HashMap<String, Object> contrastPageView(String dateType,
			String contrastDate, Integer type) {

		return webSiteDailyService.contrastPageView(dateType, contrastDate,
				type);
	}

	/**
	 * 基础指标查询 天表
	 * @param dateType
	 * @param pagenum
	 * @param pagesize
	 * @param request 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("basicIndexNum")
	@ResponseBody
	public PageList basicIndexNum(String dateType, Integer pagenum,
			Integer pagesize, HttpServletRequest request) {

		//记录日志
		Log log=new Log("网站概况","查询","按天查询",request);
		logService.addLog(log);

		return webSiteDailyService.basicIndexNum(dateType, pagenum, pagesize);
	}

	/**
	 * 基础查询 时表
	 * @param dateType
	 * @param pagenum
	 * @param pagesize
	 * @param request 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("basicIndexNumHourly")
	@ResponseBody
	public PageList basicIndexNumHourly(String dateType, Integer pagenum,
			Integer pagesize, HttpServletRequest request) {

		//记录日志
		Log log=new Log("网站概况","查询","按小时查询",request);
		logService.addLog(log);

		return webSiteHourlyService.basicIndexNumHourly(dateType, pagenum,
				pagesize);
	}

	/**
	 * 查询统计列表总量--头部
	 * @param dateType
	 * @return
	 */
	@RequestMapping("selectSumNumbers")
	@ResponseBody
	public Object selectSumNumbers(String dateType) {

		return webSiteDailyService.selectSumNumbers(dateType);
	}

	/**
	 * 根据时间查询底部访客属性
	 * @param dateType
	 * @return
	 */
	@RequestMapping("findWebsiteAttribute")
	@ResponseBody
	public HashMap<String, Object> findWebsiteAttribute(String dateType) {

		return attributeService.findWebsiteAttribute(dateType);
	}

}