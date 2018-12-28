package com.wf.dao;

import com.wf.bean.InformationLabel;
import com.wf.bean.InformationLabelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InformationLabelMapper {
    long countByExample(InformationLabelExample example);

    int deleteByExample(InformationLabelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(InformationLabel record);

    int insertSelective(InformationLabel record);

    List<InformationLabel> selectByExample(InformationLabelExample example);

    InformationLabel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") InformationLabel record, @Param("example") InformationLabelExample example);

    int updateByExample(@Param("record") InformationLabel record, @Param("example") InformationLabelExample example);

    int updateByPrimaryKeySelective(InformationLabel record);

    int updateByPrimaryKey(InformationLabel record);
}