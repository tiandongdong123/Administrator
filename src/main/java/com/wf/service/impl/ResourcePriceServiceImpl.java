package com.wf.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.DB_Source;
import com.wf.bean.Datamanager;
import com.wf.bean.PageList;
import com.wf.bean.PriceUnit;
import com.wf.bean.ProductType;
import com.wf.bean.ResourcePrice;
import com.wf.bean.ResourceType;
import com.wf.bean.SonSystem;
import com.wf.dao.DB_SourceMapper;
import com.wf.dao.PriceUnitMapper;
import com.wf.dao.ProductTypeMapper;
import com.wf.dao.ResourcePriceMapper;
import com.wf.dao.ResourceTypeMapper;
import com.wf.service.DataManagerService;
import com.wf.service.ResourcePriceService;
import com.wf.service.SonSystemService;

@Service
public class ResourcePriceServiceImpl implements ResourcePriceService {

	@Autowired
	private ResourcePriceMapper price;
	@Autowired
	private ResourceTypeMapper type;
	@Autowired
	private PriceUnitMapper unit;
	@Autowired
	private ProductTypeMapper product;
	@Autowired
	private DB_SourceMapper db;
	@Autowired
	SonSystemService son;
	@Autowired
	DataManagerService data;
	@Override
	public PageList getPrice(String  name,Integer pagesize,Integer pagenum) {
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			int startnum = (pagenum-1)*pagesize;
			r = this.price.getPrice(name,startnum,pagesize);
			for(int i=0;i<r.size();i++)
			{	
				ProductType oneP=product.getOneProduct(((ResourcePrice)r.get(i)).getResourceTypeCode());
				PriceUnit  oneU=unit.getOneUnit(((ResourcePrice)r.get(i)).getUnitCode());
				Datamanager oneD=data.getDataManagerBySourceCode(((ResourcePrice)r.get(i)).getSourceCode());
				SonSystem oneS=son.getOneSon(((ResourcePrice)r.get(i)).getSonCode());
				if(oneP!=null){					
					((ResourcePrice)r.get(i)).setResourceTypeCode(oneP.getResourceTypeName());
				}
				if(oneU!=null){									
					((ResourcePrice)r.get(i)).setUnitCode(oneU.getUnitName());
				}
				if(oneD!=null){					
					((ResourcePrice)r.get(i)).setSourceCode(oneD.getTableName());
				}
				if(oneS!=null){					
					((ResourcePrice)r.get(i)).setSonCode(oneS.getSonName());	
				}
			}
			int num = this.price.getPriceNum(name);
			p.setPageRow(r);
			p.setPageNum(pagenum);
			p.setPageTotal(num);
			p.setPageSize(pagesize);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return p;
	}

	@Override
	public boolean delectPrice(String[] ids) {
		int result = 0;
		boolean re = false;
		try {
			 result = this.price.deletePrice(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public List<ResourceType> getRtype() {
		List<ResourceType> rt = new ArrayList<ResourceType>();
		try {
			rt = this.type.getRtype();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}

	@Override
	public Map<String, Object> getResource() {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<Object> listu = this.unit.getRunitName();	
			List<DB_Source> dbstr = this.db.getAllDB();
			List<SonSystem> lists=this.son.getAll();
			map.put("Runitname", listu);
			map.put("dbsource", dbstr);
			map.put("son", lists);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public boolean checkPriceRID(String name, String rid) {
		List<ResourcePrice> li = new ArrayList<ResourcePrice>();
		boolean re = false;
		try {
			li = this.price.checkPriceRid(name, rid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li.size()>0){
			re=true;
		}
		return re;
	}

	@Override
	public boolean doAddPrice(ResourcePrice rp) {
		int result = 0;
		boolean re = false;
		try {
			 result = this.price.doAddPrice(rp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public Map<String,Object> getRP(String  id) {
		ResourcePrice rp = new ResourcePrice();
		rp=this.price.getRP(id);
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<SonSystem> listS=this.son.getAll();
			SonSystem son=this.son.getOneSon(rp.getSonCode());
			List<ProductType> listr= this.product.getByCode(rp.getSourceCode());
			List<Datamanager> listD=new ArrayList<Datamanager>();
			List<String> list = Arrays.asList(son.getProductResourceCode().split(",")); 
			for(int j=0;j<list.size();j++){
				listD.add(this.data.getDataManagerBySourceCode(list.get(j).toString()));
			}
			List<Object> listu = this.unit.getRunitName();	
			map.put("Rname", listr);
			map.put("Runitname", listu);
			map.put("Data", listD);
			map.put("Son", listS);
			map.put("resourceprice", rp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public boolean doUpdatePrice(ResourcePrice rp) {
		int result = 0;
		boolean re = false;
		try {
			 result = this.price.doUpdatePrice(rp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}
}
