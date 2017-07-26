package com.wf.dao;

import java.util.List;

import com.wf.bean.VolumeDocu;


public interface VolumeDocuMapper {
	/**
	 * 删除
	 * @param volumeId
	 * @return
	 */
    int deleteByVolume(String volumeId);
    
    /**
     * 插入
     * @param record
     * @return
     */
    int insert(List<VolumeDocu> list);
    
    /**
     * 有章节查询
     * @param chapterId
     * @return
     */
    List<Object> queryByChapterId(String chapterId);
    
    /**
     * 无章节查询
     * @param chapterId
     * @return
     */
    List<Object> queryByVolumeId(String volumeId);
    /**
     * 有章节修改
     * @param record
     * @return
     */
    int updateByChapterId(String chapterId);
    /**
     * 无章节修改
     * @param record
     * @return
     */
    int updateByVolumeId(String volumeId);
    
}