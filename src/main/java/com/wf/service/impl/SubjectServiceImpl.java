package com.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.RedisUtil;
import com.wf.bean.PageList;
import com.wf.bean.Subject;
import com.wf.dao.SubjectMapper;
import com.wf.service.SubjectService;
@Service
public class SubjectServiceImpl implements SubjectService {
	@Autowired
	SubjectMapper dao;
	RedisUtil redis = new RedisUtil();
	@Override
	public PageList getSubject(int pageNum,int pageSize,String level,String classNum,String className) {
		if(StringUtils.isEmpty(level)) level=null;
		if(StringUtils.isEmpty(classNum)) classNum=null;
		if(StringUtils.isEmpty(className)) className=null;
		PageList p=new PageList();
		Map<String,Object> mp=new HashMap<String, Object>();
		int pagen=(pageNum-1)*pageSize;
		mp.put("pageNum", pagen);
		mp.put("pageSize", pageSize);
		mp.put("level", level);
		mp.put("classNum", classNum);
		mp.put("className", className);
		List<Object> pageRow= dao.selectSubjectInfor(mp);
		Map<String,Object> mpPara=new HashMap<String, Object>();
		mpPara.put("level", level);
		mpPara.put("classNum", classNum);
		mpPara.put("className", className);
		List<Object> pageRowAll=dao.selectSubjectInforAll(mpPara);
		int pageTotal=0;
		int b =pageRowAll.size()%pageSize;
		pageTotal=pageRowAll.size()!=0 && b !=0?pageRowAll.size()/pageSize+1:pageRowAll.size()/pageSize;
		p.setPageNum(pageNum);
		p.setPageSize(pageSize);
		p.setPageRow(pageRow);
		p.setPageTotal(pageTotal);
		return p;
	}
	
	@Override
	public Boolean insertSubjects() {
		RedisUtil r = new RedisUtil();
//		学科分类1:CLCDic,2:PeriodicalSubject,3:video_subject,4:PatentIPC,5:CstadSubject,6:Standard
		String s = r.get("CstadSubject",0);
		JSONArray jsonArry = new JSONArray();
		jsonArry = JSONArray.fromObject(s);
		for (int i = 0; i < jsonArry.size(); i++) {
			JSONObject obj = jsonArry.getJSONObject(i);
			Object id = obj.get("id");
			if(id==null || StringUtils.isEmpty(id.toString())){
				continue;
				
			}
			Subject sj=new Subject();
			sj.setId(id.toString());
			sj.setClassName(obj.get("name").toString());
			sj.setClassNum(obj.get("value").toString());
			sj.setPid(obj.getString("pid").toString());
			sj.setType("5");
			dao.insertSubjects(sj);
			
		}
    	return true;

	}
	
	@Override
	public Boolean insertSubject(Subject subject) {
		boolean bol =dao.findSubjectParam(subject.getClassName())==null?true:false;
		boolean n=false;
		if(bol==true){
			n=dao.insertSubjects(subject)>0?true:false;
		}
		boolean b=bol && n;
		return b;
	}
	@Override
	public Boolean deleteSubject(String ids) {
		int n=dao.deleteSubjects(ids);
		boolean b=n>0?true:false;
		return b;
	}
	@Override
	public Subject findSubject(String id) {
		Subject subject=dao.findSubject(id);
		return subject;
	}
	@Override
	public Boolean updateSubject(Subject subject) {
		int n =dao.updateSubject(subject);
		boolean b=n>0?true:false;
		return b;
	}
	
	@Override
	public boolean subjectPublish() {
		//清空redis中对应的key
//		type="5";
		int type=7;
		for(int i=1;i<7;i++){
			redis.del("subjects"+type);
			List<Object> list= dao.find(type+"");
			JSONArray jsonArr = JSONArray.fromObject(list);
			redis.set("subjects"+type, jsonArr.toString(),1);
			
		}
//		for(int i = 0;i < list.size();i++){
//			Subject sj = (Subject) list.get(i);
//			String object = JSONObject.fromObject(sj).toString();
//			redis.zadd("pageSubject", i, sj.getId());//发布到redis
//			redis.hset("subjects", sj.getId(), object);
//			redis.set("subjects", object);
			
//		}
		
		
		return false;
	}

	@Override
	public List<Object> exportSubject(String level, String classNum,
			String className) {
		List<Object> list=new ArrayList<Object>();
		
		try {
			
			if(StringUtils.isEmpty(level)) level=null;
			if(StringUtils.isEmpty(classNum)) classNum=null;
			if(StringUtils.isEmpty(className)) className=null;
		
			Map<String,Object> mpPara=new HashMap<String, Object>();
			mpPara.put("level", level);
			mpPara.put("classNum", classNum);
			mpPara.put("className", className);
			list=dao.selectSubjectInforAll(mpPara);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
