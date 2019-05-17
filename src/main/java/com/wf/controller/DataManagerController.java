package com.wf.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.redis.RedisUtil;
import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.JsonUtil;
import com.wf.Setting.DatabaseConfigureSetting;

import net.sf.json.JSONArray;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exportExcel.ExportExcel;
import com.wf.bean.Datamanager;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.service.DataManagerService;
import com.wf.service.LogService;

@Controller
@RequestMapping("data")
public class DataManagerController {

	@Autowired
	private DataManagerService data;

	@Autowired
	LogService logService;

	DatabaseConfigureSetting dbConfig = new DatabaseConfigureSetting();
	/**
	 * 获取数据库
	 * @param dataname
	 * @return
	 */
	@RequestMapping("getdata")
	@ResponseBody
	public PageList getData(Integer pagenum,Integer pagesize,String dataname,HttpServletRequest request){

		//记录日志
		Log log=new Log("数据库配置管理","查询","数据库名称:"+dataname,request);
		logService.addLog(log);

		if(dataname==null){
			PageList p = this.data.getData(pagenum,pagesize);
			return p;
		}else{
			PageList p = this.data.findDatabaseByName(dataname);
			return p;
		}

	}
	/**
	 * 资源类型上移
	 * @throws Exception 
	 */
	@RequestMapping("/moveUpDatabase")
	public void moveUpDatabase(
			@RequestParam(value="id",required=false) String id,HttpServletResponse response,HttpServletRequest request) throws Exception {

		boolean b=this.data.moveUpDatabase(id);
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		JSONArray list = dbConfig.selectSitateFoOne();
		RedisUtil.del("datamanager");
		RedisUtil.set("datamanager", list.toString(), 6);
		JsonUtil.toJsonHtml(response, b);

		//记录日志
		Log log=new Log("数据库配置管理","上移","资源类型ID:"+id,request);
		logService.addLog(log);

	}
	/**
	 * 资源类型下移
	 */
	@RequestMapping("/moveDownDatabase")
	public void moveDownDatabase(
			@RequestParam(value="id",required=false) String id,HttpServletResponse response,HttpServletRequest request) throws Exception {
		boolean b=this.data.moveDownDatabase(id);
		
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		JSONArray list = dbConfig.selectSitateFoOne();
		RedisUtil.del("datamanager");
		RedisUtil.set("datamanager", list.toString(), 6);
		JsonUtil.toJsonHtml(response, b);
		
		//记录日志
		Log log=new Log("数据库配置管理","下移","资源类型ID:"+id,request);
		logService.addLog(log);

	}

	/**
	 * 删除数据库
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deletedata")
	@ResponseBody
	public boolean deleteData(String id,HttpServletRequest request){
		boolean a = this.data.deleteData(id);

		//记录日志
		Log log=new Log("数据库配置管理","删除","资源类型ID:"+id,request);
		logService.addLog(log);

		return a;
	}
	/**
	 *判断数据库state
	 */
	@RequestMapping("/checkResourceForOne")
	public void checkResourceForOne(String id,HttpServletResponse response,HttpServletRequest request) throws Exception {
		boolean result = this.data.checkResourceForOne(id);
		JsonUtil.toJsonHtml(response, result);
	}
	/**
	 *判断数据库status
	 */
	@RequestMapping("/checkStatus")
	public void checkStatus(String id,HttpServletResponse response,HttpServletRequest request) throws Exception {
		boolean result = this.data.checkStatus(id);
		JsonUtil.toJsonHtml(response, result);
	}
	/**
	 * 发布数据库
	 * @param id
	 * @return
	 */
	@RequestMapping("releaseData")
	@ResponseBody
	public boolean releaseData(String id) throws InterruptedException {
		boolean result = this.data.releaseData(id);
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		this.data.selectZY();
		return result;
	}
	/**
	 * 下撤数据库
	 * @param id
	 * @return
	 */
	@RequestMapping("descendData")
	@ResponseBody
	public boolean descendData(String id) throws InterruptedException {
		boolean result = this.data.descendData(id);
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		this.data.selectZY();
		return result;
	}

	/**
	 * 解冻数据库
	 * @param id
	 * @return
	 */
	@RequestMapping("opendata")
	@ResponseBody
	public boolean openData(String id,HttpServletRequest request) throws Exception {

		boolean result = this.data.openData(id);
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		this.data.selectZY();
		
		//记录日志
		Log log=new Log("数据库配置管理","解冻","解冻资源类型ID:"+id,request);
		logService.addLog(log);
		
		return result;
	}
	/**
	 * 冻结数据库
	 * @param id
	 * @return
	 */
	@RequestMapping("closedata")
	@ResponseBody
	public boolean closeData(String id,HttpServletRequest request) throws Exception {

		boolean result = this.data.closeData(id);
		//存到zookeeper后会有反应时间，sleep防止数据不能实时更新
		Thread.sleep(100);
		this.data.selectZY();
		
		//记录日志
		Log log=new Log("数据库配置管理","冻结","冻结资源类型ID:"+id,request);
		logService.addLog(log);
		return result;
	}
	

	/**
	 * 获取学科分类
	 * @return
	 */
	@RequestMapping("getsubjcet")
	@ResponseBody
	public Map<String,Object> getSubject(){
		Map<String,Object> mm  = this.data.getSubject();
		return mm;
	}
	
	/**
	 * 判断语种是否重复
	 * @param name
	 * @return
	 */
	@RequestMapping("checklname")
	@ResponseBody
	public boolean checkLName(String name,String code ){
		boolean rt = this.data.checkLName(name,code);
		return rt ;
	}
	
	/**
	 * 添加语种
	 * @param name
	 * @return
	 */
	@RequestMapping("doaddlname")
	@ResponseBody
	public boolean doAddLName(String name,String code){
		boolean rt = this.data.doAddLName(name,code);
		return rt;
	}
	
	/**
	 * 判断来源是否重复
	 * @param name
	 * @return
	 */
	@RequestMapping("checksname")
	@ResponseBody
	public boolean checkSName(String name,String code){
		boolean rt = this.data.checkSName(name,code);
		return rt ;
	}
	
	/**
	 * 添加语种
	 * @param name
	 * @return
	 */
	@RequestMapping("doaddsname")
	@ResponseBody
	public boolean doAddSName(String name,String code){
		boolean rt = this.data.doAddSName(name,code);
		return rt;
	}
	/**
	 * 判断数据库名称是否重复
	 * @param name
	 * @return
	 */
	@RequestMapping("checkdname")
	@ResponseBody
	public boolean checkDname(String name){
		boolean rt = this.data.checkDname(name);
		return rt;
	}
	
	/**
	 * 数据库添加
	 * @param customs
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("doadddata")
	@ResponseBody
	public boolean doAddData(@RequestParam(value="customs[]",required=false) String[] customs,@ModelAttribute Datamanager data,
			HttpServletRequest request){
		boolean rt = this.data.doAddData(customs,data);

		//记录日志
		Log log=new Log("数据库配置管理","增加",data.toString(),request);
		logService.addLog(log);

		return rt;
	}

	
	/**
	 * 获取选中学科分类
	 * @return
	 */
	@RequestMapping("getchecksubjcet")
	@ResponseBody
	public Map<String,Object> getCheckSubjcet(String id){
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> mm  = this.data.getSubject();
		List<String> ids = this.data.getCheckids(id);
		map.put("json",mm);
		map.put("checkids",ids);
		return map;
	}
	
	/**
	 * 数据库修改
	 * @param customs
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("doupdatedata")
	@ResponseBody
	public boolean doUpdateData(@RequestParam(value="customs[]",required=false) String[] customs,@ModelAttribute Datamanager data,
			HttpServletRequest request) throws Exception{
		boolean rt = this.data.doUpdateData(customs,data);

		//记录日志
		Log log=new Log("数据库配置管理","修改","修改后数据库信息:"+data.toString(),request);
		logService.addLog(log);

		return rt;
	}
	
	@RequestMapping("pushdata")
	@ResponseBody
	public void pushdata() throws Exception {

		this.data.selectZY();

	}
	
	/**
	 * 配置管理  数据库导出
	 * @param response
	 * @param dataname 数据库名称
	 * @throws Exception
	 */
	@RequestMapping("exportData")
	public void exportData(HttpServletRequest request,HttpServletResponse response,String dataname){

		//记录日志
		Log log=new Log("数据库配置管理","导出","导出查询条件数据库名称:"+dataname,request);
		logService.addLog(log);

		List<Object> list =data.exportData(dataname);
		if(null==list){
			list=new ArrayList<Object>();
		}else{
			list=data.exportData(dataname);
		}

		List<String> names=Arrays.asList(new String[]{"序号","数据库名称","数据库描述","数据库类型","数据库来源","资源类型","语种","自定义策略","状态"});
		JSONArray array=JSONArray.fromObject(list);
		ExportExcel excel=new ExportExcel();
		excel.exportData(response, array, names);
		
	}
}
