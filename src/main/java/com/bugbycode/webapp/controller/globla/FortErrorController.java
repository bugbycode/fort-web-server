package com.bugbycode.webapp.controller.globla;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FortErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";
	
	@RequestMapping(value=ERROR_PATH)  
    public ModelAndView handleError(HttpServletResponse response,Exception exception){  
		ModelAndView m = new ModelAndView();
		m.addObject("status", response.getStatus());
		if(exception != null) {
			m.addObject("roncooException", exception.getMessage());
		}
        m.setViewName("pages/commons/error");
        return m;
    }
	
	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}