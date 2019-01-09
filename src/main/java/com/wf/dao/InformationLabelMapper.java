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

    //查询标签名称（按照标签引用量倒序排序），也可用于标签名称查重
    List<InformationLabel> selectLabelName(@Param("labelId") String labelId,@Param("labelName") String labelName);

    //获取被引前十的标签名
    List<String> echoInformationLabel(@Param("labelName") String labelName);

    //标签被引量自加1
    Boolean updateInformationLabelNumber(@Param("labelName") String labelName);

    //标签被引量自减1
    Boolean updateInformationLabelNumberDel(@Param("labelName") String labelName);
}