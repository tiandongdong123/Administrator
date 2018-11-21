package com.wf.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wf.bean.Datamanager;
import com.wf.bean.ProductType;
import com.wf.bean.SonSystem;
import com.wf.service.DataManagerService;
import com.wf.service.ProductTypeServise;
import com.wf.service.ResourcePriceService;
import com.wf.service.SonSystemService;

@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	private ResourcePriceService rps;
	
	@Autowired
	DataManagerService data;
	
	@Autowired
	ProductTypeServise product;
	
	@Autowired
	SonSystemService son;
	
	@RequestMapping("product_source")
	@ResponseBody
	public Object product_source(HttpServletRequest request) {
		
		String str=request.getParameter("data");
		SonSystem one=son.getOneSon(str);
		String str1=one.getProductResourceCode().toString();
		List<String> list=Arrays.asList(str1.split(","));
		List<Datamanager> datalist=new ArrayList<Datamanager>();
		for(int i=0;i<list.size();i++)
		{	
			Datamanager json=this.data.getDataManagerBySourceCode(list.get(i));
			datalist.add(json);
		}
		return datalist;
	}
	
	@RequestMapping("product_type")
	@ResponseBody
	public Object product_type(HttpServletRequest request) {
		String code=request.getParameter("data");
		List<ProductType> list=product.getByCode(code);
		return list;
	}

	/**
	 * 日志管理
	 * @return
	 */
	@RequestMapping("/logManager")
	public ModelAndView logManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/log_manager");
		return mav;
	}

}
