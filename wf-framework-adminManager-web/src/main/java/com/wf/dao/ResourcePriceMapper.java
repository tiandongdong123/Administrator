package com.wf.dao;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.ResourcePrice;


public interface ResourcePriceMapper {   

   List<Object> getPrice(@Param("name") String name,@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
   
   int getPriceNum(@Param("name") String name);
    
   int deletePrice(@Param("ids") String[] ids);
   
   List<ResourcePrice> checkPriceRid(@Param("name") String name,@Param("rid") String rid);
   
   int doAddPrice(ResourcePrice rp);
   
   ResourcePrice getRP(@Param("rid") String  id);
   
   int doUpdatePrice(ResourcePrice rp);
   
   /**
    *	通过数据库code获取产品
    */
   List<Map<String, Object>> getPriceBySourceCode(@Param("sourceCode") String sourceCode);
   
   /**
    *	通过产品id获取产品信息
    */
   Map<String, Object> getPriceByRid(@Param("rid") String rid);
   
}