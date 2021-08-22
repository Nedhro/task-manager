package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.ProjectDto;
import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.entity.AuthUser;
import com.taskmanager.app.core.entity.User;
import com.taskmanager.app.service.ProjectService;
import com.taskmanager.app.service.UserService;
import com.taskmanager.app.util.CustomUtil;
import com.taskmanager.app.util.ResponseBuilder;
import com.taskmanager.app.util.RoleGroup;
import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {

  private static Set<String> features = new HashSet<>();

  /*These privileges can be used specifically*/
  static {
    features.add("PROJECT_READ");
    features.add("PROJECT_TRACK");
    features.add("PROJECT_WRITE");
    features.add("PROJECT_DELETE");
    CustomUtil.permissions.put(RoleGroup.PROJECT.name(), features);
  }

  private final ProjectService projectService;
  private final UserService userService;

  public ProjectController(ProjectService projectService, UserService userService) {
    this.projectService = projectService;
    this.userService = userService;
  }

  @PostMapping("/project/save")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response createProject(@RequestBody ProjectDto projectDto, Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null)
      return ResponseBuilder.getFailureResponse(
          HttpStatus.UNAUTHORIZED, " The user doesn't have the privileges");
    return projectService.saveProject(projectDto);
  }

  @GetMapping("/project/list")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response getProjectList() {
    return projectService.getAllProjects();
  }

  @GetMapping("/project/listByUser/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Response getProjectListByUser(@PathVariable String username) {
    User user = userService.findByUserName(username);
    if (user != null)
      return projectService.getAllProjectsByUser(user.getId());
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, " No user exists by this username");
  }

  @PutMapping("/project/update/{projectId}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response updateProject(@RequestBody ProjectDto projectDto,
      @PathVariable("projectId") Long projectId) {
    return projectService.updateProject(projectId, projectDto);
  }

  @GetMapping("/project/{projectId}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response getProject(@PathVariable("projectId") Long projectId) {
    return projectService.getProjectById(projectId);
  }

  @DeleteMapping("/project/{projectId}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response deleteProject(@PathVariable("projectId") Long projectId) {
    return projectService.deleteProject(projectId);
  }

}
