package com.wf.service;

import java.util.List;

import com.wf.bean.CommentInfo;
import com.wf.bean.PageList;

public interface PerioCommentService {

	PageList getComment(CommentInfo info, String[] dataState,
			String[] complaintStatus, String startTime, String endTime,
			String sauditm, String eauditm, String slayoutm, String elayoutm,
			Integer pagenum, Integer pagesize);

	Boolean changetype(CommentInfo info);

	CommentInfo findNotes(CommentInfo info);

	CommentInfo findNote(CommentInfo info);
	
	CommentInfo  getcommentByid(String id);

	Boolean updateNotes(String id ,String dataState,String appealReason,String user_id,String date);

	Integer getGoodForCommont(String commontid);

	List<Object> exportPerio(CommentInfo info, String[] dataState,
			String[] complaintStatus, String startTime, String endTime,
			String sauditm, String eauditm, String slayoutm, String elayoutm);
	
	Integer updateInfo(String id,String dataState);
}
