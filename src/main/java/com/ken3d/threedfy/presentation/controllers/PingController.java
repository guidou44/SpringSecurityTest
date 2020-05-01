package com.ken3d.threedfy.presentation.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PingController {

  @GetMapping("/ping")
  public String getHelloWorld() {
    return "index";
  }

  @PreAuthorize("HasMinimumRole('MANAGER')")
  @GetMapping("/ping/managerAuth")
  public String getManagerProtectedView () {
    return "index";
  }

  @PreAuthorize("HasAtLeastAuthorityOf(1)")
  @GetMapping("/ping/levelAuth")
  public String getLevelProtectedView () {
    return "index";
  }
}
