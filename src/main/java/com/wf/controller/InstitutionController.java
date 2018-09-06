package com.wf.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wf.service.InstitutionService;

/**
 *	@author: oygy
 * 	@Date: 2018-09-05
 *	@模块描述: 机构用户信息
 */

@Controller
@RequestMapping("institution")
public class InstitutionController {

	@Autowired
	private InstitutionService institutionService;
	
	/**
	 *	跳转到机构数据solr发布界面
	 */
	@RequestMapping("toSolrData")
	public ModelAndView toSolrData(ModelAndView view){
		view.setViewName("/page/usermanager/toSolrData");
		return view;
	}
	
	/**
	 * 发布数据
	 * @param userId
	 * @return
	 */
	@RequestMapping("setSolrData")
	@ResponseBody
	public Map<String,String> setSolrData(String userId){
		return institutionService.setSolrData();
	}
}
