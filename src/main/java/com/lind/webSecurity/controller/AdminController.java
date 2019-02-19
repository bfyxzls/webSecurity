package com.lind.webSecurity.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
  @RequestMapping("/admin/product")
  public String product() {
    return "admin.product";
  }

  @RequestMapping("/admin/user")
  public String user() {
    return "admin.user";
  }
}
