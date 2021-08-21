package com.taskmanager.app.core.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class UserDto {

  private Long id;

  private String username;

  private String name;

  private Boolean enabled;

  private Set<String> roles = new HashSet<>();
}
