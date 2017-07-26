package com.wf.service;

import java.util.List;

import com.wf.bean.Control;

public interface WebControlService {

	List<Control> getControl();
	
	Control getControlById(String id);
	
	boolean doUpdateControl(String id,String ip);
	
	boolean doDeleteControl(String id);
	
	boolean doAddControl(Control c);
}
