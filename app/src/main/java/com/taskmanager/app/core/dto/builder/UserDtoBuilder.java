package com.taskmanager.app.core.dto.builder;

import com.taskmanager.app.core.dto.UserDto;
import com.taskmanager.app.core.entity.Role;
import com.taskmanager.app.core.entity.User;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDtoBuilder {

  private User user;

  public UserDtoBuilder withUser(User user) {
    this.user = user;
    return this;
  }

  public UserDto build() {
    UserDto dto = new UserDto();
    if (user != null) {
      dto.setId(user.getId());
      dto.setUsername(user.getUsername());
      dto.setName(user.getName());
      dto.setEnabled(user.getEnabled());
    }
    if (Objects.requireNonNull(user).getRoles() != null)
      if (!user.getRoles().isEmpty()) {
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach((Role role) -> roles.add(role.getName()));
        dto.setRoles(roles);
      }
    return dto;
  }
}
