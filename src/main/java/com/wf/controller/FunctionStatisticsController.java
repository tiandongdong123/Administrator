package com.wf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 功能统计
 */
@Controller
@RequestMapping("functionStatistics")
public class FunctionStatisticsController {
    /**
     * 广告统计
     *
     * @return
     */
    @RequestMapping("adStatistics")
    public ModelAndView adStatistics() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/page/functionStatistics/ad_statistics");
        return view;
    }
}
