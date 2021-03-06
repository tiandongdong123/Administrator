package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Datamanager;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.bean.SonSystem;
import com.wf.service.DataManagerService;
import com.wf.service.LogService;
import com.wf.service.SonSystemService;

@Controller
@RequestMapping("/son")
public class SonSystemController {

	@Autowired
	SonSystemService pts;
	
	@Autowired
	DataManagerService data;
	
	@Autowired
	LogService logService;
	
	@RequestMapping("getson")
	@ResponseBody
	public PageList getProduct(@RequestParam("pagenum") Integer pagenum,
			@RequestParam("pagesize") Integer pagesize,HttpServletRequest request){
		PageList  pl = this.pts.getSon(pagenum, pagesize);
		for(int i=0;i<pl.getPageRow().size();i++){
			SonSystem son=(SonSystem)pl.getPageRow().get(i);
			String code =son.getProductResourceCode();
			List<String> list=Arrays.asList(code.split(","));
			String sourcedata="";
			for(int j=0;j<list.size();j++){
				Datamanager json=data.getDataManagerBySourceCode(list.get(j));
				if(null!=json){
					if(j==0){
						sourcedata=json.getTableName();
					}else{
						sourcedata=sourcedata+","+json.getTableName();
					}
				}
			}
			son.setProductResourceCode(sourcedata);
			pl.getPageRow().remove(i);
			pl.getPageRow().add(i, son);
		}
		
		//记录日志
		Log log=new Log("平台配置管理","查询","",request);
		logService.addLog(log);

		return pl;
	}
	
	/**
	 * 删除资源单位
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("deleteson")
	@ResponseBody
	public boolean deleteIds(@RequestParam(value="ids[]",required=false) String[] ids,HttpServletRequest request){
		boolean rt = this.pts.deleteSon(ids);
		
		//记录日志
		Log log=new Log("平台配置管理","删除","删除的平台ID:"+(ids==null?"":Arrays.asList(ids)),request);
		logService.addLog(log);

		return rt;
	}
	
	/**
	 * 判断资源单位名称是否重复
	 * @param unitname
	 * @return
	 */
	@RequestMapping("checkson")
	@ResponseBody
	public boolean checkProduct(HttpServletRequest request){
		SonSystem son=new SonSystem();
		//String source_code=request.getParameter("productResourceCode");
		String name=request.getParameter("sonName");
		String code=request.getParameter("sonCode");
		//son.setProductResourceCode(source_code);
		son.setSonCode(code);
		son.setSonName(name);
		boolean rt = this.pts.checkSon(son);
		return rt;
	}
	
	/**
	 * 添加子系统
	 * @param unitname
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doaddson")
	@ResponseBody
	public boolean doAddSon(HttpServletRequest request){
		SonSystem son=new SonSystem();
		String source_code=request.getParameter("productResourceCode");
		String name=request.getParameter("sonName");
		String code=request.getParameter("sonCode");
		son.setProductResourceCode(source_code==null?"":source_code);
		son.setSonCode(code);
		son.setSonName(name);
		boolean rt = this.pts.doAddSon(son);
		
		//记录日志
		Log log=new Log("平台配置管理","增加",son.toString(),request);
		logService.addLog(log);

		return rt;
	}
	/**
	 * 更新子系统
	 * @param son
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doupdateson")
	@ResponseBody
	public boolean doUpdateSon(HttpServletRequest request){
		SonSystem son=new SonSystem();
		String id=request.getParameter("id");
		String source_code=request.getParameter("productResourceCode");
		String name=request.getParameter("sonName");
		String code=request.getParameter("sonCode");
		son.setProductResourceCode(source_code==null?"":source_code);
		son.setSonCode(code);
		son.setSonName(name);
		son.setId(Integer.valueOf(id));
		boolean rt = this.pts.doUpdateSon(son);
		
		//记录日志
		Log log=new Log("平台配置管理","修改",son.toString(),request);
		logService.addLog(log);

		return rt ;
	}
	
	@RequestMapping("source_data")
	@ResponseBody
	public Object source_data() {
		
	List<Datamanager> list=data.selectAll();
		
		return list;
	}
	
}
