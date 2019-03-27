package com.wf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 交易统计
 */
@Controller
@RequestMapping("transactionStatistics")
public class TransactionStatisticsController {

    /**
     * 个人现金交易统计
     *
     * @return
     */
    @RequestMapping("personStatistics")
    public ModelAndView personStatistics() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/statistics/person_statistics");
        return view;
    }

    /**
     * 后台充值统计
     *
     * @return
     */
    @RequestMapping("backChargeStatistics")
    public ModelAndView backChargeStatistics() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/statistics/back_charge_statistics");
        return view;
    }
}
