package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PageList;
import com.wf.bean.PageManager;
import com.wf.dao.PageManagerMapper;
import com.wf.service.PageManagerService;
@Service
public class PageManagerServiceImpl implements PageManagerService{
	@Autowired
	private PageManagerMapper pageManagerMapper;

	/**
	 * 获取页面列表数据
	 */
	@Override
	public PageList getPage(String[] ids, String pageName, Integer pageNum,
			Integer pageSize) {
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			Integer startNum = (pageNum-1)*pageSize;
			pageName="%"+pageName+"%";
			int num = pageManagerMapper.getPageNum(ids,pageName);
			r = pageManagerMapper.getPage(ids,pageName,startNum,pageSize);
			p.setPageRow(r);
			p.setPageNum(pageNum);
			p.setPageTotal(num);
			p.setPageSize(pageSize);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * 根据id查询对象数据
	 */
	@Override
	public PageManager getPageManagerById(String id) {
		PageManager pm = new PageManager();
		try {
			pm = pageManagerMapper.getPageManagerById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pm;
	}

	@Override
	public boolean doAddPageManager(PageManager pm) {
			boolean rt = false;
			int x = 0;
			try {
				pm.setId(UUID.randomUUID().toString());
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				pm.setAddTime(df.format(new Date()));
				x = pageManagerMapper.doAddPageManager(pm);
			} catch (Exception e) {
				e.printStackTrace();
	    }
			if(x>0){
				rt=true;
			}
			return rt;
	}
	
	@Override
	public boolean doUpdatePageManager(PageManager pm) {
		boolean rt = false;
		int num = 0;
		try {
			num = pageManagerMapper.doUpdatePageManager(pm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}
	
	@Override
	public boolean deletePageManager(String id) {
		boolean rt = false;
		int num = 0;
		try {
			num = pageManagerMapper.deletePageManager(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}

	@Override
	public PageList exportpage(String[] ids, String pageName) {
		
		PageList p=new PageList();
		List<Object> list=new ArrayList<Object>();
		try {
			pageName="%"+pageName+"%";
			list= pageManagerMapper.getAllPage(ids,pageName);
			p.setPageRow(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return p;
	}

	@Override
	public List<Object> getKeyWord(String pageName) {
		return pageManagerMapper.getKeyword(pageName);
	}

	@Override
	public Integer getCountBymodularId(String modelarId) {
		return pageManagerMapper.getCountBymodularId(modelarId);
 	}

	
}
