package com.lind.webSecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LindAuthenticationSuccessHandler
    implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
  @Autowired
  ObjectMapper objectMapper;

  /**
   * Called when a user has been successfully authenticated.
   *
   * @param request        the request which caused the successful authentication
   * @param response       the response
   * @param authentication the <tt>Authentication</tt> object which was created during
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication)
      throws IOException, ServletException {
    String method = "onAuthenticationSuccess";
    response.setStatus(HttpServletResponse.SC_OK);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf-8");
    String jsonMessage = objectMapper.writeValueAsString(ImmutableMap.of("message", "success"));
    response.getWriter().append(jsonMessage);
    log.info("success,writeResponse,responseBody={}", jsonMessage);
  }
}
