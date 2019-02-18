package com.lind.webSecurity.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @GetMapping("/user")
  public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
    return user;
  }
}
