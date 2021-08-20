package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.UserDto;
import com.taskmanager.app.core.enums.ActiveStatus;
import com.taskmanager.app.core.model.User;
import com.taskmanager.app.repository.UserRepository;
import com.taskmanager.app.service.UserService;
import com.taskmanager.app.util.ResponseBuilder;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;
  private final String root = "User";

  public UserServiceImpl(
      UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.modelMapper = modelMapper;
  }

  @Override
  public Response save(UserDto userDto) {
    User user;
    userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user = modelMapper.map(userDto, User.class);
    user = userRepository.save(user);
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.CREATED, root + "Has been Created", user);
  }

  @Override
  public User update(User user) {
    return userRepository.save(user);
  }

  @Override
  public User getById(Long id) {
    return userRepository.findByIdAndActiveStatusTrue(id);
  }

  @Override
  public User getByName(String name) {
    return userRepository.findByUserNameAndActiveStatusTrue(name);
  }

  @Override
  public String delete(Long id) {
    User user = userRepository.findByIdAndActiveStatusTrue(id);
    user.setActiveStatus(ActiveStatus.DELETE.getValue());
    return null;
  }

  @Override
  public List<User> list() {
    return userRepository.list(ActiveStatus.ACTIVE.getValue());
  }
}
