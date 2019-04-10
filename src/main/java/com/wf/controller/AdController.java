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
	public ModelAndView search_adMessage_result() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_adMessage_result");
			return view;
	}
	/**
	 * 广告位管理
	 *
	 * @return
	 */
	@RequestMapping("/position")
	public ModelAndView search_adPosition_result() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_adPosition_result");
			return view;
	}
	/**
	 * 广告页面管理
	 *
	 * @return
	 */
	@RequestMapping("/page")
	public ModelAndView search_adPage_result() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_adPage_result");
			return view;
	}
}
