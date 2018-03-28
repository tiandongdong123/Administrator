package com.utils;

/**
 * Created by 01 on 2018/1/15.
 */
public enum EnumUtil {
    allLogin("ALL_LOGIN",1),
    onlyGroup("ONLY_GROUP",2),
    lineScan("LINE_SCAN",3);


    private String name;
    private int index;

    private EnumUtil(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (EnumUtil c : EnumUtil.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
