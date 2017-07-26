package com.wf.service;

import java.util.List;

import com.wf.bean.PageList;
import com.wf.bean.ProductType;

public interface ProductTypeServise {

	PageList getProduct(Integer pagenum,Integer pagesize);
	
	boolean	deleteProduct(String[] ids);
	
	boolean checkProduct(ProductType product);
	
	boolean doAddProduct(ProductType product);
	
	boolean doUpdateProduct(ProductType product);
	
	List<ProductType> getByCode(String code);
}
