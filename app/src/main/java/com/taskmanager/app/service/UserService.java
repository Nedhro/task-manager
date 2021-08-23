package com.taskmanager.app.service;

import com.taskmanager.app.core.dto.UserDto;
import com.taskmanager.app.core.entity.User;
import com.taskmanager.app.core.entity.UserVerification;
import java.util.List;
import org.springframework.data.domain.Page;

public interface UserService {

  List<User> findAll();

  Page<User> findAll(int page, int size);

  User findById(long Id);

  User findByUserName(String username);

  User registration(UserDto user, String password) throws Exception;

  User save(User user);

  User update(User user);

  void generateVerificationToken(User user, String token);

  UserVerification getUserByverificationToken(String token) throws Exception;
}
