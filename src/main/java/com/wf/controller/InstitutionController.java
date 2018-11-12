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
