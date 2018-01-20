package com.wanfangdata.setting;

import java.util.Map;

public class PersonAuthorityMapping {
    private Map<String, String> authority;

    public PersonAuthorityMapping(Map<String, String> authority){
        this.authority = authority;
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

    public Map<String, String> getAuthority() {
        return authority;
    }

    public void setAuthority(Map<String, String> authority) {
        this.authority = authority;
    }
}
