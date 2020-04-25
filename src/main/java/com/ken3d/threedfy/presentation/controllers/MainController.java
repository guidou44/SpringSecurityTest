package com.ken3d.threedfy.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String getMainPage() {
    return "index";
  }

  @GetMapping("/user/dashboard")
  public String getDashboard() {
    return "dashboard";
  }
}

