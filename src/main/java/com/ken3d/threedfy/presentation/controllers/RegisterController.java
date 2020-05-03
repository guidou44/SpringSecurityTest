package com.ken3d.threedfy.presentation.controllers;

import com.ken3d.threedfy.presentation.user.IUserService;
import com.ken3d.threedfy.presentation.user.UserDto;
import com.ken3d.threedfy.presentation.user.exceptions.UserAlreadyExistException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {

  private final IUserService userService;

  @Autowired
  public RegisterController(IUserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showRegistrationForm(WebRequest request, Model model) {
    UserDto userDto = new UserDto();
    model.addAttribute("user", userDto);
    return "register";
  }

  @PostMapping("/registration")
  public ModelAndView registerUserAccount(
      @ModelAttribute("user") @Valid UserDto userDto,
      HttpServletRequest request, Errors errors) {

    try {
      userService.registerNewUserAccount(userDto);
    } catch (UserAlreadyExistException uaeEx) {
      ModelAndView mav = new ModelAndView("register");
      mav.addObject("message", "An account for that username or email already exists.");
      return mav;
    }

    return new ModelAndView("successRegister", "user", userDto);
  }

}
