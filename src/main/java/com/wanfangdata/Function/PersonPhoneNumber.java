package com.wanfangdata.Function;

import com.wf.dao.PersonMapper;
import org.apache.log4j.Logger;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 获取个人手机号自定义方法
 */

@Service
public class PersonPhoneNumber implements Function {

    @Autowired
    private PersonMapper personMapper;

    private static Logger log = Logger.getLogger(PersonPhoneNumber.class);

    @Override
    public Object call(Object[] objects, Context context) {
        String userId = (String) objects[0];
        try {
            String institutionName = personMapper.getInstitutionByUserId(userId);
            if (institutionName!=null){
                return institutionName;
            }else {
                return "";
            }

        }catch (Exception e){
            log.error("查询个人手机号失败，个人ID"+userId);
            return "";
        }
    }
}
