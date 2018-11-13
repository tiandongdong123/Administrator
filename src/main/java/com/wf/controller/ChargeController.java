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
	/**
	 *	生成充值码
	 */
	@RequestMapping("chargecodecreate")
	public ModelAndView create_ChargeCode(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/create_chargeCode");
		return view;
	}
	/**
	 *	充值码批次查询
	 */
	@RequestMapping("chargecodebatchquery")
	public ModelAndView search_ChargeCode_Batch(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_chargeCode_Batch");
		return view;
	}
	/**
	 *	充值码信息查询
	 */
	@RequestMapping("chargecodequery")
	public ModelAndView search_ChargeCode(){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_chargeCode");
		return view;
	}

}
