package com.taskmanager.app.core.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserDto extends BaseDto {
  private String userName;
  private String email;
  private String password;
  private List<RoleDto> roles;
}
