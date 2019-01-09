package com.utils;

import com.wanfangdata.model.LabelListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯标签工具类
 */
public class MessageLabelUtil {

    /**
     *
     * @param split1 历史标签数组
     * @param split2 修改后的标签数组
     * @return
     */
    public static LabelListModel getChangeMessageLabel(String[] split1,String[] split2){
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        //需要增加1的集合
        List<String> add = new ArrayList<>();
        //需要减去1的集合
        List<String> del = new ArrayList<>();

        StringBuffer addStringBuffer = new StringBuffer();
        StringBuffer delStringBuffer = new StringBuffer();
        for (String s : split1) {
            list1.add(s);
        }
        for (String s : split2) {
            list2.add(s);
        }
        for (String s : list1) {
            if (!list2.contains(s)){
                del.add(s);
            }
        }
        for (String s : list2) {
            if (!list1.contains(s)){
                add.add(s);
            }
        }
        for (String s : add) {
            addStringBuffer.append(s);
        }
        for (String s : del) {
            delStringBuffer.append(s);
        }
        LabelListModel model = new LabelListModel();
        model.setAddOneStr(addStringBuffer.toString());
        model.setDelOneStr(delStringBuffer.toString());
        return model;
    }
}
