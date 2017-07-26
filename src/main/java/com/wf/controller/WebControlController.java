package com.wf.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.bean.Control;
import com.wf.service.WebControlService;

@RequestMapping("control")
@Controller
public class WebControlController {
	
	
	@Autowired
	private WebControlService control;
	
	/**
	 * 节点监测查询
	 * @return
	 */
	@RequestMapping("getcontorl")
	@ResponseBody
	public List<Control> getControl(){
		List<Control> li = this.control.getControl();
		return li;
	}
	/**
	 * 节点修改页面
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping("updatecontrol")
	public String updateControl(String id,Map<String,Object> map){
		Control c = this.control.getControlById(id);
		map.put("control", c);
		return "/page/systemmanager/updatecontrol";
	}
	
	/**
	 * 节点修改
	 * @param id
	 * @param ip
	 * @return
	 */
	@RequestMapping("doupdatecontorl")
	@ResponseBody
	public boolean doUpdateControl(String id,String ip){
		boolean rt = this.control.doUpdateControl(id,ip);
		return rt;
	}
	/**
	 * 节点删除
	 * @param id
	 * @return
	 */
	@RequestMapping("dodeletecontrol")
	@ResponseBody
	public boolean doDeleteControl(String id){
		boolean rt = this.control.doDeleteControl(id);
		return rt ;
	}
	
	/**
	 * 节点添加页面
	 * @param pid
	 * @param map
	 * @return
	 */
	@RequestMapping("addcontrol")
	public String addcontrol(String pid,Map<String ,Object> map){
		map.put("pid", pid);
		return "/page/systemmanager/addcontrol";
	}
	
	/**
	 * 节点添加
	 * @param c
	 * @return
	 */
	@RequestMapping("doaddcontrol")
	@ResponseBody
	public boolean doaddcontrol(@ModelAttribute Control c){
		boolean rt = this.control.doAddControl(c);
		return rt ;
	}

}
