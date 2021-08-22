package com.taskmanager.app.core.dto;

import java.util.HashSet;
import java.util.Set;
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
public class UserDto {

  private Long id;

  private String username;

  private String name;

  private Boolean enabled;

  private Set<String> roles = new HashSet<>();
}
