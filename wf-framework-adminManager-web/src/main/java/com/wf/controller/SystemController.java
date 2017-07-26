package com.wf.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	/**
	 * 管理员管理
	 * @return
	 */
	@RequestMapping("/adminManager")
	public ModelAndView adminManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/admin_manager");
		return mav;
	}
	/**
	 * 角色管理
	 * @return
	 */
	@RequestMapping("/roleManager")
	public ModelAndView roleManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/role_Manager");
		return mav;
	}	
	/**
	 * 部门管理
	 * @return
	 */
	@RequestMapping("/deptManager")
	public ModelAndView deptManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/dept_Manager");
		return mav;
	}	
	/**
	 * 数据库配置管理
	 * @return
	 */
	@RequestMapping("/dataManager")
	public ModelAndView dataManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/data_manager");
		return mav;
	}
	/**
	 * 资源单价配置管理
	 * @return
	 */
	@RequestMapping("/resourceManager")
	public ModelAndView resourceManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/resource_price_manager");
		return mav;
	}
	
	/**
	 * 产品类型设置
	 * @return
	 */
	@RequestMapping("/prductType")
	public ModelAndView prductType(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/product_type");
		return mav;
	}
	/**
	 * 子系统设置
	 * @return
	 */
	@RequestMapping("/sonSystem")
	public ModelAndView sonSystem(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/son_system");
		return mav;
	}
	/**
	 * 单位设置
	 * @return
	 */
	@RequestMapping("/unitSystem")
	public ModelAndView unitSystem(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/unit_set");
		return mav;
	}
	/**
	 * 添加定价
	 * @return
	 */
	@RequestMapping("/addPrice")
	public String addPrice(Map<String,Object> map){
		Map<String,Object> resultmap = this.rps.getResource();
		map.put("rlmap", resultmap);
		return "/page/systemmanager/add_price";
	}
	
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
	 * 修改定价
	 * @return
	 */
	@RequestMapping("/updateprice")
	public String updatePrice(String rid,Map<String,Object> map){
		 Map<String,Object> resultmap = this.rps.getRP(rid);
		map.put("rlmap", resultmap);
		return "/page/systemmanager/update_price";
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
	/**
	 * 网站监控
	 * @return
	 */
	@RequestMapping("/controlManager")
	public ModelAndView controlManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/systemmanager/control");
		return mav;
	}
}
