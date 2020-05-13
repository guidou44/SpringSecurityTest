package com.ken3d.threedfy.presentation.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String getMainPage() {
    return "index";
  }

  @GetMapping("/dashboard")
  @PreAuthorize("hasAtLeastAuthorityOf(0)")
  public String getDashboard() {
    return "dashboard";
  }


  @GetMapping("/error")
  public String getError() {
    return "error";
  }

  @GetMapping("/confirm-email")
  public String getConfirmEmail() {
    return "confirm-email";
  }
}

