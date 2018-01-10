package com.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wf.bean.Notes;
public interface NotesMapper {
	/**
	 * 笔记管理
	 * @return
	 */
	public List<Object>  selectNotesInfor(Map<String,Object> map);
	/**
	 * 查询笔记总数
	 * @param map
	 * @return
	 */
	public int selectNotesInforCount(Map<String,Object> map);
	
	public Notes  findNotes(String id);
	
	public int updateNotes(Notes notes);
	
	public int closeNote(@Param("id") String id,@Param("finalOpinion")String finalOpinion);
	
	boolean handlingNote(@Param("id") String id);
	
	public int openNote(@Param("id") String id);
	
	public Notes  topNO1(@Param("noteNum") String noteNum);
}
