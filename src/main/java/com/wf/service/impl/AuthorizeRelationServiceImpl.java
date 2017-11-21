package com.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wf.bean.Authorize;
import com.wf.bean.AuthorizeRelation;
import com.wf.bean.MatrixLiterature;
import com.wf.bean.PSubjectCategory;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.ProResourceType;
import com.wf.bean.Provider;
import com.wf.dao.AuthorizeMapper;
import com.wf.dao.AuthorizeRelationMapper;
import com.wf.dao.MatrixLiteratureMapper;
import com.wf.dao.PSubjectCategoryMapper;
import com.wf.dao.PersonMapper;
import com.wf.dao.ProResourceTypeMapper;
import com.wf.dao.ProviderMapper;
import com.wf.service.AuthorizeRelationService;
import com.wf.service.AuthorizeService;
import com.wf.service.PersonService;

@Service
public class AuthorizeRelationServiceImpl implements AuthorizeRelationService {

	@Autowired
	private AuthorizeRelationMapper authorizeRelation;
	@Autowired
	private ProviderMapper provider;   //资源提供商 接口
	@Autowired
	private PersonMapper persondao;
	@Autowired
	private PersonService personService;
	@Autowired
	private AuthorizeService authorizeService;
	@Autowired
	private AuthorizeMapper authorize;
	@Autowired
	private PersonMapper personMapper;
	@Autowired
	private MatrixLiteratureMapper matrixLiteratureMapper;
	@Autowired
	private PSubjectCategoryMapper pSubjectCategoryMapper;
	@Autowired
	private ProResourceTypeMapper proResourceTypeDao;
	@Override
	public PageList getAuthorizeRelation(AuthorizeRelation ar,Integer pagenum, Integer pagesize,String startDate,String endDate) {
		List<String> mlIds=null;
		if((startDate!=null && !"".equals(startDate))||(endDate!=null && !"".equals(endDate))){
			mlIds=new ArrayList<String>();
			List<Object> mlList= matrixLiteratureMapper.getMatrixLiterature(ar.getProviderId(),null,ar.getProResourceId(), null, null, startDate, endDate, null);
			for(int i=0;i<mlList.size();i++){
				MatrixLiterature ml =(MatrixLiterature)mlList.get(i);
				mlIds.add(ml.getId());
			}
			if(mlList.size()<=0){
				mlIds.add("-1");
			}
		}
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			int startnum = (pagenum-1)*pagesize;
			int num =0;
			r = this.authorizeRelation.getAuthorizeRelation(ar,startnum,pagesize,mlIds);
			num= this.authorizeRelation.getAuthorizeRelationNum(ar,mlIds);
			p.setPageRow(r);
			p.setPageNum(pagenum);
			p.setPageTotal(num);
			p.setPageSize(pagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	//查询授权机构详细信息
		@Override

		public HashMap<String, Object> getgetAuthorizeRelationInfo(String institutionId) {
			// TODO Auto-generated method stub
			 HashMap<String,Object> map=new  HashMap<String,Object>();
			 Authorize authorizeByName = authorize.getAuthorizeByName(institutionId);
			 map.put("username",authorizeByName.getUsername());
			 map.put("password", authorizeByName.getPassword());
			 List<Object> list1 =new ArrayList<Object>();
			 List<AuthorizeRelation> list = authorizeRelation.getauthorizeRelationByName(institutionId);
			 if(list.size() > 0){
				 
				 map.put("detailsURL",list.get(0).getDetailsURL());
				 map.put("downloadURL",list.get(0).getDownloadURL());
			
				 for(int i=0;i<list.size();i++){
				 AuthorizeRelation relation = list.get(i);
				 
				 HashMap<String,Object> resourceMap = new HashMap<String,Object>();//资源类型map
				 HashMap<String,Object> mutiMap = new HashMap<String,Object>();//母体map
				 Provider provider2 = provider.getProvider(relation.getProviderId());
				 resourceMap.put("provider", provider2.getProviderName());
			 	 ProResourceType proResourceType = proResourceTypeDao.getProResourceType(relation.getProResourceId());//资源类型
			 	 PSubjectCategory pSubjectCategory = pSubjectCategoryMapper.getPSubjectCategoryParent(relation.getSubjectId());//学科
			 	 MatrixLiterature matrixLiterature = matrixLiteratureMapper.getMatrixLiteratureById(relation.getPeriodicalId());//母体文献
			 	 mutiMap.put("title",matrixLiterature.getTitle());
			/* mutiMap.put("providerId",matrixLiterature.getProviderId());
			 mutiMap.put("proResourceId",matrixLiterature.getProResourceId());
			 mutiMap.put("psubjectId",matrixLiterature.getPsubjectId());*/
			 	mutiMap.put("nameen",matrixLiterature.getNameen());
			 	mutiMap.put("author",matrixLiterature.getAuthor());
			 	mutiMap.put("abstracts",matrixLiterature.getAbstracts());
			 	mutiMap.put("datePeriod",matrixLiterature.getDatePeriod());
			 	mutiMap.put("cover",matrixLiterature.getCover());
			 	resourceMap.put("resourceName", proResourceType.getResourceName());
			 	resourceMap.put("pCategoryCodes",pSubjectCategory.getpCategoryCodes());
			 	resourceMap.put("pCategoryName",pSubjectCategory.getpCategoryName());
			 	resourceMap.put("MatrixLiterature",mutiMap);
			 	list1.add(resourceMap);
			 }
			 }
			 map.put("relation",list1);
			 
			return map;
		}
	//授权 关联查询 所有
	@Override
	@RequestMapping("getAuthorizeRelationfind")
	@ResponseBody
	public List<Object> getAuthorizeRelationfind(AuthorizeRelation ar){
		List<Object>  rlist = this.authorizeRelation.getAuthorizeRelation(ar,null,null,null);
		return rlist;
	}
	
	@Override
	public List<HashMap<String, Object>> getAuthorizeRelationlist(
			AuthorizeRelation ars, Integer pagenum, Integer pagesize,String startDate,String endDate) {
		PageList pl = this.getAuthorizeRelation(ars,pagenum,pagesize,startDate,endDate);
		List<Object> list =pl.getPageRow();
		List<HashMap<String, Object>> r = new ArrayList<HashMap<String, Object>>();
		LinkedHashMap<String, Object> mp = null;
		for(int i=0;i<list.size();i++){
			AuthorizeRelation ar =(AuthorizeRelation)list.get(i);
			mp = new LinkedHashMap<String, Object>();
			mp.put("pageList", pl);
			mp.put("listSize",list.size());
			mp.put("id",ar.getId());
			ProResourceType prorType= proResourceTypeDao.getProResourceType(ar.getProResourceId());
			mp.put("proResourceId", prorType.getId());
			mp.put("proResourceName",prorType.getResourceName());
			mp.put("detailsURL",ar.getDetailsURL());
			mp.put("downloadURL", ar.getDownloadURL());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userId", ar.getInstitutionId());
			PageList plist =personService.QueryIns(ar.getInstitutionId(), null, 1, 1);
			List<Object>  pObject =plist.getPageRow();
			Map<String,Object> person =(Map<String,Object>) pObject.get(0);
			mp.put("InstitutionUsername", person.get("userId"));
			mp.put("InstitutionName", person.get("institution"));
			mp.put("InstitutionId", ar.getInstitutionId());
			Provider p =null;
			if(ar.getProviderId()!=null && !"".equals(ar.getProviderId())){
				System.out.println(ar.getProviderId());
				p=provider.getProvider(ar.getProviderId());
				mp.put("providerId",p.getId());
				mp.put("providerName",p.getProviderName());
			}
			Authorize au =authorize.getAuthorize2(ar.getAuthorizeId());
			mp.put("authorizeId",ar.getAuthorizeId());
			/**学科分类       母体*/
			
			MatrixLiterature ml= matrixLiteratureMapper.getMatrixLiteratureById(ar.getPeriodicalId());
			mp.put("title",ml.getTitle());
			mp.put("periodicalId",ar.getPeriodicalId());
			mp.put("datePeriod",ml.getDatePeriod());
			mp.put("nameen",ml.getNameen());
			mp.put("author",ml.getAuthor());
			mp.put("abstracts",ml.getAbstracts());
			
			PSubjectCategory psc= pSubjectCategoryMapper.getPSubjectCategoryParent(ar.getSubjectId());
			mp.put("subjectId",ar.getSubjectId());
			mp.put("pCategoryName",psc.getpCategoryName());
			mp.put("pCategoryCodes",psc.getpCategoryCodes());
			r.add(mp);
		}
		return r;
	}


	@Override
	public AuthorizeRelation addAuthorizeRelation(Integer authorizeId,
			String institutionId, Integer providerId, Integer proResourceId,
			Integer subjectId, String periodicalId, String detailsURL,
			String downloadURL) {
		AuthorizeRelation ar =new AuthorizeRelation();
		ar.setAuthorizeId(authorizeId);
		ar.setInstitutionId(institutionId);
		ar.setProviderId(providerId);
		ar.setProResourceId(proResourceId);
		ar.setSubjectId(subjectId);
		ar.setPeriodicalId(periodicalId);
		ar.setDetailsURL(detailsURL);
		ar.setDownloadURL(downloadURL);
		int num =authorizeRelation.addAuthorizeRelation(ar);
		if(num>0){
			return ar;
		}
		return null;
	}
	
}
