package com.utils;
public enum WebService {
	OK("创建成功", 1), 
	ERROR0("未知错误", 0), 
	ERROR_1("创建失败", -1), 
	ERROR_9("令牌校验错误", -9), 
	ERROR_1001("用户已存在", -1001), 
	ERROR_1002("时间范围未被接受(指可能范围过大，不被接受)", -1002), 
	ERROR_1003("IP范围未被接受",-1003), 
	ERROR_1004("分类范围未被接受", -1004), 
	ERROR_1005("单位名称未被接受", -1005), 
	ERROR_1006("授权阅读打印未被接受", -1006), 
	ERROR_1007("在线用户数未被接受", -1007), 
	ERROR_1008("副本数未被接受", -1008), 
	ERROR_1009("打印总份数未被接受", -1009), 
	ERROR_1010("单标准打印数未被接受", -1010);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private WebService(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (WebService c : WebService.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
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