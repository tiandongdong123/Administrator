package com.wf.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.utils.CookieUtil;

@Controller
@RequestMapping("person")
public class PersonController {

	@RequestMapping("index")
	public ModelAndView perManagers(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.addObject("PageList", "null");
		view.addObject("person", "null");
		view.addObject("roles1", "null");
		view.addObject("roles2", "null");
		view.addObject("roles3", "null");
		view.setViewName("/page/usermanager/per_manager");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("A21")!=-1){
			return view;
		}else{
			return null;
		}
	}

	/**
	 *	个人充值记录
	 */
	@RequestMapping("charge")
	public ModelAndView perAward(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/charge_order");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("A22")!=-1){
			return view;
		}else{
			return null;
		}
	}

	/**
	 *	 个人订单记录
	 */
	@RequestMapping("order")
	public ModelAndView order(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/pay_order");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("A23")!=-1){
			return view;
		}else{
			return null;
		}
	}

	/**
	 *	万方卡绑定记录
	 */
	@RequestMapping("wfcardbind")
	public ModelAndView wfcard_bind(HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/usermanager/wfcard_bind");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("A24")!=-1){
			return view;
		}else{
			return null;
		}
	}
}
