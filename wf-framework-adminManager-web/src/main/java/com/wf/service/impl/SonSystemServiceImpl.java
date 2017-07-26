package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.bean.SonSystem;
import com.wf.dao.SonSystemMapper;
import com.wf.service.SonSystemService;


@Service
public class SonSystemServiceImpl implements SonSystemService {

	@Autowired
	private SonSystemMapper ptm;
 	
	
	@Override
	public PageList getSon(Integer pagenum, Integer pagesize) {
		// TODO Auto-generated method stub
		PageList pl = new PageList();
		int num = 0;
		int pm = (pagenum-1)*pagesize;
		try {
			List<Object>  p = this.ptm.getSon(pm, pagesize);
			num = this.ptm.getSonNum();
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
	public boolean deleteSon(String[] ids) {
		// TODO Auto-generated method stub
		int result = 0;
		boolean re = false;
		try {
			 result = this.ptm.deleteSon(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public boolean checkSon(SonSystem son) {
		// TODO Auto-generated method stub
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li= this.ptm.checkSon(son);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		return rt;
	}

	@Override
	public boolean doAddSon(SonSystem son) {
		// TODO Auto-generated method stub
		int result = 0;
		boolean re = false;
		try {
			 result = this.ptm.doAddSon(son);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public boolean doUpdateSon(SonSystem son) {
		// TODO Auto-generated method stub
		int result = 0;
		boolean re = false;
		try {
			 result = this.ptm.doUpdateSon(son);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public List<SonSystem> getAll() {
		// TODO Auto-generated method stub
		List<SonSystem> list=this.ptm.getAll();
		return list;
	}

	@Override
	public SonSystem getOneSon(String sonCode) {
		// TODO Auto-generated method stub
		SonSystem son=this.ptm.getOneSon(sonCode);
				
		return son;
	}
}


	
