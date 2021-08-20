package com.taskmanager.app.service;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.UserDto;
import com.taskmanager.app.core.model.User;
import java.util.List;

public interface UserService {
  Response save(UserDto userDto);

  User update(User user);

  User getById(Long id);

  User getByName(String name);

  String delete(Long id);

  List<User> list();
}
