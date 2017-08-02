package com.wf.service;

import java.util.List;
import java.util.Map;

import com.wf.bean.PageList;
import com.wf.bean.Volume;

public interface VolumeService {
	/**
	 * 添加文辑
	 * @param session
	 * @param volume
	 * @param volumeState  文辑内容
	 * @return
	 */
	Map<String,Object> insert(String publishPerson,Volume volume,String volumeType,String listContent);
	/**
	 * 查询
	 * @param startTime
	 * @param endTime
	 * @param searchWord
	 * @param volumeType
	 * @param volumeState
	 * @return
	 */
	PageList queryList(String startTime,String endTime,String searchWord,String volumeType,String volumeState,String sortColumn,String sortWay,int pageNum,int pageSize); 
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	boolean delete(String id);
	/**
	 * 推优
	 * @param subject
	 * @param price
	 * @return
	 */
	boolean updateVolumeType(String id,String subject,String subjectName,double price);
	/**
	 * 发布
	 * @return
	 */
	boolean updateIssue(String id,String issue);
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	Map<String, Object> queryDetails(String id);
	/**
	 * 修改价格
	 */
	boolean updatePrice(String price,String volumeId);
	/**
	 * 修改文辑
	 */
	boolean updateVolume(Volume volume,String listContent);
	
	List<Object> exportVolumeDocu(String startTime, String endTime,
			String searchWord, String volumeType, String volumeState,
			String sortColumn,String sortWay);
	
}
