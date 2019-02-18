package com.lind.webSecurity.config;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @EnableWebMvcSecurity 注解开启Spring Security的功能.
 * 忽略/和/index页面的授权.
 * 指定表单登陆和登陆页面.
 * 指定自定义的成功handler处理方式.
 * @EnableGlobalMethodSecurity注解表示开启@PreAuthorize,@PostAuthorize, @Secured.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  LindAuthenticationSuccessHandler lindAuthenticationSuccessHandler;

  @Autowired
  LindAuthenticationFailHandler lindAuthenticationFailHandler;
  @Autowired
  LindAuthenticationProvider lindAuthenticationProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/", "/index").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/hello")//默认登录成功后跳转的页面
        .successHandler(lindAuthenticationSuccessHandler)
        .failureHandler(lindAuthenticationFailHandler)
        .permitAll()
        .and()
        // .addFilterBefore(lindAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).authorizeRequests()
        .logout()
        .permitAll();
  }

  @Bean
  LindUserNameAuthenticationFilter lindAuthenticationFilter() {
    LindUserNameAuthenticationFilter phoneAuthenticationFilter = new LindUserNameAuthenticationFilter();
    ProviderManager providerManager =
        new ProviderManager(Collections.singletonList(lindAuthenticationProvider));
    phoneAuthenticationFilter.setAuthenticationManager(providerManager);
    phoneAuthenticationFilter.setAuthenticationSuccessHandler(lindAuthenticationSuccessHandler);
    phoneAuthenticationFilter.setAuthenticationFailureHandler(lindAuthenticationFailHandler);
    return phoneAuthenticationFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
