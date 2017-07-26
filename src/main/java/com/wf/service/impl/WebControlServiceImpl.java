package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.URLConnection;
import com.wf.bean.Control;
import com.wf.dao.ControlMapper;
import com.wf.service.WebControlService;

@Service
public class WebControlServiceImpl implements WebControlService {

	@Autowired
	private ControlMapper control;

	@Override
	public List<Control> getControl() {
		List<Control> li = new ArrayList<Control>();
		try {
			li = this.control.getControl();
			for (int i = 0;i<li.size();i++) {
				boolean rt = false;
				URLConnection uc = new URLConnection();
				rt = uc.testURL(li.get(i).getIp());
				if(rt){
					li.get(i).setAvailable(0);
				}else{
					li.get(i).setAvailable(1);
				}
			}
			this.control.deleteControl();
			this.control.insertControlBatch(li);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return li;
	}

	@Override
	public Control getControlById(String id) {
		Control c = new Control();
		try {
			c = this.control.getControlById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	@Override
	public boolean doUpdateControl(String id, String ip) {
		boolean rt = false;
		int num = 0 ;
		try {
			num = this.control.doUpdateControl(id,ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}

	@Override
	public boolean doDeleteControl(String id) {
		boolean rt = false;
		int num = 0 ;
		try {
			num = this.control.doDeleteControl(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}

	@Override
	public boolean doAddControl(Control c) {
		boolean rt = false;
		int num = 0 ; 
		try {
			c.setId(UUID.randomUUID().toString());
			num = this.control.doAddControl(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}

}
