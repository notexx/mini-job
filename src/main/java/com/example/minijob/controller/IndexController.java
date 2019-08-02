package com.example.minijob.controller;

import com.example.minijob.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

	@Autowired
	private JobController jobController;
	
	@RequestMapping("/index")
	public String index(Model model){
		List<Job> jobList = jobController.list();
		model.addAttribute("jobs", jobList);
		return "index.html";
	}
	
}