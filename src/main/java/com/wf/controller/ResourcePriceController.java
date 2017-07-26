package com.wf.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.bean.PageList;
import com.wf.bean.ResourcePrice;
import com.wf.bean.ResourceType;
import com.wf.service.ResourcePriceService;

@Controller
@RequestMapping("resourceprice")
public class ResourcePriceController {

	@Autowired
	private ResourcePriceService Pservice;
	/**根据条件获取所有的资源单价
	 * 
	 * @return 资源单价列表
	 */
	@RequestMapping("getprice")
	@ResponseBody
	public PageList getPrice(@RequestParam(value="Rname",required =false) String  name,Integer pagesize,Integer pagenum){
		if(name!=null&&name!=""){
			name="%"+name+"%";
		}
		PageList Rnamelist  = this.Pservice.getPrice(name,pagesize,pagenum);
		return Rnamelist;
	}
	
	/**删除的资源单价
	 * 
	 * 
	 */
	@RequestMapping("deleteprice")
	@ResponseBody
	public boolean delectPrice(@RequestParam(value="ids[]",required =false) String[] ids){
		boolean re = this.Pservice.delectPrice(ids);
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
	 */
	@RequestMapping("doaddprice")
	@ResponseBody
	public boolean doAddPrice(@ModelAttribute ResourcePrice rp){
		boolean rt = this.Pservice.doAddPrice(rp);
		return rt;
	}
	
	/**
	 * 修改资源单价
	 * @param rp
	 * @return
	 */
	
	@RequestMapping("doupdateprice")
	@ResponseBody
	public boolean doUpdatePrice(@ModelAttribute ResourcePrice rp){
		boolean rt = this.Pservice.doUpdatePrice(rp);
		return rt;
	}
}
