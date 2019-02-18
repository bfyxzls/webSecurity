package com.lind.webSecurity.config;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * https://blog.csdn.net/bao19901210/article/details/52574340
 * 这种验证以前项目用过，现在没有写示例代码，先写下大概流程和需要用到的类
 * 这种验证的好处：可以在自定义登录界面添加登录时需要的参数，如多个验证码等、可以修改默认登录名称和密码的参数名
 * <p>
 * 整体流程：
 * 1.用户登录时，先经过自定义的passcard_filter过滤器，该过滤器继承了AbstractAuthenticationProcessingFilter，并且绑定了登录失败和成功时需要的处理器(跳转页面使用)
 * 2.执行attemptAuthentication方法，可以通过request获取登录页面传递的参数，实现自己的逻辑，并且把对应参数set到AbstractAuthenticationToken的实现类中
 * 3.验证逻辑走完后，调用 this.getAuthenticationManager().authenticate(token);方法，执行AuthenticationProvider的实现类的supports方法
 * 4.如果返回true则继续执行authenticate方法
 * 5.在authenticate方法中，首先可以根据用户名获取到用户信息，再者可以拿自定义参数和用户信息做逻辑验证，如密码的验证
 * 6.自定义验证通过以后，获取用户权限set到User中，用于springSecurity做权限验证
 * 7.this.getAuthenticationManager().authenticate(token)方法执行完后，会返回Authentication，如果不为空，则说明验证通过
 * 8.验证通过后，可实现自定义逻辑操作，如记录cookie信息
 * 9.attemptAuthentication方法执行完成后，由springSecuriy来进行对应权限验证，成功于否会跳转到相对应处理器设置的界面。
 */
public class LindUserNameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public LindUserNameAuthenticationFilter() {
    super(new AntPathRequestMatcher("/login", "GET"));
  }

  /**
   * Performs actual authentication.
   * <p>
   * The implementation should do one of the following:
   * <ol>
   * <li>Return a populated authentication token for the authenticated user, indicating
   * successful authentication</li>
   * <li>Return null, indicating that the authentication process is still in progress.
   * Before returning, the implementation should perform any additional work required to
   * complete the process.</li>
   * <li>Throw an <tt>AuthenticationException</tt> if the authentication process fails</li>
   * </ol>
   *
   * @param request  from which to extract parameters and perform the authentication
   * @param response the response, which may be needed if the implementation has to do a
   *                 redirect as part of a multi-stage authentication process (such as OpenID).
   * @return the authenticated user token, or null if authentication is incomplete.
   * @throws AuthenticationException if authentication fails.
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String user = request.getParameter("username");
    String pass = request.getParameter("password");

    if (user == null) {
      throw new IllegalArgumentException("Failed to get the username");
    }

    if (pass == null) {
      throw new IllegalArgumentException("Failed to get the password");
    }

    UserDetails phoneCode = User.builder()
        .username(user.trim())
        .password(pass)
        .build();

    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
        UUID.randomUUID().toString(), phoneCode);

    // Allow subclasses to set the "details" property
    authRequest.setDetails(authenticationDetailsSource.buildDetails(request));

    return this.getAuthenticationManager().authenticate(authRequest);
  }
}
