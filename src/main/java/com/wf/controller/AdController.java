package com.wf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("ad")
public class AdController {

	
	/**
	 * 广告管理
	 * 
	 * @return
	 */
	@RequestMapping("manage")
	public ModelAndView search_adMessage_result() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_adMessage_result");
		return view;
	}
	
}
