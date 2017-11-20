package com.wf.controller;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Log;
import com.wf.bean.Modular;
import com.wf.bean.PageList;
import com.wf.bean.PageManager;
import com.wf.service.LogService;
import com.wf.service.ModularService;
import com.wf.service.PageManagerService;

@Controller
@RequestMapping("page")
public class PageManagerController {
	@Autowired
	PageManagerService pageManagerService;
	@Autowired
	ModularService modularService;
	
	LogService logService;
	/**
	 * 分析页面管理页面
	 * @return
	 */
	@RequestMapping("pageManager")
	public ModelAndView pageManager(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/page/othermanager/page_manager");
		return mav;
	}
	
	/**
	* @Title: getPage
	* @Description: TODO(获取页面分析列表数据) 
	* @param ids
	* @param pageName
	* @param pageNum
	* @param pageSize
	* @return PageList    
	* @author LiuYong 
	 * @param request 
	* @throws UnsupportedEncodingException 
	 * @throws Exception 
	* @date 29 Nov 2016 9:42:04 AM
	 */
	@RequestMapping("getpage")
	@ResponseBody
	public PageList getPage(@RequestParam(value="ids[]",required =false) String[] ids,String pageName,Integer pageNum,Integer pageSize, HttpServletRequest request) throws Exception{
		pageName=java.net.URLDecoder.decode(pageName,"UTF-8");
		PageList pl = pageManagerService.getPage(ids,pageName,pageNum,pageSize);
		for(int i=0;i<pl.getPageRow().size();i++){
			Modular mode=modularService.getModularById(JSONArray.fromObject(pl.getPageRow().get(i)).getJSONObject(0).get("modularId").toString());
			if(null!=mode){
				((PageManager)pl.getPageRow().get(i)).setModularId(mode.getModularName());
			}
		}
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("查询");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("分析页面管理");
		
		log.setOperation_content("查询条件:页面名称:"+pageName+",功能模块:"+(ids==null?"":Arrays.asList(ids)));
		logService.addLog(log);

		return pl;
	}
	
	/**
	* @Title: addModular
	* @Description: TODO(模块分析添加页面)
	* @return String    返回类型
	* @author LiuYong 
	* @date 29 Nov 2016 8:56:48 AM
	 */
	@RequestMapping("addModular")
	public String addModular(){

		return "/page/othermanager/add_modular";
	}
	
	/**
	* @Title: doAddModular
	* @Description: TODO(模块分析添加操作)
	* @return boolean    返回类型
	* @author LiuYong 
	 * @param request 
	 * @throws Exception 
	* @date 29 Nov 2016 8:57:47 AM
	 */
	@RequestMapping("doAddModular")
	@ResponseBody
	public boolean doAddModular(@ModelAttribute Modular md, HttpServletRequest request) throws Exception{
		boolean rt = modularService.doAddModular(md);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("增加");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("功能模块管理");
		
		log.setOperation_content("添加功能模块信息:"+md.toString());
		logService.addLog(log);
		
		return rt;
	}
	
	/**
	* @Title: addPageManager
	* @Description: TODO(页面分析添加页面)
	* @return String    返回类型
	* @author LiuYong 
	* @date 29 Nov 2016 8:58:44 AM
	 */
	@RequestMapping("addPageManager")
	public String addPageManager(Map<String,Object> map){	
		List<Object> modularList = modularService.getModularList();
		map.put("modularList", modularList);
		return "/page/othermanager/add_page_manager";
	}
	
	/**
	* @Title: doAddPageManager
	* @Description: TODO(页面分析添加操作)
	* @return boolean    返回类型
	* @author LiuYong 
	 * @param request 
	 * @throws UnknownHostException 
	* @date 29 Nov 2016 8:59:35 AM
	 */
	@RequestMapping("doAddPageManager")
	@ResponseBody
	public boolean doAddPageManager(@ModelAttribute PageManager pm, HttpServletRequest request) throws Exception{
		boolean rt = pageManagerService.doAddPageManager(pm);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("增加");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("分析页面管理");
		
		log.setOperation_content("添加分析页面信息:"+pm.toString());
		logService.addLog(log);
		
		return rt;
	}
	
	/**
	* @Title: updatePageManager
	* @Description: TODO(页面分析修改页面)
	* @param id
	* @param map
	* @return String    返回类型
	* @author LiuYong 
	* @date 29 Nov 2016 9:06:24 AM
	 */
	@RequestMapping("updatePageManager")
	public String updatePageManager(String id,Map<String,Object> map){
		PageManager pm = pageManagerService.getPageManagerById(id);
		List<Object> modularList = modularService.getModularList();
		map.put("modularList", modularList);
		map.put("pageManager", pm);
		return "/page/othermanager/update_page_manager";
	}
	
	/**
	* @Title: doUpdatePageManager
	* @Description: TODO(页面分析修改操作) 
	* @param pm
	 * @param request 
	* @return boolean
	 * @throws UnknownHostException 
	* @throws    
	* @author LiuYong 
	* @date 29 Nov 2016 9:21:52 AM
	 */
	@RequestMapping("doUpdatePageManager")
	@ResponseBody
	public boolean doUpdatePageManager(@ModelAttribute PageManager pm, HttpServletRequest request) throws Exception{
		boolean bl = pageManagerService.doUpdatePageManager(pm);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("修改");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("分析页面管理");
		
		log.setOperation_content("修改后分析页面信息:"+pm.toString());
		logService.addLog(log);

		return bl;
	}
	
	/**
	* @Title: deletePageManager
	* @Description: TODO(分析页面删除操作) 
	* @param id
	* @return  boolean  
	* @author LiuYong 
	 * @param request 
	 * @throws Exception 
	* @date 29 Nov 2016 9:23:26 AM
	 */
	@RequestMapping("deletePageManager")
	@ResponseBody
	public boolean deletePageManager(String id, HttpServletRequest request) throws Exception{
		boolean rt = pageManagerService.deletePageManager(id);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("删除");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("分析页面管理");
		
		log.setOperation_content("删除分析页面ID:"+id);
		logService.addLog(log);

		return rt;
	}
	
	/**
	* @Title: getModularType
	* @Description: TODO(获取模块集合) 
	* @return    
	* @author LiuYong 
	* @date 29 Nov 2016 9:24:48 AM
	 */
	@RequestMapping("getModularType")
	@ResponseBody
	public List<Object> getModularType(){	
		List<Object> modularList = modularService.getModularList();
		return modularList;
	}
	
	
	/**
	 * 页面分析  -----导出
	 * @param response
	 * @param pageNum
	 * @param ids
	 * @param request 
	 * @throws Exception 
	 */
	@RequestMapping("exportpage")
	public void exportpage(HttpServletResponse response,String pageName,String ids[], HttpServletRequest request) throws Exception{
		if(ids.length==0) ids=null;
		pageName=java.net.URLDecoder.decode(pageName,"UTF-8");
		
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("导出");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("分析页面管理");
		
		log.setOperation_content("导出条件:页面名称:"+pageName+",功能模块:"+ids.toString());
		logService.addLog(log);

		PageList pl = pageManagerService.exportpage(ids,pageName);
		for(int i=0;i<pl.getPageRow().size();i++){
			Modular mode=modularService.getModularById(JSONArray.fromObject(pl.getPageRow().get(i)).getJSONObject(0).get("modularId").toString());
			((PageManager)pl.getPageRow().get(i)).setModularId(mode.getModularName());
		}
		
		JSONArray array=JSONArray.fromObject(pl.getPageRow());
					
		List<String> names=Arrays.asList(new String[]{"序号","功能模块","页面名称","页面链接"});
		
		ExportExcel excel=new ExportExcel();
		excel.exportPage(response, array, names);
		
	}
	
	
}
