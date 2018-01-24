package com.wf.controller;

import com.google.protobuf.util.Timestamps;
import com.wanfangdata.grpcchannel.BindAccountChannel;
import com.wanfangdata.grpcchannel.BindAuthorityChannel;
import com.wanfangdata.rpc.bindauthority.*;
import com.wanfangdata.setting.PersonAuthorityMapping;
import com.wf.bean.BindAccountModel;
import com.wf.bean.BindAuthorityModel;
import com.wf.dao.PersonMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wfks.authentication.account.userinfo.UserInfoDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private BindAccountChannel bindAccountChannel;
    @Autowired
    private PersonAuthorityMapping personAuthorityMapping;
    @Autowired
    private PersonMapper personMapper;


    private static final Logger log = Logger.getLogger(PersonBindInstitutionController.class);

    @RequestMapping("/userId")
    public List<String> getUserIdByInstitutionName(String institutionName) {
        List<String> userIdList = userInfoDao.getUserIdByInstitutionName(institutionName);

        List<String> userIds = new ArrayList<>();
        for (String userId : userIdList) {
            SearchAccountAuthorityRequest.Builder request = SearchAccountAuthorityRequest.newBuilder().setUserId(userId);
            SearchAccountAuthorityResponse response = bindAuthorityChannel.getBlockingStub().searchAccountAuthority(request.build());
            List<AccountAuthority> accountList = response.getItemsList();
            if (accountList != null && accountList.size() > 0) {
                userIds.add(userId);
            }
        }
        return userIds;
    }

    @RequestMapping("/jump")
    public String jumpmethod(String page) {

        return "/page/usermanager/" + page;
    }


    @RequestMapping("/openAuthority")
    public String openAuthority(BindAuthorityModel bindAuthorityModel) {
        String[] userIds = bindAuthorityModel.getUserId().split(",");
        String[] authoritys = bindAuthorityModel.getBindAuthority().split(",");
        List<String> authorityList = new ArrayList<>();
        for (String authority : authoritys) {
            authorityList.add(personAuthorityMapping.getAuthorityName(authority));
        }
        OpenBindRequest.Builder request = OpenBindRequest.newBuilder().addAllUserId(Arrays.asList(userIds))
                .setBindType(BindType.forNumber(bindAuthorityModel.getBindType()))
                .setBindLimit(bindAuthorityModel.getBindLimit())
                .setBindValidity(bindAuthorityModel.getBindValidity())
                .setDownloadLimit(bindAuthorityModel.getDownlaodLimit())
                .addAllBindAuthority(authorityList);
        bindAuthorityChannel.getBlockingStub().openBindAuthority(request.build());
        return "/page/usermanager/user_binding_manager";
    }

    @RequestMapping("/searchBindInfo")
    public List<BindAccountModel> seachBindInfo(String userId, String Institution, Date startTime, Date endTime) {
        String userType = null;
        if (userId != null && !"".equals(userId)) {
            userType = personMapper.getUserTypeByUserId(userId);
        }
        List<AccountId> accountIds = new ArrayList<>();
        SearchBindDetailsRequest.Builder request = SearchBindDetailsRequest.newBuilder();
        AccountId accountId = AccountId.newBuilder().setKey(userId).build();
        if ("2".equals(userType)) {
            accountIds.add(accountId);
            request.addAllRelatedid(accountIds);
        }
        if ("0".equals(userType)) {
            accountIds.add(accountId);
            request.addAllUser(accountIds);
        }
        request.setValidStart(Timestamps.fromMillis((startTime.getTime())));
        request.setValidEnd(Timestamps.fromMillis((endTime.getTime())));
        SearchBindDetailsResponse response = bindAccountChannel.getBlockingStub().searchBindDetails(request.build());
        List<BindDetail> bindDetail = response.getItemsList();
        //接收返回的数据
        List<BindAccountModel> modelList = new ArrayList<>();

        for (BindDetail detail : bindDetail) {
            BindAccountModel model = new BindAccountModel();
            model.setInstitutionId(detail.getRelatedid().getKey());
            model.setBindValidity(detail.getBindValidity());
            model.setBindLimit(detail.getDownloadLimit());
            model.setPersonId(detail.getUser().getKey());
            model.setBindTime(new Date(detail.getCreateTime().getSeconds()));
            model.setInvalidTime(new Date(detail.getValidEnd().getSeconds()));
            modelList.add(model);
        }

        return modelList;
    }

    @RequestMapping("/checkBindLimit")
    public List<String> checkBindLimit(List<String> userIds,Integer bindLimit){

        try {
            //已绑定人数超出修改上限账号集合
            List<String> beyondId = new ArrayList<>();
            for (String userId : userIds) {
                SearchCountRequest request = SearchCountRequest.newBuilder().setBindId(userId).build();
                SearchCountResponse response = bindAccountChannel.getBlockingStub().searchCountBindingByUserId(request);
                int count = response.getTotalcount();
                if (count>bindLimit){
                    beyondId.add(userId);
                }
            }
            return beyondId;
        }catch (Exception e){
            log.error("判断已绑定人数是否大于修改上限出错，账号："+userIds,e);
            throw e;
        }
    }
 }
