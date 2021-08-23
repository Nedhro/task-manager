package com.taskmanager.app.core.dto.builder;

import com.taskmanager.app.core.dto.RoleDto;
import com.taskmanager.app.core.entity.Permission;
import com.taskmanager.app.core.entity.Role;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RoleDtoBuilder {

  private Role role;

  public RoleDtoBuilder withRole(Role role) {
    this.role = role;
    return this;
  }

  public RoleDto build() {
    RoleDto dto = new RoleDto();
    if (role != null) {
      dto.setId(role.getId());
      dto.setName(role.getName());
      dto.setTitle(role.getTitle());
    }
    if (!Objects.requireNonNull(role).getPermissions().isEmpty()) {
      Set<String> permissions = new HashSet<>();
      role.getPermissions()
          .forEach((Permission permission) -> permissions.add(permission.getName()));
      dto.setPermissions(permissions);
    }
    return dto;
  }

  public RoleDto buildOnlyRole() {
    RoleDto dto = new RoleDto();
    if (role != null) {
      dto.setId(role.getId());
      dto.setName(role.getName());
      dto.setTitle(role.getTitle());
    }
    return dto;
  }
}
