package com.ken3d.threedfy.presentation.common.exception;

import com.ken3d.threedfy.application.user.exception.UserAppLayerException;
import com.ken3d.threedfy.domain.user.exceptions.UserDomainLayerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({Exception.class})
  public ModelAndView serverException(Exception exception) {
    ModelAndView mav = new ModelAndView("error");
    mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    mav.addObject("title", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    mav.addObject("message", exception.getClass().getName() + "\n"
        + exception.getMessage());
    return mav;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({UserDomainLayerException.class, UserAppLayerException.class})
  public ModelAndView userException(Exception exception) {
    ModelAndView mav = new ModelAndView("error");
    mav.addObject("status", HttpStatus.BAD_REQUEST.value());
    mav.addObject("title", HttpStatus.BAD_REQUEST.getReasonPhrase());
    mav.addObject("message", exception.getClass().getName() + "\n"
        + exception.getMessage());
    return mav;
  }
}
