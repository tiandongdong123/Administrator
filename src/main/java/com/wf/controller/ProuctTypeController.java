package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
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
import com.wf.bean.Datamanager;
import com.wf.bean.Log;
import com.wf.bean.PageList;
import com.wf.bean.ProductType;
import com.wf.service.DataManagerService;
import com.wf.service.LogService;
import com.wf.service.ProductTypeServise;

@Controller
@RequestMapping("product")
public class ProuctTypeController {
	
	@Autowired
	private ProductTypeServise pts;
	
	@Autowired
	DataManagerService data;
	
	@Autowired
	LogService logService;
	
	
	@RequestMapping("getproduct")
	@ResponseBody
	public PageList getProduct(@RequestParam("pagenum") Integer pagenum,
			@RequestParam("pagesize") Integer pagesize,HttpServletRequest request){
		PageList  pl = this.pts.getProduct(pagenum, pagesize);
		for(int i=0;i<pl.getPageRow().size();i++){
			ProductType Product=(ProductType)pl.getPageRow().get(i);
			String code =Product.getProductResourceCode();	
			Datamanager json=data.getDataManagerBySourceCode(code);
			if(json!=null){				
				Product.setProductResourceCode(json.getTableName());
			}
			pl.getPageRow().remove(i);
			pl.getPageRow().add(i, Product);
		}
		
		//记录日志
		Log log=new Log("产品类型设置","查询","",request);
		logService.addLog(log);

		return pl;
	}
	
	/**
	 * 删除资源单位
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("deleteproduct")
	@ResponseBody
	public boolean deleteIds(@RequestParam(value="ids[]",required=false) String[] ids,HttpServletRequest request){
		boolean rt = this.pts.deleteProduct(ids);
		
		//记录日志
		Log log=new Log("产品类型设置","删除","删除的产品类型ID"+(ids==null?"":Arrays.asList(ids)),request);
		logService.addLog(log);
		
		return rt;
	}
	
	/**
	 * 判断资源单位名称是否重复
	 * @param unitname
	 * @return
	 */
	@RequestMapping("checkproduct")
	@ResponseBody
	public boolean checkProduct(@ModelAttribute ProductType product){
		boolean rt = this.pts.checkProduct(product);
		return rt;
	}
	
	/**
	 * 添加资源单位
	 * @param unitname
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("doaddproduct")
	@ResponseBody
	public boolean doAddProduct(@ModelAttribute ProductType product,HttpServletRequest request){
		boolean rt = this.pts.doAddProduct(product);
		
		//记录日志
		Log log=new Log("产品类型设置","增加",product.toString(),request);
		logService.addLog(log);
		
		return rt;
	}
	
	@RequestMapping("doupdateproduct")
	@ResponseBody
	public boolean doUpdateProduct(@ModelAttribute ProductType product,HttpServletRequest request){
		boolean rt = this.pts.doUpdateProduct(product);
		
		//记录日志
		Log log=new Log("产品类型设置","修改",product.toString(),request);
		logService.addLog(log);
		
		return rt ;
	}
	
	
	@RequestMapping("getSource")
	@ResponseBody
	public Object getsource() {
		
	List<Datamanager> list=data.selectAll();
		
	return list;
	}
}
