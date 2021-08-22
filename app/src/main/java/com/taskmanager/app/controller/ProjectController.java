package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.ProjectDto;
import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.entity.AuthUser;
import com.taskmanager.app.service.ProjectService;
import com.taskmanager.app.util.CustomUtil;
import com.taskmanager.app.util.ResponseBuilder;
import com.taskmanager.app.util.RoleGroup;
import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {

  private static Set<String> features = new HashSet<>();

  static {
    features.add("PROJECT_READ");
    features.add("PROJECT_TRACK");
    features.add("PROJECT_WRITE");
    features.add("PROJECT_ASSIGN");
    features.add("PROJECT_PUBLISH");
    CustomUtil.permissions.put(RoleGroup.PROJECT.name(), features);
  }

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PostMapping("/project/save")
  public Response createProject(@RequestBody ProjectDto projectDto, Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null)
      return ResponseBuilder.getFailureResponse(
          HttpStatus.UNAUTHORIZED, " The user doesn't have the privileges");
    return projectService.saveProject(projectDto);
  }
}
