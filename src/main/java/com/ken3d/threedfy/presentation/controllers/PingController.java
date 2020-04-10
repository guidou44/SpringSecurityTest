package com.ken3d.threedfy.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PingController {

  @GetMapping("/ping")
  public String getHelloWorld() {
    return "Index";
  }
}
