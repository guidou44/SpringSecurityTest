package com.ken3d.threedfy.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

  @GetMapping("/login")
  public String getLogin() {
    return "login";
  }

  @GetMapping("/register")
  public String getRegister() {
    return "register";
  }
}

