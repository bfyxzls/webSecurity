package com.lind.webSecurity.config;

import java.util.ArrayList;
import java.util.List;
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
    /*
      设置用户和角色需要注意：
      1. commaSeparatedStringToAuthorityList放入角色时需要加前缀ROLE_，而在controller使用时不需要加ROLE_前缀
      2. 放入的是权限时，不能加ROLE_前缀，hasAuthority与放入的权限名称对应即可
    */
    List<UserDetails> userDetailsList = new ArrayList<>();
    userDetailsList.add(User.builder()
        .username("admin")
        .password(passwordEncoder.encode("123"))
        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("read,ROLE_ADMIN")).build());
    userDetailsList.add(User.builder()
        .username("user")
        .password(passwordEncoder.encode("123"))
        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("read,ROLE_USER"))
        .build());

    //获取用户
    return userDetailsList.stream()
        .filter(o -> o.getUsername().equals(name))
        .findFirst()
        .orElse(null);

  }
}