package com.wf.service;

import com.wf.bean.*;
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
     *
     * @param id
     */
    boolean updateInformationLabel(String id,String label);

    /**
     * 资讯标签精确查询，用于添加标签及修改标签
     * @param request
     * @return
     */
    SearchResponse<InformationLabel> searchOnlyInformationLabel(InformationLabelSearchRequset request);
}
