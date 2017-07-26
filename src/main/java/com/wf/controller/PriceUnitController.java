package com.wf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.bean.PageList;
import com.wf.service.PriceUnitService;

@Controller
@RequestMapping("unit")
public class PriceUnitController {

	@Autowired
	private PriceUnitService unit;
	/**
	 * 获取资源单位
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	@RequestMapping("getunit")
	@ResponseBody
	public PageList getUnit(@RequestParam("pagenum") Integer pagenum,
			@RequestParam("pagesize") Integer pagesize){
		PageList  pl = this.unit.getUnit(pagenum, pagesize);
		return pl;
	}
	
	/**
	 * 删除资源单位
	 * @param ids
	 * @return
	 */
	@RequestMapping("deleteunit")
	@ResponseBody
	public boolean deleteAdmin(@RequestParam(value="ids[]",required=false) String[] ids){
		boolean rt = this.unit.deleteUnit(ids);
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
	 * @return
	 */
	@RequestMapping("doaddunit")
	@ResponseBody
	public boolean doAddUnit(String unitname,String unitcode){
		boolean rt = this.unit.doAddUnit(unitname,unitcode);
		return rt;
	}
	
	@RequestMapping("doupdateunit")
	@ResponseBody
	public boolean doUpdateUnit(String unitname,String unitcode,Integer id){
		boolean rt = this.unit.doUpdateUnit(unitname,unitcode,id);
		return rt ;
	}
}
