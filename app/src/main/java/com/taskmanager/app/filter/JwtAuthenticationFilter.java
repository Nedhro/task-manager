package com.taskmanager.app.filter;

import com.taskmanager.app.service.impl.CustomUserDetailsService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomUserDetailsService customUserDetailsService;

  public JwtAuthenticationFilter(
      JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {
    final String authorizationHeader = httpServletRequest.getHeader("Authorization");

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      String username = null;
      String jwt = null;
      jwt = authorizationHeader.substring(7);
      try {
        if (StringUtils.hasText(jwt) && jwtTokenProvider.isValidateToken(jwt, httpServletRequest)) {
          username = jwtTokenProvider.extractUsername(jwt);
          UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (Exception e) {
      }
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
