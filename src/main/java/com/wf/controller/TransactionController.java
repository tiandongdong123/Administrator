package com.wf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("mechanism")
public class TransactionController {

    @RequestMapping("mechanism_order")
    public ModelAndView mechanismOrder(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/mechanism_order");
        return view;
    }
}
