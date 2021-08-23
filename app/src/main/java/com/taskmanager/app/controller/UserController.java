package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.AuthUserDto;
import com.taskmanager.app.core.dto.AuthorityDto;
import com.taskmanager.app.core.dto.UserDto;
import com.taskmanager.app.core.dto.builder.UserDtoBuilder;
import com.taskmanager.app.core.entity.AuthUser;
import com.taskmanager.app.core.entity.Role;
import com.taskmanager.app.core.entity.User;
import com.taskmanager.app.core.entity.UserVerification;
import com.taskmanager.app.repository.RoleRepository;
import com.taskmanager.app.repository.UserVerificationRepository;
import com.taskmanager.app.service.UserService;
import com.taskmanager.app.util.APIResponse;
import com.taskmanager.app.util.CustomUtil;
import com.taskmanager.app.util.RoleGroup;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private static Set<String> features = new HashSet<>();

  static {
    features.add("USER_READ");
    features.add("USER_WRITE");
    features.add("USER_ASSIGN");
    features.add("PASSWORD_WRITE");
    CustomUtil.permissions.put(RoleGroup.ADMIN.name(), features);
  }

  private final UserService userService;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserVerificationRepository userVerificationRepository;

  public UserController(
      UserService userService,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      UserVerificationRepository userVerificationRepository) {
    this.userService = userService;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.userVerificationRepository = userVerificationRepository;
  }

  @GetMapping("/admin/users/{page}/{size}")
  @PreAuthorize("hasPermission(null, 'USER', 'READ')")
  public ResponseEntity<?> getAllusers(
      Authentication auth,
      @PathVariable(value = "page") int page,
      @PathVariable(value = "size") int size) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }
    Page<User> users = userService.findAll(page, size);
    return ResponseEntity.ok(users.map(this::convertToUserDto));
  }

  @GetMapping(value = "/public/verify/{token}")
  public ResponseEntity<?> verifyUserVerificationToken(
      @PathVariable(value = "token") String token) {
    try {
      UserVerification verificationToken = userService.getUserByverificationToken(token);
      if (verificationToken == null) {
        return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
      }

      User user = verificationToken.getUser();
      user.setEnabled(true);
      userService.save(user);
      verificationToken.setVerified(true);
      userVerificationRepository.save(verificationToken);
      return ResponseEntity.ok(HttpStatus.OK);
    } catch (Exception ex) {
      return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PostMapping("/admin/user/{id}")
  @PreAuthorize("hasPermission(null, 'USER', 'ASSIGN')")
  public ResponseEntity<?> saveUserRole(
      @PathVariable(value = "id") Long id, @RequestBody List<String> roles, Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null) {
      return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
    }
    User user = userService.findById(id);
    if (user == null)
      return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
    if (!roles.isEmpty()) {
      user.setRoles(getRoles(roles));
    }
    userService.save(user);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/admin/updatepassword")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<?> savePassword(
      @RequestBody Map<String, String> password, Authentication auth) {
    try {
      AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
      if (authenticatedUser == null) {
        return new ResponseEntity<HttpStatus>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
      }
      if (password.get("password") == null || password.get("oldPassword") == null) {
        return ResponseEntity.ok(
            new APIResponse<User>(417, "Password or Old Password can't be null"));
      }
      if (!passwordEncoder.matches(password.get("oldPassword"), authenticatedUser.getPassword())) {
        throw new NotFoundException("Password mismatch");
      }
      User user = userService.findByUserName(authenticatedUser.getUsername());
      if (user == null) {
        throw new NotFoundException("User not found");
      }
      user.setPassword(passwordEncoder.encode(password.get("password")));
      userService.save(user);
      return ResponseEntity.ok(new APIResponse<User>(200, "Password updated successfully"));
    } catch (NotFoundException e) {
      return ResponseEntity.ok(new APIResponse<User>(400, e.getMessage()));
    } catch (Exception e) {
      return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("/admin/user/status/{id}")
  @PreAuthorize("hasPermission(null, 'USER', 'ASSIGN')")
  public ResponseEntity<?> updateUserStatus(
      @PathVariable(value = "id") Long id,
      @RequestParam("action") String action,
      Authentication auth) {

    try {
      AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
      if (authenticatedUser == null) {
        return new ResponseEntity<HttpStatus>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
      }

      User user = userService.findById(id);
      if (user == null) {
        return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
      }

      if (action.equalsIgnoreCase("true")) {
        user.setEnabled(true);
      } else if (action.equalsIgnoreCase("false")) {
        user.setEnabled(false);
      }

      userService.save(user);
      return ResponseEntity.ok(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("/admin/authuser")
  public AuthUserDto getAuthUser(Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null)
      return null;
    AuthUserDto user =
        new AuthUserDto(
            authenticatedUser.getId(),
            authenticatedUser.getUsername(),
            authenticatedUser.getName(),
            authenticatedUser.isEnabled());
    user.setPassword(authenticatedUser.getPassword());
    user.setAuthorities(
        authenticatedUser.getAuthorities().stream()
            .map(mapper -> new AuthorityDto(mapper.getAuthority()))
            .collect(Collectors.toList()));
    return user;
  }

  @PostMapping("/public/resetpassword")
  public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {

    try {
      if (requestBody.get("token") == null) {
        throw new NotFoundException("Error! token not found");
      }

      if (requestBody.get("password") == null) {
        throw new NotFoundException("Error! password not found");
      }

      UserVerification verificationToken =
          userService.getUserByverificationToken(requestBody.get("token"));

      if (verificationToken == null) {
        throw new NotFoundException("Error! invalid token");
      }

      User user = verificationToken.getUser();

      if (user == null) {
        throw new NotFoundException("Error! user not found");
      }

      user.setPassword(passwordEncoder.encode(requestBody.get("password")));
      userService.save(user);
      verificationToken.setVerified(true);
      userVerificationRepository.save(verificationToken);
      return ResponseEntity.ok(
          new APIResponse<User>(200, "Success! your password has been reset successfully."));
    } catch (NotFoundException nfe) {
      return ResponseEntity.ok(new APIResponse<User>(404, nfe.getMessage()));
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  @GetMapping("/public/username")
  public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) {

    try {
      User user = userService.findByUserName(email);
      if (user != null) {
        return new ResponseEntity<>(user, HttpStatus.OK);
      }

      return ResponseEntity.ok(HttpStatus.OK);

    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
  }

  private UserDto convertToUserDto(final User user) {
    return new UserDtoBuilder().withUser(user).build();
  }

  private Set<Role> getRoles(List<String> roles) {
    Set<Role> rolesList = new HashSet<>();
    for (String str : roles) {
      Role role = roleRepository.findByName(str);
      if (role != null)
        rolesList.add(role);
    }
    return rolesList;
  }
}
