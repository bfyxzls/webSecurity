package com.lind.webSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    User user = new User(name,
        passwordEncoder.encode("123456"),
        AuthorityUtils.commaSeparatedStringToAuthorityList("read,ROLE_USER"));//设置权限和角色
    // 1. commaSeparatedStringToAuthorityList放入角色时需要加前缀ROLE_，而在controller使用时不需要加ROLE_前缀
    // 2. 放入的是权限时，不能加ROLE_前缀，hasAuthority与放入的权限名称对应即可
    return user;
  }
}