package com.lind.webSecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @GetMapping("/auth")
  public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
    return user;
  }

  @GetMapping("/write")
  @PreAuthorize("hasAuthority('write')")
  public String getWrite() {
    return "have a write authority";
  }

  @GetMapping("/read")
  @PreAuthorize("hasAuthority('read')")
  public String readDate() {
    return "have a read authority";
  }

  @GetMapping("/read-or-write")
  @PreAuthorize("hasAnyAuthority('read','write')")
  public String readWriteDate() {
    return "have a read or write authority";
  }


  @GetMapping("/admin-role")
  @PreAuthorize("hasRole('admin')")
  public String readAdmin() {
    return "have a admin role";
  }

  @GetMapping("/user-role")
  @PreAuthorize("hasRole('USER')")
  public String readUser() {
    return "have a user role";
  }
}
