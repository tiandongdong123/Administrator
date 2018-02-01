package com.wanfangdata.setting;

import java.util.Map;

public class BindAuthorityMapping {
    private Map<String, String> authority;

    private Map <Integer,String> bindType;

    public BindAuthorityMapping(Map<String, String> authority, Map <Integer,String> bindType){
        this.authority = authority;
        this.bindType = bindType;
    }

    /**
     * 获取权限中文名
     *
     * @param id
     * @return
     */
    public String getAuthorityName(String id) {
        return authority.containsKey(id) ? authority.get(id) : id;
    }

    public String getBindTypeName(Integer type){
        return  bindType.containsKey(type)?bindType.get(type):type.toString();
    }

    public Map<String, String> getAuthority() {
        return authority;
    }

    public void setAuthority(Map<String, String> authority) {
        this.authority = authority;
    }

    public Map<Integer, String> getBindType() {
        return bindType;
    }

    public void setBindType(Map<Integer, String> bindType) {
        this.bindType = bindType;
    }
}
