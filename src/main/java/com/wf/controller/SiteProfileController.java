package com.wf.controller;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wf.bean.PageList;
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

	/** 网站概况页面 */
	@RequestMapping("othermanager-siteProfile")
	public ModelAndView qikanaddMatrixLiterature() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/othermanager/site_profile");
		return view;
	}

	/** 昨天访问浏览量 天表 */
	@RequestMapping("findPageView")
	@ResponseBody
	public HashMap<String, Object> findPageView(Integer type, String dateType) {

		HashMap<String, Object> map = webSiteDailyService.findPageView(type,
				dateType);

		return map;
	}

	/** 昨天访问量 小时表 E-char图 */

	@RequestMapping("findPageViewHourly")
	@ResponseBody
	public Object findPageViewHourly(Integer type, String dateType) {

		return webSiteHourlyService.findPageViewHourly(type, dateType);
	}

	// 对比折线图 小时表 contrastPageViewHourly
	@RequestMapping("contrastPageViewHourly")
	@ResponseBody
	public Object contrastPageViewHourly(Integer type, String dateType,
			String contrastDate) {

		return webSiteHourlyService.contrastPageViewHourly(dateType,
				contrastDate, type);
	}

	// 查询统计列表总量
	@RequestMapping("selectSumNumbers")
	@ResponseBody
	public Object selectSumNumbers(String dateType) {

		return webSiteDailyService.selectSumNumbers(dateType);
	}

	// 基础指标查询 天表
	@RequestMapping("basicIndexNum")
	@ResponseBody
	public PageList basicIndexNum(String dateType, Integer pagenum,
			Integer pagesize) {

		return webSiteDailyService.basicIndexNum(dateType, pagenum, pagesize);
	}

	// 基础查询 时表
	@RequestMapping("basicIndexNumHourly")
	@ResponseBody
	public PageList basicIndexNumHourly(String dateType, Integer pagenum,
			Integer pagesize) {

		return webSiteHourlyService.basicIndexNumHourly(dateType, pagenum,
				pagesize);
	}

	// 根据时间查询客户属性
	@RequestMapping("findWebsiteAttribute")
	@ResponseBody
	public HashMap<String, Object> findWebsiteAttribute(String dateType) {

		return attributeService.findWebsiteAttribute(dateType);
	}

	// 对比时间折线图 contrastPageView
	@RequestMapping("contrastPageView")
	@ResponseBody
	public HashMap<String, Object> contrastPageView(String dateType,
			String contrastDate, Integer type) {

		return webSiteDailyService.contrastPageView(dateType, contrastDate,
				type);
	}
}