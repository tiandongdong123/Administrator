package com.wf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

		@RequestMapping("error")
		public ModelAndView  error() {
			ModelAndView model = new ModelAndView();
			model.setViewName("/page/error/error");
			return model;
		}
}
