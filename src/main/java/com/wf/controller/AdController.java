package com.wf.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;

@Controller
@RequestMapping("/ad")
public class AdController {


	/**
	 * 发布广告管理
	 * 
	 * @return
	 */
	@RequestMapping("/manage")
	public ModelAndView search_adMessage_result(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_adMessage_result");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("C23")!=-1){
			return view;
		}else{
			return null;
		}
	}
	/**
	 * 广告位管理
	 *
	 * @return
	 */
	@RequestMapping("/position")
	public ModelAndView search_adPosition_result(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_adPosition_result");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("C22")!=-1){
			return view;
		}else{
			return null;
		}
	}
	/**
	 * 广告页面管理
	 *
	 * @return
	 */
	@RequestMapping("/page")
	public ModelAndView search_adPage_result(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_adPage_result");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("C21")!=-1){
			return view;
		}else{
			return null;
		}
	}

}
