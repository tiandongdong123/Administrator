package com.wf.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.wf.Setting.DatabaseConfigureSetting;
import com.wf.Setting.ResourceTypeSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.RedisUtil;
import com.wf.bean.Custom;
import com.wf.bean.DB_Source;
import com.wf.bean.Datamanager;
import com.wf.bean.Language;
import com.wf.bean.PageList;
import com.wf.bean.ResourceType;
import com.wf.dao.CustomMapper;
import com.wf.dao.DB_SourceMapper;
import com.wf.dao.DatamanagerMapper;
import com.wf.dao.LanguageMapper;
import com.wf.dao.ResourceTypeMapper;
import com.wf.service.DataManagerService;

import net.sf.json.JSONArray;

@Service
public class DataManagerServiceImpl implements DataManagerService {

	@Autowired
	private DatamanagerMapper data;
	@Autowired
	private ResourceTypeMapper type;
	@Autowired
	private LanguageMapper language;
	@Autowired
	private DB_SourceMapper db;
	@Autowired
	private CustomMapper custom;

	DatabaseConfigureSetting dbConfig = new DatabaseConfigureSetting();

	/*	@Override
        public PageList getData(String dataname,Integer pagenum,Integer pagesize) {
            List<Object>  r = new ArrayList<Object>();
            PageList p = new PageList();
            try {
                int startnum = (pagenum-1)*pagesize;
                r = this.data.getData("%"+dataname+"%",dataname,startnum,pagesize);
                if(r.size()>0){
                for(int i=0;i<r.size();i++)
                {
                    String word=((Datamanager)r.get(i)).getSourceDb();
                    String newword=null;
                    if(word!=null&&word!=""){
                    String[] str=word.split(",");
                    List<String> list = Arrays.asList(str);

                    for(int j=0;j<list.size();j++){
                        if(list.get(j)!=""&&list.get(j)!=null){
                            DB_Source dbs = db.getOneSource(list.get(j).toString());
                            if(j==0){

                                if(dbs!=null){
                                    newword = dbs.getDbSourceName();
                                }
                            }
                            else{
                                if(dbs!=null){
                                    newword = newword+","+dbs.getDbSourceName();
                                }
                            }
                        }
                    }
                    }
                    String launage=((Datamanager)r.get(i)).getLanguage();
                    String langude="";
                    if(launage!=null&&launage!=""){
                    List<String> listlan = Arrays.asList(launage.split(","));

                    if(listlan.size()>0){
                        for(int j=0;j<listlan.size();j++){
                        if(	listlan.get(j)!=null&&	listlan.get(j)!=""){
                            Language lan=this.language.getOne(listlan.get(j));
                            if(j==0){
                                langude=lan.getLanguageName();
                            }else{
                                langude=langude+","+lan.getLanguageName();
                            }
                            }
                        }
                    }
                    }
                    String resource_type=((Datamanager)r.get(i)).getResType();
                    String sources="";
                    if(resource_type!=null&&resource_type!=""){
                    List<String> listtyp = Arrays.asList(resource_type.split(","));
                    if(listtyp.size()>0){
                        for(int j=0;j<listtyp.size();j++){
                            if(listtyp.get(j)!=null&&listtyp.get(j)!=""){
                                ResourceType reso=this.type.getOne(listtyp.get(j));
                                 if(j==0){
                                     sources=reso.getTypeName();
                                 }else{
                                     sources=sources+","+reso.getTypeName();
                                 }
                                }
                            }
                    }
                    }
                    ((Datamanager)r.get(i)).setLanguage(langude);
                    ((Datamanager)r.get(i)).setResType(sources);
                    ((Datamanager)r.get(i)).setSourceDb(StringUtils.isNoneBlank(newword)==true?newword.replace("null", ""):"");
                }
                }
                int num = this.data.getDataNum(dataname);
                p.setPageRow(r);
                p.setPageNum(pagenum);
                p.setPageTotal(num);
                p.setPageSize(pagesize);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return p;

        }*/
	@Override
	public PageList getData(Integer pageNum,Integer pageSize) {
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			r = dbConfig.getDatabase();
			int pageN=(pageNum-1)*pageSize;
			ArrayList<Object> pageRow = new ArrayList<>();
			if((r.size()-pageN)>=10){
				for(int i = 0 ; i< pageSize ; i++){
					pageRow.add(r.get(i+pageN));
				}
			}else{
				for(int i = 0 ; i<(r.size()-pageN);i++){
					pageRow.add(r.get(i+pageN));
				}
			}

			if(pageRow.size()>0){
				for(int i=0;i<pageRow.size();i++)
				{
					String word=((Datamanager)pageRow.get(i)).getSourceDb();
					String newword=null;
					if(word!=null&&word!=""){
						String[] str=word.split(",");
						List<String> list = Arrays.asList(str);

						for(int j=0;j<list.size();j++){
							if(list.get(j)!=""&&list.get(j)!=null){
								DB_Source dbs = db.getOneSource(list.get(j));
								if(j==0){

									if(dbs!=null){
										newword = dbs.getDbSourceName();
									}
								}
								else{
									if(dbs!=null){
										newword = newword+","+dbs.getDbSourceName();
									}
								}
							}
						}
					}
					String launage=((Datamanager)r.get(i)).getLanguage();
					String langude="";
					if(launage!=null&&launage!=""){
						List<String> listlan = Arrays.asList(launage.split(","));

						if(listlan.size()>0){
							for(int j=0;j<listlan.size();j++){
								if(	listlan.get(j)!=null&&	listlan.get(j)!=""){
									Language lan=this.language.getOne(listlan.get(j));
									if(j==0){
										langude=lan.getLanguageName();
									}else{
										langude=langude+","+lan.getLanguageName();
									}
								}
							}
						}
					}
					String resource_type=((Datamanager)pageRow.get(i)).getResType();
					String sources="";
					if(resource_type!=null&&resource_type!=""){
						List<String> listtyp = Arrays.asList(resource_type.split(","));
						if(listtyp.size()>0){
							for(int j=0;j<listtyp.size();j++){
								if(listtyp.get(j)!=null&&listtyp.get(j)!=""){
									ResourceType reso=this.type.getOne(listtyp.get(j));
									if(j==0){
										sources=reso.getTypeName();
									}else{
										sources=sources+","+reso.getTypeName();
									}
								}
							}
						}
					}
					((Datamanager)pageRow.get(i)).setLanguage(langude);
					((Datamanager)pageRow.get(i)).setResType(sources);
					((Datamanager)pageRow.get(i)).setSourceDb(StringUtils.isNoneBlank(newword)==true?newword.replace("null", ""):"");
				}
			}
			int pageTotal=r.size()!=0 && r.size()%pageSize !=0?r.size()/pageSize+1:r.size()/pageSize;
			p.setPageRow(pageRow);
			p.setPageNum(pageNum);
			p.setPageTotal(pageTotal);
			p.setPageSize(pageSize);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	@Override
	public PageList findDatabaseByName(String dataName){
		List<Object>  r = new ArrayList<Object>();
		PageList p = new PageList();
		try {
			r = dbConfig.findDatabaseByName(dataName);
			ArrayList<Object> pageRow = new ArrayList<>();

			for(int i = 0 ; i<r.size();i++){
				pageRow.add(r.get(i));
			}
			if(pageRow.size()>0){
				for(int i=0;i<pageRow.size();i++)
				{
					String word=((Datamanager)pageRow.get(i)).getSourceDb();
					String newword=null;
					if(word!=null&&word!=""){
						String[] str=word.split(",");
						List<String> list = Arrays.asList(str);

						for(int j=0;j<list.size();j++){
							if(list.get(j)!=""&&list.get(j)!=null){
								DB_Source dbs = db.getOneSource(list.get(j));
								if(j==0){
									if(dbs!=null){
										newword = dbs.getDbSourceName();
									}
								}
								else{
									if(dbs!=null){
										newword = newword+","+dbs.getDbSourceName();
									}
								}
							}
						}
					}
					String launage=((Datamanager)r.get(i)).getLanguage();
					String langude="";
					if(launage!=null&&launage!=""){
						List<String> listlan = Arrays.asList(launage.split(","));

						if(listlan.size()>0){
							for(int j=0;j<listlan.size();j++){
								if(	listlan.get(j)!=null&&	listlan.get(j)!=""){
									Language lan=this.language.getOne(listlan.get(j));
									if(j==0){
										if(lan!=null){
											langude=lan.getLanguageName();
										}
									}else{
										langude=langude+","+lan.getLanguageName();
									}
								}
							}
						}
					}
					String resource_type=((Datamanager)pageRow.get(i)).getResType();
					String sources="";
					if(resource_type!=null&&resource_type!=""){
						List<String> listtyp = Arrays.asList(resource_type.split(","));
						if(listtyp.size()>0){
							for(int j=0;j<listtyp.size();j++){
								if(listtyp.get(j)!=null&&listtyp.get(j)!=""){
									ResourceType reso=this.type.getOne(listtyp.get(j));
									if(j==0){
										if(reso!=null){
											sources=reso.getTypeName();
										}
									}else{
										sources=sources+","+reso.getTypeName();
									}
								}
							}
						}
					}
					((Datamanager)pageRow.get(i)).setLanguage(langude);
					((Datamanager)pageRow.get(i)).setResType(sources);
					((Datamanager)pageRow.get(i)).setSourceDb(StringUtils.isNoneBlank(newword)==true?newword.replace("null", ""):"");
				}
			}
			p.setPageRow(pageRow);
			p.setPageNum(1);
			p.setPageTotal(1);
			p.setPageSize(10);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	@Override
	public  boolean moveUpDatabase(String id){
		boolean b = dbConfig.moveUpDatabase(id);
		return b;
	}
	@Override
	public  boolean moveDownDatabase(String id){
		boolean b = dbConfig.moveDownDatabase(id);
		return b;
	}
	@Override
	public boolean checkResourceForOne(String id){
		boolean result = dbConfig.checkResourceForOne(id);
		return result;
	}
	@Override
	public boolean checkStatus(String id){
		boolean result = dbConfig.checkStatus(id);
		return result;
	}
	@Override
	public boolean deleteData(String id) {
		boolean rt = false;
		int rtnum = 0;
		int cos = 0;
		try {
			//在数据库中删除
			rtnum  = this.data.deleteData(id);
			//在zookeeper中删除数据库配置
			dbConfig.deleteDatabase(id);
			cos  = this.custom.doDeleteCustom(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rtnum>0){
			rt = true;
		}
		return rt;
	}

	@Override
	public boolean releaseData(String id) {
		boolean result = false;
		int num = 0;
		int state = 1 ;
		try {
			num  = this.data.releaseData(id);
			dbConfig.updateDatabaseState(state,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			result = true;
		}
		return result;
	}
	@Override
	public boolean descendData(String id) {
		boolean result = false;
		int num = 0;
		int state = 0 ;
		try {
			num  = this.data.descendData(id);
			dbConfig.updateDatabaseState(state,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			result = true;
		}
		return result;
	}

	@Override
	public boolean closeData(String id) {
		boolean rt = false;
		int rtnum = 0;
		int status = 0 ;
		try {
			rtnum  = this.data.closeData(id);
			dbConfig.updateDatabaseStatus(status,id);
			//cos  = this.custom.doDeleteCustom(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rtnum>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public boolean openData(String id) {
		boolean rt = false;
		int rtnum = 0;
		int status = 1 ;
		try {
			rtnum  = this.data.openData(id);
			dbConfig.updateDatabaseStatus(status,id);
			//cos  = this.custom.doDeleteCustom(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rtnum>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public Map<String, Object> getResource() {
		ResourceTypeSetting resourceTypeSetting = new ResourceTypeSetting();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			List<ResourceType> listr= resourceTypeSetting.getAll();
			List<Language> lastr = this.language.getAllLanguage();
			List<DB_Source> dbstr = this.db.getAllDB();
			map.put("resourcetype", listr);
			map.put("language", lastr);
			map.put("dbsource", dbstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	@Override
	public Map<String, Object> getSubject() {
		Map<String,Object> mm = new HashMap<String,Object>();
		String val="";
		JSONArray  json = new JSONArray();
		boolean success = false;
		try {
			RedisUtil util = new RedisUtil();
			val  =util.get("CLCDic",0);
			json = JSONArray.fromObject(val);
			success =true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		mm.put("json", json);
		mm.put("success", success);
		return mm;
	}
	@Override
	public boolean checkLName(String name,String code) {
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li= this.language.checkLName(name,code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public boolean doAddLName(String name,String code) {
		Language la = new Language();
		la.setLanguageId(UUID.randomUUID().toString());
		la.setLanguageName(name);
		la.setLanguageCode(code);
		int result = 0;
		boolean re = false;
		try {
			result = this.language.doAddLName(la);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}

	@Override
	public boolean checkSName(String name,String code) {
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li= this.db.checkSName(name,code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public boolean doAddSName(String name,String code) {
		DB_Source la = new DB_Source();
		la.setDbSourceId(UUID.randomUUID().toString());
		la.setDbSourceName(name);
		la.setDbSourceCode(code);
		int result = 0;
		boolean re = false;
		try {
			result = this.db.doAddSName(la);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result>0){
			re=true;
		}
		return re;
	}
	@Override
	public boolean checkDname(String name) {
		boolean rt = false;
		List<Object> li = new ArrayList<Object>();
		try {
			li= this.data.checkDname(name);
			/*//在zookeeper中判断
			boolean result = dbConfig.checkName(name);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(li!=null&&li.size()>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public boolean doAddData(String[] customs, Datamanager data) {
		List<Custom> cs = new ArrayList<Custom>();
		boolean rt = false;
		int cus = 0;
		int dus = 0;
		String dataid = UUID.randomUUID().toString();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		data.setId(dataid);
		data.setAddTime(df.format(new Date()));
		System.out.println(df.format(new Date()));
		for(int i =0;i<customs.length;i++){
			String [] str = customs[i].split("%");
			Custom ccs = new Custom();
			ccs.setDbId(dataid);
			ccs.setId(UUID.randomUUID().toString());
			ccs.setParamOne(str[0]);
			ccs.setParamTwo(str[1]);
			ccs.setAccuracy(str[2]);
			ccs.setRelation(str[3]);
			cs.add(ccs);
		}
		try {
			if (data.getImgLogoSrc() != null && data.getImgLogoSrc() != "") {
				data.setImgLogoSrc(data.getImgLogoSrc().replace("http://www.wanfangdata.com.cn/", "${search}"));
			}
			if (data.getLink() != null && data.getLink() != "") {
				data.setLink(data.getLink().replace("http://www.wanfangdata.com.cn/", "${search}"));
			}
			//在zookeeper中添加数据库配置
			dbConfig.addDatabase(data);
			//在数据库中添加
			dus = this.data.doAddData(data);
			cus = this.custom.doAddCustom(cs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(dus>0&&cus>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public Map<String, Object> getCheck(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Custom> cs = new ArrayList<Custom>();
		Datamanager dm = new Datamanager();
		List<Object>  data = new ArrayList<Object>();
		String[] language = new String[]{};
		String[] resourtype = new String[]{};
		String[] source = new String[]{};
		try {
			cs = this.custom.getCustomById(id);
			dm = this.data.getDataManagerById(id);
			if (dm != null) {
				String lang = dm.getLanguage();
				String sour = dm.getSourceDb();
				String resour = dm.getResType();
				if (lang != null && lang != "") {
					language = lang.split(",");
				}
				if (sour != null && sour != "") {
					source = sour.split(",");
				}
				if (resour != null && resour != "") {
					resourtype = resour.split(",");
				}
			}
			data = dbConfig.findDatabaseByName(dm.getTableName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("checklanguage", language);
		map.put("checksource", source);
		map.put("checkresourcetype", resourtype);
		map.put("dm", dm);
		map.put("cs", cs);
		map.put("dn",data);
		return map;
	}
	@Override
	public List<String> getCheckids(String id) {
		Datamanager dm = new Datamanager();
		String[] treeids  = new String[]{};
		List<String> ids  = new ArrayList<String>();
		try {
			dm = this.data.getDataManagerById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}
	@Override
	public boolean doUpdateData(String[] customs, Datamanager data) {
		List<Custom> cs = new ArrayList<Custom>();
		boolean rt = false;
		int cus = 0;
		int dus = 0;
		int del = 0;
		for(int i =0;i<customs.length;i++){
			String [] str = customs[i].split("%");
			Custom ccs = new Custom();
			ccs.setDbId(data.getId());
			ccs.setId(UUID.randomUUID().toString());
			ccs.setParamOne(str[0]);
			ccs.setParamTwo(str[1]);
			ccs.setAccuracy(str[2]);
			ccs.setRelation(str[3]);
			cs.add(ccs);
		}
		try {
			if (data.getImgLogoSrc() != null && data.getImgLogoSrc() != "") {
				data.setImgLogoSrc(data.getImgLogoSrc().replace("http://www.wanfangdata.com.cn/", "${search}"));
			}
			if (data.getLink() != null && data.getLink() != "") {
				data.setLink(data.getLink().replace("http://www.wanfangdata.com.cn/", "${search}"));
			}
			//在zookeeper中修改数据库配置
			dbConfig.updateDatabase(data);
			//在数据库中修改
			dus = this.data.doUpdateData(data);
			del = this.custom.doDeleteCustom(data.getId());
			cus = this.custom.doAddCustom(cs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(dus>0&&cus>0&&del>0){
			rt = true;
		}
		return rt;
	}

	@Override
	public List<Datamanager> selectAll() {
		// TODO Auto-generated method stub
		List<Datamanager> list= this.data.selectAll();
		return list;
	}
	@Override
	public Datamanager getDataManagerBySourceCode(String productSourceCode) {
		// TODO Auto-generated method stub

		Datamanager data1=this.data.getDataManagerBySourceCode(productSourceCode);
		return data1;
	}
	@Override
	public void selectZY() {
		// TODO Auto-generated method stub
		JSONArray list = dbConfig.selectSitateFoOne();
		RedisUtil redis= new RedisUtil();
		redis.del("datamanager");
		redis.set("datamanager", list.toString(), 6);
	}
	@Override
	public List<Object> exportData(String dataname) {
		List<Object>  data = new ArrayList<Object>();
		if(null==dataname || "".equals(dataname)){
			data=dbConfig.getDatabase();
		}else{
			data=dbConfig.findDatabaseByName(dataname);
		}
		
		if(data.size()>0){
			for(int i=0;i<data.size();i++)
			{
				String word=((Datamanager)data.get(i)).getSourceDb();
				String newword=null;
				if(word!=null&&word!=""){
					String[] str=word.split(",");
					List<String> list = Arrays.asList(str);

					for(int j=0;j<list.size();j++){
						if(list.get(j)!=""&&list.get(j)!=null){
							DB_Source dbs = db.getOneSource(list.get(j));
							if(j==0){

								if(dbs!=null){
									newword = dbs.getDbSourceName();
								}
							}
							else{
								if(dbs!=null){
									newword = newword+","+dbs.getDbSourceName();
								}
							}
						}
					}
				}
				String launage=((Datamanager)data.get(i)).getLanguage();
				String langude="";
				if(launage!=null&&launage!=""){
					List<String> listlan = Arrays.asList(launage.split(","));

					if(listlan.size()>0){
						for(int j=0;j<listlan.size();j++){
							if(	listlan.get(j)!=null&&	listlan.get(j)!=""){
								Language lan=this.language.getOne(listlan.get(j));
								if(j==0){
									langude=lan.getLanguageName();
								}else{
									langude=langude+","+lan.getLanguageName();
								}
							}
						}
					}
				}
				String resource_type=((Datamanager)data.get(i)).getResType();
				String sources="";
				if(resource_type!=null&&resource_type!=""){
					List<String> listtyp = Arrays.asList(resource_type.split(","));
					if(listtyp.size()>0){
						for(int j=0;j<listtyp.size();j++){
							if(listtyp.get(j)!=null&&listtyp.get(j)!=""){
								ResourceType reso=this.type.getOne(listtyp.get(j));
								if(j==0){
									sources=reso.getTypeName();
								}else{
									sources=sources+","+reso.getTypeName();
								}
							}
						}
					}
				}
				((Datamanager)data.get(i)).setLanguage(langude);
				((Datamanager)data.get(i)).setResType(sources);
				((Datamanager)data.get(i)).setSourceDb(StringUtils.isNoneBlank(newword)==true?newword.replace("null", ""):"");
			}
		}
		
		return data;
	}
	
	@Override
	public List<Datamanager> getDatabaseByCode(String code) {
		return data.getDatabaseByCode(code);
	}
	

}
