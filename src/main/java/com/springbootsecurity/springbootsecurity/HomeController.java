package com.springbootsecurity.springbootsecurity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping("/home")
  public String home() {
    return "This is the home page";
  }

  @GetMapping("/admin")
  public String admin() {
    return "This is the admin page";
  }
}
