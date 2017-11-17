package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.CookieUtil;
import com.utils.DateTools;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.bean.ResourcePrice;
import com.wf.bean.ResourceType;
import com.wf.service.LogService;
import com.wf.service.ResourcePriceService;

@Controller
@RequestMapping("resourceprice")
public class ResourcePriceController {

	@Autowired
	private ResourcePriceService Pservice;
	
	
	@Autowired
	private LogService logService;
	
	/**根据条件获取所有的资源单价
	 * 
	 * @return 资源单价列表
	 * @throws Exception 
	 */
	@RequestMapping("getprice")
	@ResponseBody
	public PageList getPrice(@RequestParam(value="Rname",required =false) String  name,
			Integer pagesize,Integer pagenum,HttpServletRequest request) throws Exception{
		if(name!=null&&name!=""){
			name="%"+name+"%";
		}
		PageList Rnamelist  = this.Pservice.getPrice(name,pagesize,pagenum);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("查询");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("资源单价配置管理");
		log.setOperation_content("资源单价配置管理条件:产品名称:"+name);
		logService.addLog(log);
		
		return Rnamelist;
	}
	
	/**删除的资源单价
	 * @throws Exception 
	 * 
	 * 
	 */
	@RequestMapping("deleteprice")
	@ResponseBody
	public boolean delectPrice(@RequestParam(value="ids[]",required =false) String[] ids,HttpServletRequest request) throws Exception{
		boolean re = this.Pservice.delectPrice(ids);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("删除");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("资源单价配置管理");
		log.setOperation_content("删除的产品ID:"+ids.toString());
		logService.addLog(log);

		return re;
	}
	/**获取所有的资源类型
	 * 
	 * 
	 */
	@RequestMapping("getRtype")
	@ResponseBody
	public List<ResourceType> getRtype(){
		List<ResourceType> rt = this.Pservice.getRtype();
		return rt;
	}
	
	/**
	 * 重复查询
	 * @param resource_type
	 * @param rid
	 * @return
	 */
	@RequestMapping("checkpricerid")
	@ResponseBody
	public boolean checkPriceRID(String name,String rid){
		boolean rt = this.Pservice.checkPriceRID(name, rid);
		return rt;
	}
	
	/**
	 * 添加资源单价
	 * @param rp
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doaddprice")
	@ResponseBody
	public boolean doAddPrice(@ModelAttribute ResourcePrice rp,HttpServletRequest request) throws Exception{
		boolean rt = this.Pservice.doAddPrice(rp);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("增加");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("资源单价配置管理");
		log.setOperation_content("增加的产品定价信息:"+rp.toString());
		logService.addLog(log);

		return rt;
	}
	
	/**
	 * 修改资源单价
	 * @param rp
	 * @return
	 * @throws Exception 
	 */
	
	@RequestMapping("doupdateprice")
	@ResponseBody
	public boolean doUpdatePrice(@ModelAttribute ResourcePrice rp,HttpServletRequest request) throws Exception{
		boolean rt = this.Pservice.doUpdatePrice(rp);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("修改");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().toString());
		log.setModule("资源单价配置管理");
		log.setOperation_content("修改后的产品定价信息:"+rp.toString());
		logService.addLog(log);

		return rt;
	}
}
