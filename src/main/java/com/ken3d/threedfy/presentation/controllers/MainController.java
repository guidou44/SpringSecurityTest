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
  @PreAuthorize("HasAtLeastAuthorityOf(0)")
  public String getDashboard() {
    return "dashboard";
  }
}

