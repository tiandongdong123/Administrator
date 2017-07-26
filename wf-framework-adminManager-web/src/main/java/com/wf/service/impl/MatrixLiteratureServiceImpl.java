package com.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utils.GetUuid;
import com.wf.bean.MatrixLiterature;
import com.wf.bean.PSubjectCategory;
import com.wf.bean.PageList;
import com.wf.bean.ProResourceType;
import com.wf.bean.Provider;
import com.wf.dao.AuthorizeRelationMapper;
import com.wf.dao.MatrixLiteratureMapper;
import com.wf.dao.PSubjectCategoryMapper;
import com.wf.dao.ProResourceTypeMapper;
import com.wf.dao.ProviderMapper;
import com.wf.service.MatrixLiteratureService;

@Service
public class MatrixLiteratureServiceImpl implements MatrixLiteratureService {

	@Autowired
	private MatrixLiteratureMapper matrixLiterature;  //母体文献查询
	@Autowired
	private ProviderMapper provider;   //资源提供商 接口
	@Autowired
	private PSubjectCategoryMapper psubject; //学科分类接口
	@Autowired
	private ProResourceTypeMapper resourceType;
	@Autowired
	private AuthorizeRelationMapper relationMapper;//授权关联表接口
	
	@Override
	public PageList getMatrixLiterature(Integer providerId,Integer psubjectId,Integer proResourceId, Integer pagenum, Integer pagesize,String startDate,String endDate,List<String> unEqualId) {
		PageList p = new PageList();
		try {
			int startnum = (pagenum-1)*pagesize;
			List<Object> mlist =matrixLiterature.getMatrixLiterature(providerId, psubjectId,proResourceId, startnum, pagesize,startDate,endDate,unEqualId);
			int num = matrixLiterature.getMatrixLiteratureNum(providerId, psubjectId,proResourceId,startDate,endDate,unEqualId);
			p.setPageRow(mlist);
			p.setPageNum(pagenum);
			p.setTotalRow(num);
			p.setPageTotal(num);
			p.setPageSize(pagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	
	@Override
	public List<HashMap<String, Object>> getMatrixLiteratureList(Integer providerId,Integer psubjectId,Integer proResourceId, Integer pagenum, Integer pagesize,String startDate,String endDate,List<String> unEqualId) {
		PageList pl = this.getMatrixLiterature(providerId, psubjectId,proResourceId, pagenum, pagesize,startDate,endDate,unEqualId);
		List<Object> list =pl.getPageRow();
		List<HashMap<String, Object>> r = new ArrayList<HashMap<String, Object>>();
		LinkedHashMap<String, Object> mp = null;
		for(int i=0;i<list.size();i++){
			MatrixLiterature mal =(MatrixLiterature)list.get(i);
			mp = new LinkedHashMap<String, Object>();
			mp.put("pageList", pl);
			mp.put("listSize",list.size());
			mp.put("id", mal.getId());
			mp.put("title", mal.getTitle());
			mp.put("nameen", mal.getNameen());
			mp.put("author", mal.getAuthor());
			mp.put("abstracts", mal.getAbstracts());
			mp.put("datePeriod",mal.getDatePeriod());
			mp.put("cover",mal.getCover());
			Provider p =null;
			if(mal.getProviderId()!=null && !"".equals(mal.getProviderId())){
				p=provider.getProvider(mal.getProviderId());
				mp.put("providerId",p.getId());
				mp.put("providerName",p.getProviderName());
			}
			PSubjectCategory psc =null;
			if(mal.getPsubjectId()!=null && !"".equals(mal.getPsubjectId())){
				psc=psubject.getPSubjectCategoryParent(mal.getPsubjectId());
				mp.put("psubjectId",psc.getId());
				mp.put("categoryName", psc.getpCategoryName());
				mp.put("categoryCodes", psc.getpCategoryCodes());
			}
			ProResourceType prt =null;
			if(mal.getProResourceId() !=null && !"".equals(mal.getProResourceId())){
				prt =resourceType.getProResourceType(mal.getProResourceId());//资源类型
				mp.put("proResourceId",prt.getId());//资源类型id
				mp.put("resourceName",prt.getResourceName());//资源类型name
			}
			r.add(mp);
		}
		return r;
	}
	
	/****
	 * 
	 * 添加母体
	 * */
	@Override
	public int addMatrixLiterature(Integer providerId, Integer psubjectId,
			Integer proResourceId,String title, String nameen, String author, String abstracts,
			String datePeriod, String cover) {
		// TODO Auto-generated method stub
		int i = -1;
		i = matrixLiterature.addMatrixLiterature(GetUuid.getId(),providerId, psubjectId, proResourceId, title, nameen, author, abstracts, datePeriod, cover);
		return i;
	}

	/**
	 * 修改母体
	 * */
	@Override
	public boolean updateMatrixLiterature(Integer providerId,
			Integer psubjectId,Integer proResourceId, String title, String nameen, String author,
			String abstracts, String datePeriod, String cover, String id) {
		// TODO Auto-generated method stub
		boolean b= false;
		b = matrixLiterature.updateMatrixLiterature(providerId, psubjectId, proResourceId, title, nameen, author, abstracts, datePeriod, cover, id);
		return b;
	}

	/**
	 * 删除母体
	 * */
	@Override
	public boolean deleteMatrixLiterature(String id) {
		// TODO Auto-generated method stub
		String periodicalId = id;
		int i = relationMapper.getAuthorizeRelations(periodicalId);
		if(i > 0){
			return false;
		}else{
		 boolean bl = matrixLiterature.deleteMatrixLiterature(id);
		return bl;
		}
	}


	@Override/** 查询母体  带学科分类名、修改时用*/
	public HashMap<String, Object> getMatrixLiteratureById(String id) {
		// TODO Auto-generated method stub
		HashMap<String,Object> map =new LinkedHashMap<String,Object>();
		MatrixLiterature ml = matrixLiterature.getMatrixLiteratureById(id);
		PSubjectCategory psc = psubject.getPSubjectCategoryParent(ml.getPsubjectId());
		map.put("muti", ml);
		map.put("xueke", psc);
		return map;
	}

	
	
}
