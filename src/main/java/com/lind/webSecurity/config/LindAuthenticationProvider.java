package com.lind.webSecurity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 如果改变认证的方式，需要实现自己的 AuthenticationProvider.默认是DaoAuthenticationProvider，https://github.com/spring-projects/spring-security/blob/a3210c96d9f6fd64c285d71fd3175072b32c41bf/core/src/main/java/org/springframework/security/authentication/dao/DaoAuthenticationProvider.java
 * 如果需要改变认证的用户信息来源，我们可以实现 UserDetailsService.
 * 认证是由 AuthenticationManager 来管理的，但是真正进行认证的是 AuthenticationManager 中定义的 AuthenticationProvider。AuthenticationManager 中可以定义有多个 AuthenticationProvider。当我们使用 authentication-provider 元素来定义一个 AuthenticationProvider 时，如果没有指定对应关联的 AuthenticationProvider 对象，Spring Security 默认会使用 DaoAuthenticationProvider。DaoAuthenticationProvider 在进行认证的时候需要一个 UserDetailsService 来获取用户的信息 UserDetails，其中包括用户名、密码和所拥有的权限等。所以如果我们需要改变认证的方式，我们可以实现自己的 AuthenticationProvider；如果需要改变认证的用户信息来源，我们可以实现 UserDetailsService。
 * <p>
 * 实现了自己的 AuthenticationProvider 之后，我们可以在配置文件中这样配置来使用我们自己的 AuthenticationProvider。其中 myAuthenticationProvider 就是我们自己的 AuthenticationProvider 实现类对应的 bean。
 */
@Component
@Slf4j
public class LindAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * 校验密码有效性.
   *
   * @param userDetails    .
   * @param authentication .
   * @throws AuthenticationException .
   */
  @Override
  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    if (authentication.getCredentials() == null) {
      logger.debug("Authentication failed: no credentials provided");

      throw new BadCredentialsException(messages.getMessage(
          "AbstractUserDetailsAuthenticationProvider.badCredentials",
          "Bad credentials"));
    }
    // 当前输入的密码与数据库的密码比较
    String presentedPassword = authentication.getCredentials().toString();

    if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
      logger.debug("Authentication failed: password does not match stored value");

      throw new BadCredentialsException(messages.getMessage(
          "AbstractUserDetailsAuthenticationProvider.badCredentials",
          "Bad credentials"));
    }
  }

  /**
   * 获取用户.
   *
   * @param username       .
   * @param authentication .
   * @return
   * @throws AuthenticationException .
   */
  @Override
  protected UserDetails retrieveUser(
      String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    /**
     1.  根据用户提交的用户名从缓存中查询UserDetails
     2.  若没有在缓存中查询到UserDetails，那么就调用retrieveUser检索UserDetails，这个方法是一个抽象方法，
     在子类中实现；若在子类中没有查询到UserDetails，那么就会抛出UsernameNotFoundException，
     在AbstractUserDetailsAuthenticationProvider捕获了这个异常并且进行了处理
     3. 接着开始检查UserDetails(不论是从缓存中获取的UserDetails，还是检索出来的UserDetails，都要进行检查)，
     主要包括的检测有user是否已经过期，是否已经被锁定等等，在AbstractUserDetailsAuthenticationProvider没有检测
     用户输入的密码是否正确，在检查这一步调用了抽象
     additionalAuthenticationChecks，所有在子类中要实现密码是否匹配的检测
     4.  若通过了所有的检测，那么就判断是否这个UserDetails已经放入到了缓存中，若没有那么就放入到缓存中。
     5.  最后返回一个包含了用户完整信息的UsernamePasswordAuthenticationToken，包括用户名，密码，权限等等。
     */
    UserDetails loadedUser = userDetailsService.loadUserByUsername(username);
    if (loadedUser == null) {
      throw new InternalAuthenticationServiceException(
          "UserDetailsService returned null, which is an interface contract violation");
    }
    return loadedUser;
  }
}
