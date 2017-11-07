package com.wf.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.exportExcel.ExportExcel;
import com.wf.bean.Modular;
import com.wf.bean.PageList;
import com.wf.service.ModularService;

@Controller
@RequestMapping("modular")
public class ModularManagerController {
	@Autowired
	ModularService modularService;
	
	
	/**
	 * 功能模块管理页面
	 * @return
	 */
	@RequestMapping("modularManager")
	public ModelAndView modularManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/othermanager/modular_manager");
		return mav;
	}
	
	
	
	
	/**
	 * 模块添加页面
	 * @return
	 */
	@RequestMapping("addModular")
	public String addModular(){

		return "/page/othermanager/add_modular";
	}
	
	@RequestMapping("doAddModular")
	@ResponseBody
	public boolean doAddModular(@ModelAttribute Modular md){
		boolean rt = modularService.doAddModular(md);
		return rt;
	}
	
	@RequestMapping("updateModular")
	public String updateModularManager(String id,Map<String,Object> map){
		Modular md = modularService.getModularById(id);
		map.put("modular", md);
		return "/page/othermanager/update_modular";
	}
	
	@RequestMapping("doUpdateModular")
	@ResponseBody
	public boolean doUpdateModular(@ModelAttribute Modular md){
		boolean bl = modularService.doUpdateModular(md);
		return bl;
	}
	
	@RequestMapping("deleteModular")
	@ResponseBody
	public boolean deleteModular(String id){
		boolean rt = modularService.deleteModular(id);
		return rt;
	}
	
	/**
	 * 获取数据库
	 * @param dataname
	 * @return
	 */
	@RequestMapping("getModular")
	@ResponseBody
	public PageList getModular(@RequestParam(value="ids[]",required =false) String[] ids,Integer pageNum,Integer pageSize){
		PageList pl = modularService.getModular(ids,pageNum,pageSize);
		return pl;
	}
	
	/**
	 * 导出功能模块
	 * @param response
	 * @param pageNum
	 * @param ids
	 */
	@RequestMapping("exportmodular")
	public void  exportmodular(HttpServletResponse response,String[] ids){
			List<Object> list=new ArrayList<Object>();
			
			if(ids.length==0) ids=null;
			
			list= modularService.exportmodular(ids);
			JSONArray array=JSONArray.fromObject(list);
					
			List<String> names=Arrays.asList(new String[]{"序号","功能模块","URL地址"});
			
			ExportExcel excel=new ExportExcel();
			excel.exportModular(response, array, names);
		
	}
	
}