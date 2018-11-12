package com.wf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 充值管理模块
 * @author wangguosheng
 *
 */
@Controller
@RequestMapping("charge")
public class ChargeController {

	/**
	 *	充值管理
	 */
	@RequestMapping("manage")
	public ModelAndView pay(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/pay_manager");
		return view;
	}
}
