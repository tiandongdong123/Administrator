package com.wf.service;

import java.util.List;

import com.wf.bean.Notes;
import com.wf.bean.PageList;

public interface NotesService {
	/**
	 * 笔记分页
	 * @param pageNum
	 * @param pageSize
	 * @param branch
	 * @param human
	 * @param colums
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	PageList getNotes(int pageNum,int pageSize,String userName,String noteNum,String resourceName,
			String[] resourceType,String[] dataState,String[] complaintStatus,String startTime,String endTime);
	
	Notes findNotes(String id);
	
	Notes findNoteOne(String id);
	
	Boolean updateNotes(Notes notes);
	
	Boolean closeNote(String id,String finalOpinion);
	
	Boolean openNote(String id);
	
	Boolean handlingNote(String id);
	
	List<Object> exportNotes(String userName,
			String noteNum, String resourceName,String[] resourceType,String[] dataState,
			String[] complaintStatus,String startTime,String endTime);
	
}
