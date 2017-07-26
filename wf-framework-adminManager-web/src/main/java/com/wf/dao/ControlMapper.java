package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Control;

public interface ControlMapper {
   List<Control> getControl();
   
   int UpdateControl(List<Control> li);
   
   int insertControlBatch(List<Control> li);
   
   int deleteControl();
   
   Control getControlById(@Param("id") String id);
   
   int doUpdateControl(@Param("id") String id,@Param("ip") String ip);
   
   int doDeleteControl(@Param("id") String id);
   
   int doAddControl(Control c);
}