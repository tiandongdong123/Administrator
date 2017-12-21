package com.wf.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import com.xxl.conf.core.XxlConfClient;
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
		int page=(pageNum-1)*pageSize;
		mp.put("pageNum", page);
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
				String type = ((Notes)pageRow.get(i)).getResourceType();
				if("tech_result".equals(type)){
					type = "techResult";
				}else if("standards".equals(type)){
					type = "standard";
				}else if("gazetteers".equals(type)){
					type = "local chronicles";
				}
				ResourceType res=resource.getOne(type);
				if(null!=res){
					((Notes)pageRow.get(i)).setResourceType(res.getTypeName());	
				}
			}
		}
		int totalRow= dao.selectNotesInforCount(mp);
		p.setTotalRow(totalRow);
		int pageTotal= totalRow%pageSize!=0?totalRow/pageSize+1:totalRow/pageSize;
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
		Notes notesNO1 = dao.topNO1(notes.getNoteNum());
		if(notesNO1.getPerformAction() == 1){
			updateWork(notes.getNoteNum(),notes.getDataState());
		}
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
	public List<Object> exportNotes(String userName, String noteNum, String resourceName, 
			String[] resourceType, String[] dataState,String[] complaintStatus, String startTime,
			String endTime, String[] noteProperty, String[] performAction) {
		
		Map<String,Object> mp=new HashMap<String, Object>();
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
		List<Object> pageRowAll= dao.selectNotesInfor(mp);
		
		if(pageRowAll.size()>0){
			for(int i=0;i<pageRowAll.size();i++){
				String  type = ((Notes)pageRowAll.get(i)).getResourceType();
				if("tech_result".equals(type)){
					type = "techResult";
				}else if("standards".equals(type)){
					type = "standard";
				}else if("gazetteers".equals(type)){
					type = "local chronicles";
				}
				ResourceType res=resource.getOne(type);
				if(null!=res){					
					((Notes)pageRowAll.get(i)).setResourceType(res.getTypeName());
				}
				String noteDate = ((Notes)pageRowAll.get(i)).getNoteDate();
				if(noteDate.indexOf(".") != -1){
					((Notes)pageRowAll.get(i)).setNoteDate(noteDate.substring(0, noteDate.indexOf(".")));
				}
				String auditTime = ((Notes)pageRowAll.get(i)).getAuditTime();
				if(auditTime!=null && auditTime.indexOf(".") != -1){
					((Notes)pageRowAll.get(i)).setAuditTime(auditTime.substring(0, auditTime.indexOf(".")));
				}
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
	
	public boolean updateWork(String noteNum,String dataState){
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE notes SET data_state = '" + dataState + "' WHERE note_num = '" + noteNum + "'";
			con = getConnection();
			stmt = con.prepareStatement(sql);
			int num = stmt.executeUpdate();
			if(num > 0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt!=null){
					stmt.close();					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(con!=null){
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public Connection getConnection() throws Exception{
		try {
			String url = XxlConfClient.get("wf-work.jdbc.url", null);
			String username = XxlConfClient.get("wf-public.jdbc.username", null);
			String password = XxlConfClient.get("wf-public.jdbc.password", null);
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
