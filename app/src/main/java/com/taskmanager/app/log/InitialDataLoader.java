package com.taskmanager.app.log;

import com.taskmanager.app.core.model.Permission;
import com.taskmanager.app.core.model.Role;
import com.taskmanager.app.core.model.User;
import com.taskmanager.app.repository.PermissionRepository;
import com.taskmanager.app.repository.RoleRepository;
import com.taskmanager.app.repository.UserRepository;
import com.taskmanager.app.util.CustomUtil;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final PermissionRepository permissionRepository;

  boolean alreadySetup = false;

  @Value("${auth.adminuser}")
  private String adminUser;

  @Value("${auth.adminpassword}")
  private String adminPassword;

  @Value("${auth.commonuser}")
  private String commonUserName;

  @Value("${auth.commonuserpassword}")
  private String commonUserPassword;

  public InitialDataLoader(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      PermissionRepository permissionRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.permissionRepository = permissionRepository;
  }

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {

    if (alreadySetup)
      return;

    /* Common Setting Menu*/
    Permission settingMenuPermisssion = createPrivilegeIfNotFound("SETTING_MENU", "MENU");
    /* Role Privileges*/
    Permission roleAssignPermission = createPrivilegeIfNotFound("ROLE_ASSIGN", "ROLE");
    Permission roleReadPermission = createPrivilegeIfNotFound("ROLE_READ", "ROLE");
    Permission roleWritePermission = createPrivilegeIfNotFound("ROLE_WRITE", "ROLE");
    /* create ROLE_ADMIN*/
    Permission roleAdminPermission = createPrivilegeIfNotFound("ROLE_ADMIN", "ROLE_ADMIN");
    createRoleIfNotFound(
        "ROLE_ADMIN",
        Arrays.asList(
            roleAdminPermission,
            settingMenuPermisssion,
            roleAssignPermission,
            roleReadPermission,
            roleWritePermission));

    /* create ROLE_USER*/
    Permission roleUserPermission = createPrivilegeIfNotFound("ROLE_USER", "ROLE_USER");
    createRoleIfNotFound("ROLE_USER", Arrays.asList(roleUserPermission, settingMenuPermisssion));

    /* Admin User Creation*/
    Role adminRole = roleRepository.findByName("ROLE_ADMIN");
    Set<Role> adminRoles = new HashSet<>();
    adminRoles.add(adminRole);
    User userAdmin = userRepository.findByUsername(adminUser);
    if (userAdmin == null) {
      userAdmin = new User();
      userAdmin.setName("Admin User");
      userAdmin.setPassword(passwordEncoder.encode(adminPassword));
      userAdmin.setRoles(adminRoles);
      userAdmin.setEnabled(true);
      userAdmin.setUsername(adminUser);
      userAdmin.setLastPasswordResetDate(Calendar.getInstance().getTime());
      userRepository.save(userAdmin);
    }
    if (!userAdmin.getRoles().contains(adminRole)) {
      userAdmin.setRoles(adminRoles);
    }

    /* Common User Creation*/
    Role userRole = roleRepository.findByName("ROLE_USER");
    Set<Role> userRoles = new HashSet<>();
    userRoles.add(userRole);
    User commonUser = userRepository.findByUsername(commonUserName);
    if (commonUser == null) {
      commonUser = new User();
      commonUser.setName("Common User");
      commonUser.setPassword(passwordEncoder.encode(commonUserPassword));
      commonUser.setRoles(userRoles);
      commonUser.setEnabled(true);
      commonUser.setUsername(commonUserName);
      commonUser.setLastPasswordResetDate(Calendar.getInstance().getTime());
      userRepository.save(commonUser);
    }
    if (!commonUser.getRoles().contains(userRole)) {
      commonUser.setRoles(userRoles);
    }

    Iterator<Entry<String, Set<String>>> it = CustomUtil.permissions.entrySet().iterator();
    while (it.hasNext()) {
      @SuppressWarnings("rawtypes")
      Entry pair = it.next();
      @SuppressWarnings("unchecked")
      Set<String> features = (Set<String>) pair.getValue();
      for (String feature : features) {
        createPrivilegeIfNotFound(feature, pair.getKey().toString());
      }
    }

    alreadySetup = true;
  }

  /**
   * Custom Method for creating Role
   *
   * @param name       Role name
   * @param privileges Role privileges collection
   */
  @Transactional
  public Role createRoleIfNotFound(String name, Collection<Permission> privileges) {
    Role role = roleRepository.findByName(name);
    if (role == null) {
      role = new Role();
      role.setName(name);
      role.setPermissions(privileges);
      roleRepository.save(role);
    }
    return role;
  }

  /**
   * Custom Method for creating Privilege
   *
   * @param name  privilege name
   * @param group privilege group
   */
  @Transactional
  public Permission createPrivilegeIfNotFound(String name, String group) {
    Permission privilege = permissionRepository.findByName(name);
    if (privilege == null) {
      privilege = new Permission();
      privilege.setName(name);
      privilege.setGroup(group);
      permissionRepository.save(privilege);
    }
    return privilege;
  }
}
