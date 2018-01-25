package com.wanfangdata.Function;

import com.wf.dao.PersonMapper;
import org.apache.log4j.Logger;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 获取机构名称自定义方法
 */

@Service
public class InstitutionName implements Function{

    @Autowired
    private PersonMapper personMapper;

    private static Logger log = Logger.getLogger(InstitutionName.class);
    @Override
    public Object call(Object[] objects, Context context) {
        String userId = (String) objects[0];
        try {
            String institutionName = personMapper.getInstitutionByUserId(userId);
            return institutionName;
        }catch (Exception e){
            log.error("查询机构名称失败，机构ID:"+userId);
            throw e;
        }
    }
}
