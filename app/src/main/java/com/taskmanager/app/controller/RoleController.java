package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.RoleDto;
import com.taskmanager.app.core.dto.builder.RoleDtoBuilder;
import com.taskmanager.app.core.entity.AuthUser;
import com.taskmanager.app.core.entity.Permission;
import com.taskmanager.app.core.entity.Role;
import com.taskmanager.app.repository.PermissionRepository;
import com.taskmanager.app.repository.RoleRepository;
import com.taskmanager.app.util.CustomUtil;
import com.taskmanager.app.util.RoleGroup;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

  private static Set<String> features = new HashSet<>();

  static {
    features.add("ROLE_READ");
    features.add("ROLE_WRITE");
    features.add("ROLE_ASSIGN");
    CustomUtil.permissions.put(RoleGroup.ROLE.name(), features);
  }

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;

  public RoleController(RoleRepository roleRepository, PermissionRepository permissionRepository) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
  }

  @GetMapping("/admin/roles")
  @PreAuthorize("hasPermission(null, 'ROLE', 'READ')")
  public ResponseEntity<?> roles(Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }
    Set<RoleDto> roles = new HashSet<>();
    for (Role role : roleRepository.findAll()) {
      roles.add(new RoleDtoBuilder().withRole(role).buildOnlyRole());
    }
    return ResponseEntity.ok(roles);
  }

  @GetMapping("/admin/role/{id}")
  @PreAuthorize("hasPermission(null, 'ROLE', 'READ')")
  public ResponseEntity<?> getRoleById(@PathVariable(value = "id") Long id, Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }

    Optional<Role> role = roleRepository.findById(id);
    return role.map(value -> ResponseEntity.ok(new RoleDtoBuilder().withRole(value).build()))
        .orElse(null);
  }

  @PostMapping("/admin/role")
  @PreAuthorize("hasPermission(null, 'ROLE', 'WRITE')")
  public ResponseEntity<?> saveRole(@RequestBody RoleDto roleDto, Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }
    Role role = roleRepository.findByName(roleDto.getName());
    if (role != null)
      return new ResponseEntity<>("Error! Role already Exist with this name", HttpStatus.FOUND);

    role = new Role();
    role.setName(roleDto.getName());
    role.setTitle(roleDto.getTitle());

    roleRepository.save(role);
    return ResponseEntity.ok(role);
  }

  @PostMapping("/admin/role/{id}")
  @PreAuthorize("hasPermission(null, 'ROLE', 'ASSIGN')")
  public ResponseEntity<?> saveRolePermission(
      @PathVariable(value = "id") Long id,
      @RequestBody Set<String> permissions,
      Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }
    Optional<Role> role = roleRepository.findById(id);
    if (role.isEmpty())
      return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
    if (!permissions.isEmpty()) {
      role.get().setPermissions(getPermissions(permissions));
    }
    roleRepository.save(role.get());
    return ResponseEntity.ok(role);
  }

  private Set<Permission> getPermissions(Set<String> permissions) {
    Set<Permission> PermissionsList = new HashSet<>();
    for (String str : permissions) {
      Permission permission = permissionRepository.findByName(str);
      if (permission != null)
        PermissionsList.add(permission);
    }
    return PermissionsList;
  }
}
