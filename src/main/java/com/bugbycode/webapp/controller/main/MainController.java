package com.bugbycode.webapp.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping("/main")
	public String home(ModelMap model) {
		return "pages/layout/main";
	}
}
