package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.Modular;
import com.wf.bean.PageList;
import com.wf.dao.ModularMapper;
import com.wf.service.ModularService;
@Service
public class ModularServiceImpl implements ModularService{
	@Autowired
	private ModularMapper modularMapper;

	/**
	 * 获取页面列表数据
	 */
	@Override
	public PageList getModular(String[] ids, Integer pageNum,
			Integer pageSize) {
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			Integer startNum = (pageNum-1)*pageSize;
			int num = modularMapper.getPageNum(ids);
			r = modularMapper.getPage(ids,startNum,pageSize);
			p.setPageRow(r);
			p.setPageNum(pageNum);
			p.setPageTotal(num);
			p.setPageSize(pageSize);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	@Override
	public boolean doAddModular(Modular md) {
		boolean rt = false;
		int x = 0;
		try {
			md.setId(UUID.randomUUID().toString());
			x = modularMapper.doAddModular(md);
		} catch (Exception e) {
			e.printStackTrace();
    }
		if(x>0){
			rt=true;
		}
		return rt;
	}
	
	/**
	 * 根据id查询对象数据
	 */
	@Override
	public Modular getModularById(String id) {
		Modular md = new Modular();
		try {
			md = modularMapper.getModularById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md;
	}
	
	@Override
	public boolean doUpdateModular(Modular md) {
		boolean rt = false;
		int num = 0;
		try {
			num = modularMapper.doUpdateModular(md);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}
	
	@Override
	public boolean deleteModular(String id) {
		boolean rt = false;
		int num = 0;
		try {
			num = modularMapper.deleteModular(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}
	
	/**
	 * 获取列表数据
	 */
	@Override
	public List<Object> getModularList() {
		List<Object> modularList = new ArrayList<Object>();
		try {
			modularList = modularMapper.getModularList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modularList;
	}

	@Override
	public List<Object> exportmodular(String[] ids) {
		
		List<Object> list=new ArrayList<Object>();
		
		try {
			list=modularMapper.getModularAll(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	

}
