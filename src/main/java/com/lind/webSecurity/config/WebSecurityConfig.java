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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @EnableWebMvcSecurity 注解开启Spring Security的功能.
 * 忽略/和/index页面的授权.
 * 指定表单登陆和登陆页面.
 * 指定自定义的成功handler处理方式.
 * @EnableGlobalMethodSecurity注解表示开启@PreAuthorize,@PostAuthorize, @Secured.
 * 授权执行顺序：filter->provider.retrieveUser->userDetialsService->provider.additionalAuthenticationChecks
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
        .addFilterAt(lindAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).authorizeRequests().and()
        .logout()
        .permitAll();
  }

  /**
   * 自定义的Filter.
   * AuthenticationFilter默认是：UsernamePasswordAuthenticationFilter.https://github.com/spring-projects/spring-security/blob/ec970c9b8e7c2d669bc80b1bd21ad3ba91a20461/web/src/main/java/org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.java
   * AuthenticationProvider默认是：DaoAuthenticationProvider.https://github.com/spring-projects/spring-security/blob/a3210c96d9f6fd64c285d71fd3175072b32c41bf/core/src/main/java/org/springframework/security/authentication/dao/DaoAuthenticationProvider.java
   * 授权方式get:/login?username=zzl&password=123456
   *
   * @return
   */
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

  /**
   * 密码生成策略.
   *
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
