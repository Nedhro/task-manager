package com.taskmanager.app.service.impl;

import com.taskmanager.app.config.AuthUserFactory;
import com.taskmanager.app.core.dto.AuthUserDto;
import com.taskmanager.app.core.model.AuthUser;
import com.taskmanager.app.core.model.User;
import com.taskmanager.app.repository.UserRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public AuthUser loadUserByUsername(String username) throws UsernameNotFoundException {

    try {
      if (username != null) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
          return null;
        }
        return AuthUserFactory.create(user);
      }
      AuthUserDto UserDto =
          restTemplate
              .exchange(
                  "http://localhost:8080/admin/authuser", HttpMethod.GET, null, AuthUserDto.class)
              .getBody();
      if (UserDto == null)
        return null;

      AuthUser user =
          new AuthUser(
              UserDto.getId(),
              UserDto.getUsername(),
              UserDto.getName(),
              UserDto.getPassword(),
              UserDto.getAuthorities().stream()
                  .map(mapper -> new SimpleGrantedAuthority(mapper.getName()))
                  .collect(Collectors.toList()),
              UserDto.getEnabled(),
              UserDto.getLastPasswordResetDate());
      return user;
    } catch (Exception e) {
      return null;
    }
  }
}
