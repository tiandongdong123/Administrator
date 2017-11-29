package com.wf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.Notes;
import com.wf.bean.PageList;
import com.wf.bean.ResourceType;
import com.wf.dao.NotesMapper;
import com.wf.dao.ResourceTypeMapper;
import com.wf.service.NotesService;
@Service
public class NotesServiceImpl implements NotesService {
	@Autowired
	NotesMapper dao;
	@Autowired
	ResourceTypeMapper resource;
	@Override
	public PageList getNotes(int pageNum, int pageSize, String userName, String noteNum, String resourceName, String[] resourceType, String[] dataState, String[] complaintStatus, String startTime,String endTime, String[] noteProperty, String[] performAction) {
		PageList p=new PageList();
		Map<String,Object> mp=new HashMap<String, Object>();
		int pagen=(pageNum-1)*pageSize;
		mp.put("pageNum", pagen);
		mp.put("pageSize", pageSize);
		mp.put("userName", userName);
		mp.put("noteNum", noteNum);
		mp.put("resourceName", resourceName);
		mp.put("resourceType", resourceType);
		mp.put("dataState", dataState);
		mp.put("complaintStatus", complaintStatus);
		mp.put("startTime", startTime);
		mp.put("endTime", endTime);
		mp.put("noteProperty", noteProperty);
		mp.put("performAction", performAction);
		List<Object> pageRow= dao.selectNotesInfor(mp);
		if(pageRow.size()>0){
			for(int i=0;i<pageRow.size();i++){
				ResourceType res=resource.getOne(((Notes)pageRow.get(i)).getResourceType());
				if(null!=res){
					((Notes)pageRow.get(i)).setResourceType(res.getTypeName());	
				}
			}
		}
		Map<String,Object> mpPara=new HashMap<String, Object>();
		mpPara.put("userName", userName);
		mpPara.put("noteNum", noteNum);
		mpPara.put("resourceName", resourceName);
		mpPara.put("resourceType", resourceType);
		mpPara.put("dataState", dataState);
		mpPara.put("complaintStatus", complaintStatus);
		mpPara.put("startTime", startTime);
		mpPara.put("endTime", endTime);
		List<Object> pageRowAll= dao.selectNotesInforAll(mpPara);
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
	public Notes findNotes(String id) {
		dao.handlingNote(id);
		Notes notes =dao.findNotes(id);
		ResourceType res=resource.getOne(notes.getResourceType());
		notes.setResourceType(res.getTypeName());
		return notes;
	}
	@Override
	public Boolean updateNotes(Notes notes) {
		boolean f=dao.updateNotes(notes)>0?true:false;
		return f;
	}
	@Override
	public Boolean closeNote(String id,String finalOpinion) {
		boolean rt = false;
		int num = 0;
		try {
			num = dao.closeNote(id,finalOpinion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public Boolean openNote(String id) {
		boolean rt = false;
		int num = 0;
		try {
			num = dao.openNote(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(num>0){
			rt = true;
		}
		return rt;
	}
	@Override
	public Boolean handlingNote(String id) {
		boolean rt=dao.handlingNote(id);
		return rt;
	}
	@Override
	public List<Object> exportNotes(String userName, String noteNum,
			String resourceName, String[] resourceType, String[] dataState,
			String[] complaintStatus, String startTime,String endTime) {
		
		Map<String,Object> mp=new HashMap<String, Object>();
		mp.put("userName", userName);
		mp.put("noteNum", noteNum);
		mp.put("resourceName", resourceName);
		mp.put("resourceType", resourceType);
		mp.put("dataState", dataState);
		mp.put("complaintStatus", complaintStatus);
		mp.put("startTime", startTime);
		mp.put("endTime", endTime);
		List<Object> pageRowAll= dao.selectNotesInfor(mp);
		
		if(pageRowAll.size()>0){
			for(int i=0;i<pageRowAll.size();i++){
				ResourceType res=resource.getOne(((Notes)pageRowAll.get(i)).getResourceType());
				((Notes)pageRowAll.get(i)).setResourceType(res.getTypeName());
			}
		}
		
		return pageRowAll;
		
	}
	@Override
	public Notes findNoteOne(String id) {
		Notes notes =dao.findNotes(id);
		ResourceType res=resource.getOne(notes.getResourceType());
		notes.setResourceType(res.getTypeName());
		return notes;
	}
	

}
