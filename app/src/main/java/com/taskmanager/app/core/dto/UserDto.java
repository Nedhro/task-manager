package com.taskmanager.app.core.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends BaseDto {

  private String userName;
  private String email;
  private String password;
  private List<RoleDto> roles;
}
