package com.wf.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.service.PersonService;

@Controller
@RequestMapping("person")
public class PersonController {

	@Autowired
	private PersonService personservice;
	
	
	@RequestMapping("queryperson")
	public ModelAndView queryPerson(Person person,Integer pagenum,Integer pagesize,ModelAndView view){
		Map<String,Object> p = personservice.QueryPersion(person,pagenum,pagesize, null);
		view.addObject("PageList", p);
		view.setViewName("");
		return view;
	}
}
