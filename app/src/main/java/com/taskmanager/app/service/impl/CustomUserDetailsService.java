package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.UserPrinciple;
import com.taskmanager.app.core.model.User;
import com.taskmanager.app.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserService userService;

  public CustomUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    User user = userService.getByName(userName);
    UserPrinciple userPrinciple = UserPrinciple.create(user);
    if (userPrinciple == null) {
      throw new UsernameNotFoundException("User Not Found");
    }
    return userPrinciple;
  }
}
