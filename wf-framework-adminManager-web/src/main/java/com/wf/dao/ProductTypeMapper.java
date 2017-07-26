package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ProductType;

public interface ProductTypeMapper {

		List<Object> getProduct(@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
		
		int getProductNum();

		
		int deleteProduct(@Param("ids") String[] ids);
		
		List<Object> checkProduct(ProductType product);
		
		int doAddProduct(ProductType product);
		
		int doUpdateProduct(ProductType product);
		
		List<Object> getRprodutName();
		
		ProductType  getOneProduct(@Param("resourceTypeCode") String productcode);
		
		List<ProductType> getByCode(@Param("productResourceCode") String code);
}
