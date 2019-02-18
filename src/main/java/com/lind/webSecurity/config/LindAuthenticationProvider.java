package com.lind.webSecurity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LindAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

  }

  @Override
  protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//    1.  根据用户提交的用户名从缓存中查询UserDetails
//    2.  若没有在缓存中查询到UserDetails，那么就调用retrieveUser检索UserDetails，这个方法是一个抽象方法，在子类中实现；若在子类中没有查询到UserDetails，那么就会抛出UsernameNotFoundException，在AbstractUserDetailsAuthenticationProvider捕获了这个异常并且进行了处理
//    3. 接着开始检查UserDetails(不论是从缓存中获取的UserDetails，还是检索出来的UserDetails，都要进行检查)，主要包括的检测有user是否已经过期，是否已经被锁定等等，在AbstractUserDetailsAuthenticationProvider没有检测用户输入的密码是否正确，在检查这一步调用了抽象
//    additionalAuthenticationChecks，所有在子类中要实现密码是否匹配的检测
//    4.  若通过了所有的检测，那么就判断是否这个UserDetails已经放入到了缓存中，若没有那么就放入到缓存中。
//    5.  最后返回一个包含了用户完整信息的UsernamePasswordAuthenticationToken，包括用户名，密码，权限等等。
    return User.builder().username("lind").build();
  }
}
