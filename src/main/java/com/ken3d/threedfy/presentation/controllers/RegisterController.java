package com.ken3d.threedfy.presentation.controllers;

import com.ken3d.threedfy.presentation.user.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
public class RegisterController {


  @GetMapping("/register")
  public String showRegistrationForm(WebRequest request, Model model) {
    UserDto userDto = new UserDto();
    model.addAttribute("user", userDto);
    return "register";
  }

}
