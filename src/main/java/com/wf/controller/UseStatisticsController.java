package com.wf.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("userStatistics")
public class UseStatisticsController {



    @RequestMapping("statisticsInfo")
    public ModelAndView statisticsInformation(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/user_statistics");
        return view;
    }

}
