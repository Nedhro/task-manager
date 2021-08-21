package com.taskmanager.app.config;

import com.taskmanager.app.core.model.AuthUser;
import com.taskmanager.app.core.model.Permission;
import com.taskmanager.app.core.model.Role;
import com.taskmanager.app.core.model.User;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class AuthUserFactory {

  private AuthUserFactory() {
  }

  public static AuthUser create(User user) {
    return new AuthUser(
        user.getId(),
        user.getUsername(),
        user.getName(),
        user.getPassword(),
        getAuthorities(user.getRoles()),
        user.getEnabled(),
        user.getLastPasswordResetDate());
  }

  private static Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

    return getGrantedAuthorities(getPrivileges(roles));
  }

  private static Set<String> getPrivileges(Collection<Role> roles) {

    Set<String> privileges = new HashSet<>();
    Set<Permission> collection = new HashSet<>();
    for (Role role : roles) {
      collection.addAll(role.getPermissions());
    }
    for (Permission item : collection) {
      privileges.add(item.getName());
    }
    return privileges;
  }

  private static Set<GrantedAuthority> getGrantedAuthorities(Set<String> privileges) {
    Set<GrantedAuthority> authorities = new HashSet<>();
    for (String privilege : privileges) {
      authorities.add(new SimpleGrantedAuthority(privilege));
    }
    return authorities;
  }
}
