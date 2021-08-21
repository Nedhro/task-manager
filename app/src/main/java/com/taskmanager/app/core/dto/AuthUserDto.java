package com.taskmanager.app.core.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class AuthUserDto {

  private Long id;
  private String username;
  private String name;
  private Boolean enabled;
  private String password;
  private List<AuthorityDto> authorities = new ArrayList<>();
  private Date lastPasswordResetDate;

  public AuthUserDto(Long id, String username, String name, Boolean enabled) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.enabled = enabled;
  }
}
