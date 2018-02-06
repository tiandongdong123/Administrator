package com.wf.controller;

import com.google.protobuf.util.Timestamps;
import com.utils.QRCodeUtil;
import com.wanfangdata.grpcchannel.BindAccountChannel;
import com.wanfangdata.grpcchannel.BindAuthorityChannel;
import com.wanfangdata.model.BindSearchParameter;
import com.wanfangdata.model.PagerModel;
import com.wanfangdata.rpc.bindauthority.*;
import com.wanfangdata.setting.BindAuthorityMapping;
import com.wf.bean.BindAccountModel;
import com.wf.bean.BindAuthorityModel;
import com.wf.dao.PersonMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wfks.authentication.account.userinfo.UserInfoDao;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private BindAuthorityMapping bindAuthorityMapping;
    @Autowired
    private PersonMapper personMapper;


    private static final Logger log = Logger.getLogger(PersonBindInstitutionController.class);

    /**
     * 获取机构下所有没有设置个人绑定权限的机构账号和机构子账号
     *
     * @param institutionName 机构名称
     * @return
     */
    @RequestMapping("/userId")
    @ResponseBody
    public List<String> getUserIdByInstitutionName(String institutionName) {
        List<String> userIdList = userInfoDao.getUserIdByInstitutionName(institutionName);

        List<String> userIds = new ArrayList<>();
        for (String userId : userIdList) {
            SearchAccountAuthorityRequest.Builder request = SearchAccountAuthorityRequest.newBuilder().setUserId(userId);
            SearchAccountAuthorityResponse response = bindAuthorityChannel.getBlockingStub().searchAccountAuthority(request.build());
            List<AccountAuthority> accountList = response.getItemsList();
            if (accountList == null || accountList.size() < 1) {
                userIds.add(userId);
            }
        }
        return userIds;
    }

    /**
     * 跳转机构子账号信息管理页面
     *
     * @param userId 用户Id（此参数用于判断是否由信息管理页面跳转而来）
     * @param model
     * @return
     */
    @RequestMapping("/bindInfo")
    public String toBindInfoManagement(String userId,Model model) {

        if (userId!=null&&!"".equals(userId)){
            model.addAttribute("userId",userId);
            model.addAttribute("upPage",true);
        }
        model.addAttribute("pager",null);
        return "/page/usermanager/user_binding_manager";
    }

    /**
     * 跳转个人绑定机构权限设置页面
     *
     * @return
     */
    @RequestMapping("/setPersonAuthority")
    public String toPersonBindAuthority() {

        return "/page/usermanager/user_binding_authority" ;
    }

    /**
     * 开通个人绑定机构权限
     *
     * @param bindAuthorityModel 绑定信息
     * @return
     */
    @RequestMapping("/openAuthority")
    public String openAuthority(BindAuthorityModel bindAuthorityModel) {
        String[] userIds = bindAuthorityModel.getUserId().split(",");
        String[] authoritys = bindAuthorityModel.getBindAuthority().split(",");
        List<String> authorityList = new ArrayList<>();
        for (String authority : authoritys) {
            authorityList.add(bindAuthorityMapping.getAuthorityName(authority));
        }
        OpenBindRequest.Builder request = OpenBindRequest.newBuilder().addAllUserId(Arrays.asList(userIds))
                .setBindType(BindType.forNumber(bindAuthorityModel.getBindType()))
                .setBindLimit(bindAuthorityModel.getBindLimit())
                .setBindValidity(bindAuthorityModel.getBindValidity())
                .setDownloadLimit(bindAuthorityModel.getDownloadLimit())
                .addAllBindAuthority(authorityList);
        bindAuthorityChannel.getBlockingStub().openBindAuthority(request.build());
        return "/page/usermanager/user_binding_manager";
    }

    /**
     * 修改个人绑定机构权限
     *
     * @param bindAuthorityModel 绑定信息
     * @return
     */
    @RequestMapping("/updateAuthority")
    @ResponseBody
    public Boolean editBindAuthority(BindAuthorityModel bindAuthorityModel){
        try{
            String[] userIds = bindAuthorityModel.getUserId().split(",");
            String[] authoritys = bindAuthorityModel.getBindAuthority().split(",");
            List<String> authorityList = new ArrayList<>();
            for (String authority : authoritys) {
                authorityList.add(bindAuthorityMapping.getAuthorityName(authority));
            }
            EditBindRequest.Builder request = EditBindRequest.newBuilder().addAllUserId(Arrays.asList(userIds))
                    .setBindType(BindType.forNumber(bindAuthorityModel.getBindType()))
                    .setBindLimit(bindAuthorityModel.getBindLimit())
                    .setBindValidity(bindAuthorityModel.getBindValidity())
                    .setDownloadLimit(bindAuthorityModel.getDownloadLimit())
                    .addAllBindAuthority(authorityList);
            ServiceResponse response = bindAuthorityChannel.getBlockingStub().editBindAuthority(request.build());
            if (response.getServiceResult()){
                return true;
            }else {
                return  false;
            }
        }catch (Exception e){
            log.error("修改个人绑定机构权限失败，机构id："+bindAuthorityModel.getUserId());
           return false;
        }
    }

    /**
     * 个人绑定机构信息管理查询
     *
     * @param parameter 查询参数类
     * @param upPage 上层页面
     * @param model
     * @return
     */
    @RequestMapping("/searchBindInfo")
    public String seachBindInfo(BindSearchParameter parameter,String upPage, Model model) {

        String userType = null;
        Date startTime = null;
        Date endTime = null;
        if(parameter.getPage()==null){
            parameter.setPage(1);
        }
        if(parameter.getPageSize()==null){
            parameter.setPageSize(20);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (parameter.getStartDay()!=null&&!"".equals(parameter.getStartDay())){
                startTime = format.parse(parameter.getStartDay());
            }
            if (parameter.getEndDay()!=null&&!"".equals(parameter.getEndDay())){
                endTime = format.parse(parameter.getEndDay());
            }
;
        } catch (ParseException e) {
            log.error("转换时间出错",e);
        }

        if (parameter.getUserId() != null && !"".equals(parameter.getUserId())) {
            if (parameter.getInstitutionName() != null &&!"".equals(parameter.getInstitutionName())) {
                String groupName = personMapper.getInstitutionByUserId(parameter.getUserId());
                if (parameter.getInstitutionName().equals(groupName)) {
                    return null;
                }
            }
            userType = personMapper.getUserTypeByUserId(parameter.getUserId());
        }
        List<AccountId> accountIds = new ArrayList<>();
        SearchBindDetailsRequest.Builder request = SearchBindDetailsRequest.newBuilder();
        if (userType != null) {
            AccountId accountId = AccountId.newBuilder().setKey(parameter.getUserId()).build();
            if ("2".equals(userType)) {
                accountIds.add(accountId);
                request.addAllRelatedid(accountIds);
            }
            if ("0".equals(userType)) {
                accountIds.add(accountId);
                request.addAllUser(accountIds);
            }
        } else if (parameter.getInstitutionName() != null &&!"".equals(parameter.getInstitutionName())) {
            List<String> userIds = userInfoDao.getUserIdByInstitutionName(parameter.getInstitutionName());
            for (String id : userIds) {
                AccountId accountId = AccountId.newBuilder().setKey(id).build();
                accountIds.add(accountId);
            }
            request.addAllRelatedid(accountIds);
        }
        if (startTime!=null){
            request.setStartAddTime(Timestamps.fromMillis((startTime.getTime())));
        }
       if(endTime!=null){
           request.setEndAddTime(Timestamps.fromMillis((endTime.getTime())));
       }
        SearchBindDetailsRequest.Builder countRequest = request;
        SearchBindDetailsResponse countResponse = bindAccountChannel.getBlockingStub().searchBindDetailsOrderUser(countRequest.build());
        int totalSize = countResponse.getTotalCount();

       if (parameter.getPage()!=0){
            int index = (parameter.getPage()-1)*parameter.getPageSize();
            request.setStartIndex(index);
       }
       if(parameter.getPageSize()!=0){
            request.setCount(parameter.getPageSize());
       }
        SearchBindDetailsResponse response = bindAccountChannel.getBlockingStub().searchBindDetailsOrderUser(request.build());
        List<BindDetail> bindDetail = response.getItemsList();
        //接收返回的数据
        List<BindAccountModel> modelList = new ArrayList<>();

        for (BindDetail detail : bindDetail) {
            BindAccountModel bindModel = new BindAccountModel();
            bindModel.setInstitutionId(detail.getRelatedid().getKey());
            bindModel.setBindType(bindAuthorityMapping.getBindTypeName(detail.getBindType().getNumber()));
            bindModel.setBindValidity(detail.getBindValidity());
            bindModel.setBindLimit(detail.getDownloadLimit());
            bindModel.setDownloadLimit(detail.getDownloadLimit());
            bindModel.setPersonId(detail.getUser().getKey());
            Date bindTime = new Date(Timestamps.toMillis(detail.getValidStart()));
            Date expireTime = new Date(Timestamps.toMillis(detail.getValidEnd()));
            bindModel.setBindTime(transformDate(bindTime));
            bindModel.setInvalidTime(transformDate(expireTime));
            bindModel.setAuthority(detail.getAuthority());
            modelList.add(bindModel);
        }

        int currentPage = parameter.getPage();
        int pageSize = parameter.getPageSize();
        String actionUrl = "/bindAuhtority/searchBindInfo.do";
        PagerModel<BindSearchParameter> formList = new PagerModel<BindSearchParameter>(currentPage, totalSize, pageSize, modelList, actionUrl, parameter);
        model.addAttribute("pager",formList);
        model.addAttribute("upPage",upPage);

        return "/page/usermanager/user_binding_table";
    }

    /**
     *  检查个人绑定机构权限的绑定上限
     *
     * @param userId 机构账号
     * @param bindLimit 绑定上限数值
     * @return
     */
    @RequestMapping("/checkBindLimit")
    @ResponseBody
    public Boolean checkBindLimit(String userId, Integer bindLimit) {

        if(userId==null||bindLimit==null){
            return false;
        }
        try {
            //已绑定人数超出修改上限账号集合
            List<AccountId> accountIds = new ArrayList<>();
            AccountId accountId = AccountId.newBuilder().setAccounttype("Group").setKey(userId).build();
            accountIds.add(accountId);
            SearchBindDetailsRequest countRequest = SearchBindDetailsRequest.newBuilder().addAllRelatedid(accountIds).build();
            SearchBindDetailsResponse countResponse = bindAccountChannel.getBlockingStub().searchBindDetailsOrderUser(countRequest);
                int count = countResponse.getTotalCount();
                if (count > bindLimit) {
                    return false;
                }
            return true;
        } catch (Exception e) {
            log.error("判断已绑定人数是否大于修改上限出错，账号：" + userId, e);
            throw e;
        }
    }

    /**
     * 机构下所有机构账号和机构子账号
     *
     * @param institutionName 机构名称
     * @return
     */
    @RequestMapping("/allUserId")
    @ResponseBody
    public List<String> getAllUserIdByInstitutionName(String institutionName) {

        return userInfoDao.getUserIdByInstitutionName(institutionName);

    }

    /**
     * 返回线下扫描二维码
     *
     * @param userId 绑定的机构id
     * @param response
     */
    @RequestMapping("/getQRCode")
    public void getQRCode(String userId,HttpServletResponse response) {
        try {
            CodeDetail codeDetail = CodeDetail.newBuilder().setBindId(userId).setBindType(BindType.LINE_SCAN).build();
            GetCodeRequest codeRequest = GetCodeRequest.newBuilder().addCodeDetails(codeDetail).build();
            GetCodeResponse codeResponse = bindAccountChannel.getBlockingStub().getQRCode(codeRequest);
            String url = codeResponse.getCiphertext();
            QRCodeUtil.outputQRCode(response, url, 400, 400);
        }catch (Exception e){
            log.error("返回二维码失败，失败账号："+userId);
        }
    }

    /**
     * 重置二维码
     *
     * @param userId 绑定的机构id
     * @param response
     */
    @RequestMapping("/resetQRCode")
    public void resetQRCode(String userId,HttpServletResponse response) {
        try {
            CodeDetail codeDetail = CodeDetail.newBuilder().setBindId(userId).setBindType(BindType.LINE_SCAN).build();
            GetCodeRequest codeRequest = GetCodeRequest.newBuilder().addCodeDetails(codeDetail).build();
            GetCodeResponse codeResponse = bindAccountChannel.getBlockingStub().resetQRCode(codeRequest);
            String url = codeResponse.getCiphertext();
            QRCodeUtil.outputQRCode(response, url, 400, 400);
        }catch (Exception e){
            log.error("重置二维码失败，失败账号："+userId);
        }
    }
    /**
     * 转换时间（格式：yyyy-MM-dd hh:mm:ss ）
     * @param date
     * @return
     */
    public String transformDate(Date date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = format.format(date);
            return time;
        } catch (Exception e) {
            log.error("时间转换出错", e);
            throw e;
        }
    }
}
