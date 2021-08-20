package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.UserDto;
import com.taskmanager.app.repository.RoleRepository;
import com.taskmanager.app.service.UserService;
import com.taskmanager.app.util.UrlConstraint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = UrlConstraint.UserManagement.ROOT)
public class UserController {

  private final UserService userService;
  private final RoleRepository roleRepository;

  public UserController(UserService userService, RoleRepository roleRepository) {
    this.userService = userService;
    this.roleRepository = roleRepository;
  }

  @PostMapping
  public Response saveUser(@RequestBody UserDto userDto) {
    return userService.save(userDto);
  }
}
