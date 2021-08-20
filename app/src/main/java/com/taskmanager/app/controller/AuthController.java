package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.LoginDto;
import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.service.AuthService;
import com.taskmanager.app.util.UrlConstraint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstraint.AuthManagement.ROOT)
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(UrlConstraint.AuthManagement.LOGIN)
  public Response login(
      @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
    return authService.login(loginDto, request);
  }
}
