package com.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.PSubjectCategory;
import com.wf.bean.ProResourceType;
import com.wf.bean.Provider;
import com.wf.dao.MatrixLiteratureMapper;
import com.wf.dao.PSubjectCategoryMapper;
import com.wf.dao.ProResourceTypeMapper;
import com.wf.dao.ProviderMapper;
import com.wf.service.PSubjectCategoryService;


@Service
public class PSubjectCategoryServiceImpl implements PSubjectCategoryService {

	@Autowired
	private PSubjectCategoryMapper pSubjectCategory; //资源提供商 的  学科分类 接口
	@Autowired
	private ProResourceTypeMapper proResourceType;//资源类型
	@Autowired
	private MatrixLiteratureMapper literatureMapper;//母体文献接口
	
	@Autowired
	private ProviderMapper provider; //资源提供商
	@Override
	public List<HashMap<String, Object>> getPSubjectCategory(Integer providerId,Integer proResourceId) {
		List<HashMap<String, Object>> r = new ArrayList<HashMap<String, Object>>();
		LinkedHashMap<String, Object> mp = null;
		List<PSubjectCategory> list =pSubjectCategory.getPSubjectCategory(providerId,proResourceId);
		for (int i = 0; i < list.size(); i++) {
			PSubjectCategory psc = list.get(i);
			mp = new LinkedHashMap<String, Object>();
			mp.put("id", psc.getId());
			mp.put("pCategoryName", psc.getpCategoryName());
			mp.put("name", psc.getpCategoryName());
			mp.put("pCategoryCodes",psc.getpCategoryCodes()); 
			if(psc.getProviderId()!=null){
				Provider prov =provider.getProvider(psc.getProviderId());
				mp.put("providerId", prov.getId());
				mp.put("ProviderName",prov.getProviderName());
			}
			if(psc.getProResourceId()!= null){
				ProResourceType prt = proResourceType.getProResourceType(psc.getProResourceId());
				mp.put("proResourceId", prt.getId());
				mp.put("proResourceName", prt.getResourceName());
			}
			if (psc.getParentId() != null && psc.getParentId()!=0) {
				PSubjectCategory p =pSubjectCategory.getPSubjectCategoryParent(psc.getParentId());
				mp.put("pId", p.getId());
			} else {
				mp.put("pId", "0");
			}
			r.add(mp);
		}
		// TODO Auto-generated method stub
		return r;
	}

	@Override/**增加学科分类*/
	public int addPSubjectCategory(Integer providerId, String pCategoryCodes,
			String pCategoryName, Integer parentId,Integer proResourceId) {
		// TODO Auto-generated method stub
		int i = -1;
		i = pSubjectCategory.addPSubjectCategory(providerId, pCategoryCodes, pCategoryName, parentId,proResourceId);
		return i;
	}
	@Override/**修改学科分类*/
	public boolean updatePSubjectCategory(Integer providerId,
			String pCategoryCodes, String pCategoryName, Integer parentId,
			Integer id,Integer proResourceId) {
		// TODO Auto-generated method stub
		boolean b = pSubjectCategory.updatePSubjectCategory(providerId, pCategoryCodes, pCategoryName, parentId, id, proResourceId);
		
		return b;
	}
	@Override/**删除学科分类*/
	public boolean deletePSubjectCategory(Integer id) {
		// TODO Auto-generated method stub
		Integer psubjectId= id;
		List<Object> list = literatureMapper.getMatrixLiterature(null, psubjectId, null, null, null, null, null, null);
		if(list.size() > 0){
			return false;
		}else{
		boolean bl = pSubjectCategory.deletePSubjectCategory(id);
		return bl;
		}
	}
	@Override
	public PSubjectCategory getPSubjectCategoryParent(Integer id) {
		// TODO Auto-generated method stub
		PSubjectCategory sub = pSubjectCategory.getPSubjectCategoryParent(id);
		Integer parentId = sub.getParentId();
		if(parentId != null && !"".equals(parentId)){
			PSubjectCategory psub = pSubjectCategory.getPSubjectCategoryParent(parentId);
			//psub.getpCategoryName();    /****************/
			sub.setParentName(psub.getpCategoryName());
		}
			return sub;
	}
	
}
