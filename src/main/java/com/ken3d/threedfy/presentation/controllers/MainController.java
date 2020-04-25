package com.ken3d.threedfy.presentation.controllers;

import javax.annotation.security.RolesAllowed;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String getMainPage() {
    return "index";
  }

  @GetMapping("/dashboard")
  @Secured({"USER"})
  public String getDashboard() {
    return "dashboard";
  }
}

