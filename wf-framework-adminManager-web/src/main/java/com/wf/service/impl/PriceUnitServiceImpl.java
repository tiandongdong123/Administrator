package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.dao.PriceUnitMapper;
import com.wf.service.PriceUnitService;

@Service
public class PriceUnitServiceImpl implements PriceUnitService {

	@Autowired
	private PriceUnitMapper unit;
	@Override
	public PageList getUnit(Integer pagenum,Integer pagesize) {
		PageList pl = new PageList();
		int num = 0;
		int pm = (pagenum-1)*pagesize;
		try {
			List<Object>  p = this.unit.getUnit(pm,pagesize);
			num = this.unit.getUnitNum();
			pl.setPageNum(pagenum);
			pl.setPageRow(p);
			pl.setPageSize(pagesize);
			pl.setPageTotal(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pl;
	}
	@Override
	public boolean deleteUnit(String[] ids) {
		int result = 0;
		boolean re = false;
		try {
			 result = this.unit.deleteUnit(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}
	@Override
	public boolean checkUnit(String unitname,String unitcode) {
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li= this.unit.checkUnit(unitname,unitcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public boolean doAddUnit(String unitname,String unitcode) {
		int result = 0;
		boolean re = false;
		try {
			 result = this.unit.doAddUnit(unitname,unitcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}
	@Override
	public boolean doUpdateUnit(String unitname,String unitcode, Integer id) {
		int result = 0;
		boolean re = false;
		try {
			 result = this.unit.doUpdateUnit(unitname,unitcode,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}
}
