package com.taskmanager.app.service;

import com.taskmanager.app.core.dto.LoginDto;
import com.taskmanager.app.core.dto.Response;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {
  Response login(LoginDto loginDto, HttpServletRequest request);
}
