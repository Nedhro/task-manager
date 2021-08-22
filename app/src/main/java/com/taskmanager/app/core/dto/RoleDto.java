package com.taskmanager.app.core.dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode()
public class RoleDto {

  private Long id;

  @NotNull
  private String name;

  private String title;

  private Set<String> permissions = new HashSet<>();
}
