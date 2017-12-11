package com.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wf.bean.CommentInfo;
import com.wf.bean.PageList;
import com.wf.dao.CommentInfoMapper;
import com.wf.service.PerioCommentService;
@Service
public class PerioCommentServiceImpl implements PerioCommentService {

	@Autowired
	private CommentInfoMapper ci;
	
	@Override
	public PageList getComment(CommentInfo info,String[]dataState,String[]complaintStatus,String startTime,String endTime
			,String sauditm,String eauditm,String slayoutm,
			String elayoutm,Integer pagenum,Integer pagesize) {
		List<Object> li = new ArrayList<Object>();
		PageList pl = new PageList();
		int count=0;
		try {
			li = ci.getComment(info,dataState,complaintStatus,startTime,endTime,sauditm,eauditm,slayoutm,elayoutm,pagenum,pagesize);
			count = ci.getCommentCount(info,dataState,complaintStatus,startTime,endTime,sauditm,eauditm,slayoutm,elayoutm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pl.setPageNum(pagenum+1);
		pl.setPageSize(pagesize);
		pl.setPageRow(li);
		pl.setPageTotal(count);
		return pl;
	}

	@Override
	public Boolean changetype(CommentInfo info) {
		// TODO Auto-generated method stub
		Boolean i=ci.changetype(info);
		return i;
	}

	@Override
	public CommentInfo findNotes(CommentInfo info) {
		// TODO Auto-generated method stub
		CommentInfo data=ci.findNotes(info);
		return data;
	}

	@Override
	public Boolean updateNotes(String id ,String dataState,String appealReason,String user_id,String date) {
		// TODO Auto-generated method stub
		Boolean i=ci.updateNotes(id, dataState, appealReason,user_id,date);
		return i;
	}

	@Override
	public CommentInfo findNote(CommentInfo info) {
		// TODO Auto-generated method stub
		CommentInfo data=ci.findNote(info);
		return data;
	}

	@Override
	public Boolean handlingStatus(CommentInfo info) {
		// TODO Auto-generated method stub
		Boolean i=ci.handlingStatus(info);
		return i;
	}

	@Override
	public List<Object> exportPerio(CommentInfo info, String[] dataState,
			String[] complaintStatus, String startTime, String endTime,
			String sauditm, String eauditm, String slayoutm, String elayoutm) {
		
		List<Object> list=new ArrayList<Object>();
		
		try {
			list=ci.getCommentAll(info, dataState, complaintStatus, startTime, endTime, sauditm, eauditm, slayoutm, elayoutm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Integer getGoodForCommont(String commontid) {
		// TODO Auto-generated method stub
		
		return ci.getGoodForCommont(commontid);
	}

	@Override
	public CommentInfo getcommentByid(String id) {
		// TODO Auto-generated method stub
		return ci.getcommentByid(id);
	}

	@Override
	public Integer updateInfo(String perioid, String dataState) {
		// TODO Auto-generated method stub
		return null;
	}

}
