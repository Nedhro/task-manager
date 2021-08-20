package com.taskmanager.app.config;

import com.taskmanager.app.core.enums.ActiveStatus;
import com.taskmanager.app.core.model.Role;
import com.taskmanager.app.core.model.User;
import com.taskmanager.app.repository.RoleRepository;
import com.taskmanager.app.repository.UserRepository;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DbInit {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public DbInit(
      RoleRepository roleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostConstruct
  public void saveUser() {
    Role roleAdmin, roleUser;
    User userAdmin, userNormal;
    // ADMIN USER
    String adminUser = "admin";
    String adminRole = "ROLE_ADMIN";
    roleAdmin = roleRepository.findByName("ROLE_ADMIN");
    if (roleAdmin == null) {
      roleAdmin = new Role();
      roleAdmin.setName(adminRole);
      roleAdmin = roleRepository.save(roleAdmin);
    }
    userAdmin =
        userRepository.getByUserNameAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(), adminUser);
    if (userAdmin == null) {
      userAdmin = new User();
      userAdmin.setUserName(adminUser);
      userAdmin.setPassword(passwordEncoder.encode("Admin123"));
      userAdmin.setEmail("admin@gmail.com");
    }
    userAdmin.setRoles(Arrays.asList(roleAdmin));
    userRepository.save(userAdmin);

    // NORMAL USER
    String normalUser = "user";
    String normalUserRole = "ROLE_USER";
    roleUser = roleRepository.findByName("ROLE_USER");
    if (roleUser == null) {
      roleUser = new Role();
      roleUser.setName(normalUserRole);
      roleUser = roleRepository.save(roleUser);
    }
    userNormal =
        userRepository.getByUserNameAndActiveStatusTrue(ActiveStatus.ACTIVE.getValue(), normalUser);
    if (userNormal == null) {
      userNormal = new User();
      userNormal.setUserName(normalUser);
      userNormal.setPassword(passwordEncoder.encode("User123"));
      userNormal.setEmail("user@gmail.com");
    }
    userNormal.setRoles(Arrays.asList(roleUser));
    userRepository.save(userNormal);
  }
}
