package com.ken3d.threedfy.presentation.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("!prod")
public class PingController {

  @GetMapping("/ping")
  public String getHelloWorld() {
    return "index";
  }
}
