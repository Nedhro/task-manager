package com.taskmanager.app.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taskmanager.app.core.model.User;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class UserPrinciple implements UserDetails {

  private Long id;
  private String userName;
  @JsonIgnore private String password;
  private Collection<? extends GrantedAuthority> authorities;

  public UserPrinciple(
      Long id,
      String userName,
      String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.userName = userName;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserPrinciple create(User user) {
    try {
      List<GrantedAuthority> authorities =
          user.getRoles().stream()
              .map(role -> new SimpleGrantedAuthority(role.getName()))
              .collect(Collectors.toList());
      return new UserPrinciple(user.getId(), user.getUserName(), user.getPassword(), authorities);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
