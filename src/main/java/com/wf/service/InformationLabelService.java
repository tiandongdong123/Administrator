package com.wf.service;

import com.wf.bean.*;
import org.apache.ecs.wml.B;
import org.apache.ibatis.annotations.Param;
import wfks.accounting.transaction.SearchResponse;

import java.util.List;


public interface InformationLabelService {
    /**
     * 生成资讯标签
     * @param informationLabelRequest
     * @return
     */
    InformationLabelResponse insertInformationLabel(InformationLabelRequest informationLabelRequest);

    /**
     * 查询资讯标签信息
     * @param request
     * @return
     */
    SearchResponse<InformationLabel> searchInformationLabel(InformationLabelSearchRequset request);

    /**
     * 删除资讯标签信息
     * @param ids
     * @return
     */
    void deleteInformationLabel(List<String> ids);

    /**
     * 修改资讯标签
     * @param informationLabelRequest
     * @return
     */
    boolean updateInformationLabel(InformationLabelRequest informationLabelRequest);

    /**
     * 资讯标签精确查询，用于添加标签及修改标签
     * @param request
     * @return
     */
    SearchResponse<InformationLabel> searchOnlyInformationLabel(InformationLabelSearchRequset request);

    /**
     * 查询标签名称（按照标签引用量倒序排序），也可用于标签名称查重：传递labelId即排除此id所对应的标签
     * @param labelId
     * @param labelName
     * @return
     */
    List<InformationLabel> selectLabelName(@Param("labelId") String labelId, @Param("labelName") String labelName);

    /**
     * 获取被引前十的标签名
     * @param labelName
     * @return
     */
    List<String> echoInformationLabel(@Param("labelName") String labelName);

    /**
     * 标签被引量自加1
     * @param labelName
     * @return
     */
    Boolean updateInformationLabelNumber(@Param("labelName") String labelName);

    /**
     * 标签被引量自减1
     * @param labelName
     * @return
     */
    Boolean updateInformationLabelNumberDel(@Param("labelName") String labelName);
}
