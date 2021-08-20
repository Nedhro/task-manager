package com.taskmanager.app.core.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RoleDto extends BaseDto {
  @NotEmpty(message = "Name is mandatory")
  private String name;
}
