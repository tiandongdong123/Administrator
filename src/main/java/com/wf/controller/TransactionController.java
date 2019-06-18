package com.wf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("mechanism")
public class TransactionController {

    @RequestMapping("mechanism_order")
    public ModelAndView mechanismOrder(String userId){
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/mechanism_order");
        view.addObject("userId",userId);
        return view;
    }

    @RequestMapping("recharge")
    public ModelAndView recharge(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/mechanism_recharge");
        return view;
    }

    @RequestMapping("transaction")
    public ModelAndView transaction(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/mechanism_transaction");
        return view;
    }

    @RequestMapping("WFCardStatistics")
    public ModelAndView WFCardStatistics(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/usermanager/WFCardStatistics");
        return view;
    }
}
