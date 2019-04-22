package com.wf.controller;

import com.alibaba.fastjson.JSONObject;
import com.wf.service.AdminService;
import com.wf.service.impl.AdminServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("jsonp")
public class JsonpController {

    private static final Logger log = Logger.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminService adminService;


    @RequestMapping(value = "getOperatorUser")
    @ResponseBody
    public void getOperatorUser(String name, String callback,HttpServletResponse response){
        String code = null;
        List<String> res = new ArrayList<>();
        Map<String,Object> result = new HashMap<>();
        if(StringUtils.isEmpty(name)){
            code = "400";
        }else {
            code = "200";
            res = adminService.getAdminNames(name);
        }
        result.put("code",code);
        result.put("data",res.toArray());
        String json = callback + "(" + JSONObject.toJSONString(result)+ ")";
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        try {
            out  = response.getWriter();
            out.write(json);
            out.flush();
        } catch (IOException e) {
            log.error("获取输出对象出错",e);
        }finally {
            if(out != null){
                out.close();
            }
        }
    }
}
