package com.taskmanager.app.config;

import com.taskmanager.app.filter.JwtAuthenticationFilter;
import com.taskmanager.app.service.impl.CustomUserDetailsService;
import com.taskmanager.app.util.UrlConstraint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final CustomUserDetailsService customUserDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(
      CustomUserDetailsService customUserDetailsService,
      PasswordEncoder passwordEncoder,
      MyAuthenticationEntryPoint myAuthenticationEntryPoint,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.customUserDetailsService = customUserDetailsService;
    this.passwordEncoder = passwordEncoder;
    this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    String allPrefix = "/*";
    http.cors()
        .and()
        .csrf()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(myAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(
            UrlConstraint.AuthManagement.ROOT + allPrefix, UrlConstraint.UserManagement.ROOT)
        .permitAll()
        //                .antMatchers("/jdhhd").hasAnyRole("", "")
        .anyRequest()
        .authenticated();
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
