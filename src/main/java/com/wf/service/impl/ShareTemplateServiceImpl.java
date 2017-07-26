package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.DateTools;
import com.wf.bean.PageList;
import com.wf.bean.ShareTemplate;
import com.wf.dao.ShareTemplateMapper;
import com.wf.service.ShareTemplateService;
@Service
public class ShareTemplateServiceImpl implements ShareTemplateService {
	@Autowired
	ShareTemplateMapper dao;
	@Override
	public Boolean addShareTemplate(ShareTemplate shareTemplate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		shareTemplate.setShareTime(sdf.format(new Date()));
		boolean bol=dao.findShareTemplateParam(shareTemplate.getShareType())==null?true:false;
		boolean n=false;
		if(bol==true){
			n=dao.insertShareTemplate(shareTemplate)>0?true:false;
		}
		boolean b=bol && n;
		return b;
	}

	@Override
	public PageList getShareTemplate(int pageNum, int pageSize, String shareType) {
		if(StringUtils.isEmpty(shareType)) shareType=null;
		PageList p=new PageList();
		Map<String,Object> mp=new HashMap<String, Object>();
		int pageN=(pageNum-1)*pageSize;
		mp.put("pageNum", pageN);
		mp.put("pageSize", pageSize);
		mp.put("shareType", shareType);
		Map<String,Object> mpPara=new HashMap<String, Object>();
		mpPara.put("shareType", shareType);
		List<Object>  pageRowAll=dao.selectShareTemplateInforAll(mpPara);
		int pageTotal=0;
		int b =pageRowAll.size()%pageSize;
		pageTotal=pageRowAll.size()!=0 && b !=0?pageRowAll.size()/pageSize+1:pageRowAll.size()/pageSize;
		List<Object>  pageRow=dao.selectShareTemplateInfor(mp);
		p.setPageNum(pageNum);
		p.setPageRow(pageRow);
		p.setPageSize(pageSize);
		p.setPageTotal(pageTotal);
		return p;
	}

	@Override
	public ShareTemplate findShareTemplate(String id) {
		ShareTemplate shareTemplate =dao.findShareTemplate(id);
		return shareTemplate;
	}

	@Override
	public Boolean deleteShareTemplate(String ids) {
		int n=dao.deleteShareTemplate(ids);
		boolean b=n>0?true:false;
		return b;
	}

	@Override
	public Boolean updateShareTemplate(ShareTemplate shareTemplate) {
		shareTemplate.setShareTime(DateTools.getSysDate());
		/*boolean bol=dao.findShareTemplateParamToUpdate(shareTemplate)==null?true:false;
		boolean n=false;
		if(bol==true){
			 n =dao.updateShareTemplate(shareTemplate)>0?true:false;
		}
		boolean b=bol && n;*/
		boolean n =dao.updateShareTemplate(shareTemplate)>0?true:false;
		return n;
	}
	/**
	 * 查询所有
	 * @return
	 */
	@Override
	public List<ShareTemplate> selectAll() {
		List<ShareTemplate> list = dao.selectAll();
		return list;
	}

	@Override
	public List<Object> exportshareTemplate(String shareType) {
	
		List<Object> list=new ArrayList<Object>();
		try {
			
			if(StringUtils.isEmpty(shareType)) shareType=null;
			Map<String,Object> mpPara=new HashMap<String, Object>();
			mpPara.put("shareType", shareType);
			list=dao.selectShareTemplateInforAll(mpPara);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public ShareTemplate checkShareTemplate(ShareTemplate shareTemplate,String addOrUpdate) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("id",shareTemplate.getId());
		map.put("shareType",shareTemplate.getShareType());
		map.put("addOrUpdate",addOrUpdate);
		return dao.findShareTemplateParamToUpdate(map);
	}

}
