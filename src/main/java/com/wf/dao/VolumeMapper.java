package com.wf.dao;

import java.util.List;
import java.util.Map;

import com.wf.bean.Volume;

public interface VolumeMapper {
	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
    int deleteById(String id);
    /**
     * 插入
     * @param volume
     * @return
     */
    int insert(Volume volume);
    /**
     * 查询所有
     * @param map
     * @return
     */
    List<Object> queryAll(Map<String,Object> map);
    /**
     * 分页查询
     * @param map
     * @return
     */
    List<Object> queryByPage(Map<String,Object> map);
    /**
     * 修改
     * @param id
     * @return
     */
    int updateById(String id);
    /**
	 * 推优
	 */
    int updateVolumeType(Map<String,Object> map);
    /**
	 * 发布
	 */
	int updateIssue(Map<String,Object> map);
    /**
	 * 详情
	 * @param id
	 * @return
	 */
    Volume queryDetails(String id);
    /**
     * 修改价格
     * @param map
     * @return
     */
    int updatePrice(Map<String,Object> map);
    /**
     * 修改文辑
     */
    int updateVolume(Volume volume);
    /**
     * 查询文辑的最大编号
     * @param letter (优选O/用户U)
     * @return
     */
    String queryMaxVolumeNo(String letter);
}