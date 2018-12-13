package com.wf.service.impl;

import com.wf.bean.*;
import com.wf.dao.InformationLabelMapper;
import com.wf.service.InformationLabelService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ecs.wml.I;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.generic.BitOperations;
import wfks.accounting.transaction.SearchResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class InformationLabelImpl implements InformationLabelService {
    private static org.apache.log4j.Logger log = Logger.getLogger(InformationLabelImpl.class);
    @Autowired
    InformationLabelMapper informationLabelMapper;

    @Override
    public InformationLabelResponse insertInformationLabel(InformationLabelRequest request) {
        if (request.getLabel() == null || request.getOperator() == null || request.getOperatingTime() == null) {
            log.info("InformationLabelRequest标签信息不完整");
            throw new IllegalArgumentException("InformationLabelRequest标签信息不完整");
        }
        InformationLabel informationLabel = new InformationLabel();
        InformationLabelResponse response = new InformationLabelResponse();
        try {
            informationLabel.setOperatingTime(request.getOperatingTime());
            informationLabel.setLabel(request.getLabel());
            informationLabel.setTotalNumber(0);
            informationLabel.setOperator(request.getOperator());
            int insert = informationLabelMapper.insert(informationLabel);
            if (insert == 0) {
                //资讯标签插入失败
                log.info("资讯标签插入失败 插入label：" + request.getLabel());
                throw new RuntimeException("资讯标签插入失败！！！");
            }
            response.setInformationLabel(informationLabel);
            response.setSuccess(true);
        } catch (Exception e) {
            log.info("生成资讯标签失败 插入informationLabel：" + informationLabel, e);
            throw new RuntimeException("生成资讯标签失败出错！！！");
        }
        log.info("生成资讯标签方法结束.插入informationLabel：" + informationLabel);
        return response;
    }

    @Override
    public SearchResponse<InformationLabel> searchInformationLabel(InformationLabelSearchRequset request) {
        //初始化example，为了放置查询参数、起始的下标、偏移量
        InformationLabelExample example = new InformationLabelExample();
        //通过example创建criteria，为了放置查询条件
        InformationLabelExample.Criteria criteria = example.createCriteria();
        if (request.getLabel() != null && !"".equals(request.getLabel())) {
            String label = "%" + request.getLabel() + "%";
            criteria.andLabelLike(label);
        }
        if (request.getOperator() != null && !"".equals(request.getOperator())) {
            String operator = "%" + request.getOperator() + "%";
            criteria.andOperatorLike(operator);
        }
        if (request.getOperatingTimeStart() != null) {
            criteria.andOperatingTimeGreaterThanOrEqualTo(request.getOperatingTimeStart());
        }
        if (request.getOperatingTimeEnd() != null) {
            criteria.andOperatingTimeLessThanOrEqualTo(request.getOperatingTimeEnd());
        }
        //设置起始下标和偏移量
        example.setLimit(request.getCount());
        example.setOffset(request.getStartIndex());
        try {
            List<InformationLabel> list = informationLabelMapper.selectByExample(example);
            long count = informationLabelMapper.countByExample(example);
            return new SearchResponse<InformationLabel>(list, new Long(count).intValue());
        } catch (Exception e) {
            log.error("查询资讯标签失败，request：" + request.toString(), e);
            throw e;
        }
    }

    @Override
    public void deleteInformationLabel(List<String> ids) {
        List<Integer> list = new ArrayList<Integer>();
        CollectionUtils.collect(ids, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Integer.valueOf(o.toString());
            }
        }, list);

        try {
            InformationLabelExample example = new InformationLabelExample();
            InformationLabelExample.Criteria criteria = example.createCriteria();
            if (ids != null && ids.size() > 0) {
                criteria.andIdIn(list);
            }
            informationLabelMapper.deleteByExample(example);

        } catch (Exception e) {
            log.error("删除资讯信息失败，ids：" + ids, e);
        }
    }

    @Override
    public boolean updateInformationLabel(String id, String label) {
        log.info("修改资讯标签开始！！");
        if (label.equals("") || label == null) {
            log.info("参数为空 id：" + id);
            return false;
        }
        InformationLabel informationLabel = new InformationLabel();
        informationLabel.setLabel(label);
        informationLabel.setId(Integer.valueOf(id));
        try {
            informationLabelMapper.updateByPrimaryKeySelective(informationLabel);
            return true;
        } catch (Exception e) {
            log.info("修改资讯标签！！");
            return false;
        }
    }
}