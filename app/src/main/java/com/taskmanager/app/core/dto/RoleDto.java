package com.taskmanager.app.core.dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleDto {

  private Long id;

  @NotNull
  private String name;

  private String title;

  private Set<String> permissions = new HashSet<>();
}
