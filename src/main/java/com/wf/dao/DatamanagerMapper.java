package com.wf.dao;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Datamanager;

public interface DatamanagerMapper {

    
    List<Map<String, Object>> selectList();
    
    Map<String, Object> selectDataByPsc(@Param("productSourceCode") String productSourceCode);
    
    List<Object> getData(@Param("dataname") String dataname,@Param("dataname1") String dataname1,@Param("pagenum") Integer pagenum,@Param("pagesize") Integer pagesize);
    
    int getDataNum(@Param("dataname") String dataname);
    
    int deleteData(@Param("id") String id);
    
    int closeData(@Param("id") String id);
    
    int openData(@Param("id") String id);
    
    List<Object> checkDname(@Param("name") String name);
    
    int doAddData(Datamanager data);
    
    Datamanager getDataManagerById(@Param("id") String id);
    
    int doUpdateData(Datamanager data);
    
    List<Datamanager> selectAll();
    
    Datamanager getDataManagerBySourceCode(@Param("productSourceCode") String productSourceCode);
    
   JSONArray selectZY();
    
   List<Object> exportData(@Param("dataname") String dataname,@Param("dataname1") String dataname1);
}