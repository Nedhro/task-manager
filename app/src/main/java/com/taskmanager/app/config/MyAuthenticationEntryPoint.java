package com.taskmanager.app.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private static final Logger logger =
      LogManager.getLogger(MyAuthenticationEntryPoint.class.getName());

  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    logger.error("message: " + authException.getMessage());
    response.setStatus(response.SC_UNAUTHORIZED);
    response.sendError(response.SC_UNAUTHORIZED, "Unauthorized Request");
  }
}
