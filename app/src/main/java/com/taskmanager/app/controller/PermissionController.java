package com.taskmanager.app.controller;

import com.taskmanager.app.core.model.AuthUser;
import com.taskmanager.app.repository.PermissionRepository;
import com.taskmanager.app.util.CustomUtil;
import com.taskmanager.app.util.MenuGroup;
import com.taskmanager.app.util.RoleGroup;
import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PermissionController {

  private static Set<String> featuresMenu = new HashSet<>();

  static {
    for (MenuGroup menu : MenuGroup.values()) {
      featuresMenu.add(menu.name());
    }
    CustomUtil.permissions.put(RoleGroup.MENU.name(), featuresMenu);
  }

  private final PermissionRepository permissionRepository;

  public PermissionController(PermissionRepository permissionRepository) {
    this.permissionRepository = permissionRepository;
  }

  @GetMapping("/admin/permissions")
  @PreAuthorize("hasPermission(null, 'ROLE', 'READ')")
  public ResponseEntity<?> permissions(Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }
    return ResponseEntity.ok(permissionRepository.findAll());
  }

  @GetMapping("/admin/permission/{group}")
  @PreAuthorize("hasPermission(null, 'ROLE', 'READ')")
  public ResponseEntity<?> permissionsByGroup(
      @PathVariable(value = "group") String group, Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }
    return ResponseEntity.ok(permissionRepository.findByGroup(group));
  }

  @GetMapping("/admin/permission/group")
  @PreAuthorize("hasPermission(null, 'ROLE', 'READ')")
  public ResponseEntity<?> groups(Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }
    return ResponseEntity.ok(RoleGroup.values());
  }
}
