package com.taskmanager.app.security;

import com.taskmanager.app.config.AuthorizationRequestInterceptor;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CustomAuthenticationTokenFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TokenProvider jwtTokenProvider;

  @Autowired
  private RestTemplate restTemplate;

  @Value("Authorization")
  private String tokenHeader;

  @Value("${jwt.token.bearer}")
  private String bearer;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String tokenHeader = request.getHeader(this.tokenHeader);
    if (tokenHeader != null
        && tokenHeader.startsWith(bearer)
        && !request.getRequestURI().startsWith("/public")) {
      String authToken = tokenHeader.substring(bearer.length());
      String username = jwtTokenProvider.getUsernameFromToken(authToken);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        AuthorizationRequestInterceptor requestInterceptor =
            new AuthorizationRequestInterceptor(tokenHeader);
        restTemplate.setInterceptors(Collections.singletonList(requestInterceptor));
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (userDetails != null && jwtTokenProvider.validateToken(authToken, userDetails)) {
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }

    chain.doFilter(request, response);
  }
}
