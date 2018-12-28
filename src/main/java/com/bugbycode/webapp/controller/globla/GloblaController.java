package com.bugbycode.webapp.controller.globla;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GloblaController{
	/*
	@ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
	public ModelAndView processException(HttpServletResponse response,RuntimeException exception) {
        ModelAndView m = new ModelAndView();
        m.addObject("status", response.getStatus());
        m.addObject("roncooException", exception.getMessage());
        m.setViewName("pages/commons/error");
        return m;
    }*/
}