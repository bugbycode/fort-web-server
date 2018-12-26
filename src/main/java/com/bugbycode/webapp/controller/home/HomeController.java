package com.bugbycode.webapp.controller.home;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/home")
	public String home(ModelMap model) {
		UserDetails employee =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.put("ONLINE_USER", employee);
		return "pages/layout/main";
	}
}
