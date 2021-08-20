package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.LoginDto;
import com.taskmanager.app.core.dto.LoginResponseDto;
import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.filter.JwtTokenProvider;
import com.taskmanager.app.service.AuthService;
import com.taskmanager.app.util.ResponseBuilder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthServiceImpl(
      AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public Response login(LoginDto loginDto, HttpServletRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));
    if (authentication.isAuthenticated()) {
      LoginResponseDto loginResponseDto = new LoginResponseDto();
      loginResponseDto.setToken(jwtTokenProvider.generateToken(authentication, request));
      loginResponseDto.setUsername(authentication.getName());
      return ResponseBuilder.getSuccessResponse(
          HttpStatus.OK, "Logged In Success", loginResponseDto);
    }
    return ResponseBuilder.getFailureResponse(
        HttpStatus.BAD_REQUEST, "Invalid Username or password");
  }
}
