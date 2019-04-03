package com.wf.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;
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
	public ModelAndView pay(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/pay_manager");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("B")!=-1){
			return view;
		}else{
			return null;
		}
	}
	/**
	 *	生成充值码
	 */
	@RequestMapping("chargecodecreate")
	public ModelAndView create_ChargeCode(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/create_chargeCode");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("D21")!=-1){
			return view;
		}else{
			return null;
		}
	}
	/**
	 *	充值码批次查询
	 */
	@RequestMapping("chargecodebatchquery")
	public ModelAndView search_ChargeCode_Batch(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_chargeCode_Batch");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("D22")!=-1){
			return view;
		}else{
			return null;
		}
	}
	/**
	 *	充值码信息查询
	 */
	@RequestMapping("chargecodequery")
	public ModelAndView search_ChargeCode(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/search_chargeCode");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("D23")!=-1){
			return view;
		}else{
			return null;
		}
	}

}
