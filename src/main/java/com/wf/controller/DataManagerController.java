package com.wf.controller;

import java.util.Arrays;
import java.util.HashMap;
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

import com.exportExcel.ExportExcel;
import com.wf.bean.Datamanager;
import com.wf.bean.PageList;
import com.wf.service.DataManagerService;

@Controller
@RequestMapping("data")
public class DataManagerController {

	@Autowired
	private DataManagerService data;
	/**
	 * 获取数据库
	 * @param dataname
	 * @return
	 */
	@RequestMapping("getdata")
	@ResponseBody
	public PageList getData(String dataname,Integer pagenum,Integer pagesize){
		PageList pl = this.data.getData(dataname,pagenum,pagesize);
		return pl;
	}
	/**
	 * 删除数据库
	 * @param id
	 * @return
	 */
	@RequestMapping("deletedata")
	@ResponseBody
	public boolean deleteData(String id){
		boolean a = this.data.deleteData(id);
		return a;
	}
	/**
	 * 解冻数据库
	 * @param id
	 * @return
	 */
	@RequestMapping("opendata")
	@ResponseBody
	public boolean openData(String id){
		boolean a = this.data.openData(id);
		return a;
	}
	/**
	 * 冻结数据库
	 * @param id
	 * @return
	 */
	@RequestMapping("closedata")
	@ResponseBody
	public boolean closeData(String id){
		boolean a = this.data.closeData(id);
		return a;
	}
	
	
	/**
	 * 添加数据页
	 * @return
	 */
	@RequestMapping("adddata")
	public String addData(Map<String, Object> map){
		Map<String,Object> mm = this.data.getResource();
		map.put("rlmap", mm);
		return "/page/systemmanager/add_data";
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
	 */
	@RequestMapping("doadddata")
	@ResponseBody
	public boolean doAddData(@RequestParam(value="customs[]",required=false) String[] customs,@ModelAttribute Datamanager data){
		boolean rt = this.data.doAddData(customs,data);
		return rt;
	}
	/**
	 * 数据库修改页面
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping("updatedata")
	public String doUpdateData(String id,Map<String, Object> map){
		Map<String,Object> mm = this.data.getResource();
		Map<String,Object> check = this.data.getCheck(id);
		mm.putAll(check);
		map.put("rlmap", mm);
		return "/page/systemmanager/update_data";
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
	 */
	@RequestMapping("doupdatedata")
	@ResponseBody
	public boolean doUpdateData(@RequestParam(value="customs[]",required=false) String[] customs,@ModelAttribute Datamanager data){
		boolean rt = this.data.doUpdateData(customs,data);
		return rt;
	}
	
	@RequestMapping("pushdata")
	@ResponseBody
	public void pushdata() {
		
		this.data.selectZY();
	}
	
	/**
	 * 配置管理  数据库导出
	 * @param response
	 * @param dataname 数据库名称
	 */
	@RequestMapping("exportData")
	public void exportData(HttpServletResponse response,String dataname){
		List<Object> list=data.exportData(dataname);
		List<String> names=Arrays.asList(new String[]{"序号","数据库名称","数据库描述","数据库类型","数据库来源","资源类型","语种","自定义策略","状态"});
		JSONArray array=JSONArray.fromObject(list);
		ExportExcel excel=new ExportExcel();
		excel.exportData(response, array, names);
		
	}
}
