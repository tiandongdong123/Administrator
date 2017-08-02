package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.bean.ProductType;
import com.wf.dao.ProductTypeMapper;
import com.wf.service.ProductTypeServise;

@Service
public class ProductTypeServiceImpl implements ProductTypeServise {
	
	@Autowired
	private ProductTypeMapper ptm;
 	
	@Override
	public PageList getProduct(Integer pagenum, Integer pagesize) {
		// TODO Auto-generated method stub
		PageList pl = new PageList();
		int num = 0;
		int pm = (pagenum-1)*pagesize;
		try {
			List<Object>  p = this.ptm.getProduct(pm, pagesize);
			num = this.ptm.getProductNum();
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
	public boolean deleteProduct(String[] ids) {
		// TODO Auto-generated method stub
		int result = 0;
		boolean re = false;
		try {
			 result = this.ptm.deleteProduct(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public boolean checkProduct(ProductType product) {
		// TODO Auto-generated method stub
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li= this.ptm.checkProduct(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		return rt;
	}

	@Override
	public boolean doAddProduct(ProductType product) {
		// TODO Auto-generated method stub
		int result = 0;
		boolean re = false;
		try {
			 result = this.ptm.doAddProduct(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public boolean doUpdateProduct(ProductType product) {
		// TODO Auto-generated method stub
		int result = 0;
		boolean re = false;
		try {
			 result = this.ptm.doUpdateProduct( product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public List<ProductType> getByCode(String code) {
		// TODO Auto-generated method stub
		
		List<ProductType> list=this.ptm.getByCode(code);
		return list;
	}

}
