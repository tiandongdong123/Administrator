package com.wf.dao;

import java.util.List;

import com.wf.bean.VolumeChapter;


public interface VolumeChapterMapper {
	/**
	 * 删除文辑时删除章
	 * @param volumeId
	 * @return
	 */
    int delete(String volumeId);
    /**
     * 插入
     * @param record
     * @return
     */
    int insert(List<VolumeChapter> list);
    /**
     * 查询
     * @param volumeId
     * @return
     */
    List<Object> query(String volumeId);
    
    /**
     * 详情页查询章节
     * @param volumeId
     * @return
     */
    List<Object> queryChapter(String volumeId);
    /**
     * 修改
     * @param record
     * @return
     */
    int update(VolumeChapter record);
}