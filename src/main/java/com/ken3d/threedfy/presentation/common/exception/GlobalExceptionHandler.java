package com.ken3d.threedfy.presentation.common.exception;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Profile("prod")
@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({Exception.class})
  public ModelAndView exception(Exception exception) {
    ModelAndView mav = new ModelAndView("error");
    mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    mav.addObject("title", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    mav.addObject("message", exception.getMessage());
    return mav;
  }
}
