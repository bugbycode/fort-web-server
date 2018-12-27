package com.bugbycode.webapp.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("/query")
	public String query(){
		return "pages/user/list";
	}
}
