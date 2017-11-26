package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.service.LogService;
import com.wf.service.PriceUnitService;

@Controller
@RequestMapping("unit")
public class PriceUnitController {

	@Autowired
	private PriceUnitService unit;
	
	@Autowired
	LogService logService;
	
	/**
	 * 获取资源单位
	 * @param pagenum
	 * @param pagesize
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("getunit")
	@ResponseBody
	public PageList getUnit(@RequestParam("pagenum") Integer pagenum,
			@RequestParam("pagesize") Integer pagesize,HttpServletRequest request){
		PageList  pl = this.unit.getUnit(pagenum, pagesize);
		
		//记录日志
		Log log=new Log("单位设置","查询","",request);
		logService.addLog(log);

		return pl;
	}
	
	/**
	 * 删除资源单位
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("deleteunit")
	@ResponseBody
	public boolean deleteAdmin(@RequestParam(value="ids[]",required=false) String[] ids,HttpServletRequest request){
		boolean rt = this.unit.deleteUnit(ids);
		
		//记录日志
		Log log=new Log("单位设置","删除","删除的资源单位ID:"+(ids==null?"":Arrays.asList(ids)),request);
		logService.addLog(log);
		
		return rt;
	}
	
	/**
	 * 判断资源单位名称是否重复
	 * @param unitname
	 * @return
	 */
	@RequestMapping("checkunit")
	@ResponseBody
	public boolean checkUnit(String unitname,String unitcode){
		boolean rt = this.unit.checkUnit(unitname,unitcode);
		return rt;
	}
	
	/**
	 * 添加资源单位
	 * @param unitname
	 * @param request 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doaddunit")
	@ResponseBody
	public boolean doAddUnit(String unitname,String unitcode, HttpServletRequest request){
		boolean rt = this.unit.doAddUnit(unitname,unitcode);
		
		//记录日志
		Log log=new Log("单位设置","增加","增加的资源单位信息:unitname:"+unitname+"unitcode:"+unitcode,request);
		logService.addLog(log);

		return rt;
	}
	
	@RequestMapping("doupdateunit")
	@ResponseBody
	public boolean doUpdateUnit(String unitname,String unitcode,Integer id, HttpServletRequest request){
		boolean rt = this.unit.doUpdateUnit(unitname,unitcode,id);
		
		//记录日志
		Log log=new Log("单位设置","修改","修改后的资源单位信息:unitname:"+unitname+"unitcode:"+unitcode,request);
		logService.addLog(log);

		return rt ;
	}
}
