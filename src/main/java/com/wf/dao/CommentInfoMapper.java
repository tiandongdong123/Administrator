package com.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.CommentInfo;


public interface CommentInfoMapper {

	List<Object> getComment(@Param("info")CommentInfo info,@Param("dataState")String[]dataState,@Param("complaintStatus")String[]complaintStatus,@Param("startTime")String startTime,@Param("endTime")String endTime,
			@Param("sauditm")String sauditm,@Param("eauditm")String eauditm,@Param("slayoutm")String slayoutm,
			@Param("elayoutm")String elayoutm,@Param("pagenum")Integer pagenum,@Param("pagesize")Integer pagesize);
	
	Integer getCommentCount(@Param("info")CommentInfo info,@Param("dataState")String[]dataState,@Param("complaintStatus")String[]complaintStatus,@Param("startTime")String startTime,@Param("endTime")String endTime,
			@Param("sauditm")String sauditm,@Param("eauditm")String eauditm,@Param("slayoutm")String slayoutm,
			@Param("elayoutm")String elayoutm);
	
	
	List<Object> getCommentAll(@Param("info")CommentInfo info,@Param("dataState")String[]dataState,@Param("complaintStatus")String[]complaintStatus,@Param("startTime")String startTime,@Param("endTime")String endTime,
			@Param("sauditm")String sauditm,@Param("eauditm")String eauditm,@Param("slayoutm")String slayoutm,
			@Param("elayoutm")String elayoutm);
	
	Boolean changetype(@Param("info")CommentInfo info);
	
	CommentInfo findNotes(@Param("info")CommentInfo info);
	
	CommentInfo findNote(@Param("info")CommentInfo info);
	
	CommentInfo getcommentByid(@Param("id") String id);

	Boolean updateNotes(@Param("id")String id ,@Param("dataState")String dataState,@Param("appealReason")String appealReason,@Param("user_id")String user_id,@Param("date")String date);
	
	Boolean handlingStatus(@Param("info")CommentInfo info);
	
	Integer getGoodForCommont(@Param("id") String commontid);
}