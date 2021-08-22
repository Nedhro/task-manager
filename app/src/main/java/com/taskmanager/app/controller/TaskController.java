package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.TaskDto;
import com.taskmanager.app.core.entity.AuthUser;
import com.taskmanager.app.service.TaskService;
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
public class TaskController {

  private static Set<String> features = new HashSet<>();
  /*These privileges can be used specifically*/
  static {
    features.add("TASK_READ");
    features.add("TASK_TRACK");
    features.add("TASK_WRITE");
    features.add("TASK_DELETE");
    CustomUtil.permissions.put(RoleGroup.TASK.name(), features);
  }

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping("/task/save")
  public Response createTask(@RequestBody TaskDto taskDto, Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null)
      return ResponseBuilder.getFailureResponse(
          HttpStatus.UNAUTHORIZED, " The user doesn't have the privileges");
    return taskService.saveTask(taskDto);
  }
}
