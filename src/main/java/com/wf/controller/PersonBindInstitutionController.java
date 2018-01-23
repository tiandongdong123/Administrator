package com.wf.controller;

import com.wanfangdata.grpcchannel.BindAuthorityChannel;
import com.wanfangdata.rpc.bindauthority.*;
import com.wanfangdata.setting.PersonAuthorityMapping;
import com.wf.bean.BindAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wfks.authentication.account.userinfo.UserInfoDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 01 on 2018/1/18.
 */

/**
 * 个人绑定机构信息管理及权限设置Controller
 */
@Controller
@RequestMapping("/bindAuhtority")
public class PersonBindInstitutionController {

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private BindAuthorityChannel bindAuthorityChannel;
    @Autowired
    private PersonAuthorityMapping personAuthorityMapping;
    
    @RequestMapping("/userId")
    public List<String> getUserIdByInstitutionName(String institutionName){
        List<String> userIdList = userInfoDao.getUserIdByInstitutionName(institutionName);

        List<String> userIds = new ArrayList<>();
        for (String userId : userIdList) {
            SearchAccountAuthorityRequest.Builder request = SearchAccountAuthorityRequest.newBuilder().setUserId(userId);
            SearchAccountAuthorityResponse response = bindAuthorityChannel.getBlockingStub().searchAccountAuthority(request.build());
            List<AccountAuthority> accountList = response.getItemsList();
            if (accountList!=null&&accountList.size()>0){
                userIds.add(userId);
            }
        }
        return userIds;
    }
    @RequestMapping("/openAuthority")
    public String openAuthority(BindAuthority bindAuthority){
        String[] userIds = bindAuthority.getUserId().split(",");
        String[] authoritys = bindAuthority.getBindAuthority().split(",");
        List<String> authorityList = new ArrayList<>();
        for (String authority : authoritys) {
            authorityList.add(personAuthorityMapping.getAuthorityName(authority));
        }
        OpenBindRequest.Builder request = OpenBindRequest.newBuilder().addAllUserId(Arrays.asList(userIds))
                .setBindType(OpenBindRequest.BindType.forNumber(bindAuthority.getBindType()))
                .setBindLimit(bindAuthority.getBindLimit())
                .setBindValidity(bindAuthority.getBindValidity())
                .setDownloadLimit(bindAuthority.getDownlaodLimit())
                .addAllBindAuthority(authorityList);
        bindAuthorityChannel.getBlockingStub().openBindAuthority(request.build());
        return "/page/usermanager/user_binding_manager";
    }

   /* public  List<String> seachBindInfo(String userId,String Institution,String startTime,String endTime){
        if(userId!=null&&!"".equals(userId)){

        }
    }*/
}
