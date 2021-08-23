package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.TaskDto;
import com.taskmanager.app.core.entity.AuthUser;
import com.taskmanager.app.core.entity.User;
import com.taskmanager.app.core.enums.TaskStatus;
import com.taskmanager.app.service.TaskService;
import com.taskmanager.app.service.UserService;
import com.taskmanager.app.util.ResponseBuilder;
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
public class TaskController {

  private final UserService userService;
  private final TaskService taskService;

  public TaskController(UserService userService, TaskService taskService) {
    this.userService = userService;
    this.taskService = taskService;
  }

  @PostMapping("/tasks/save")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response createTask(@RequestBody TaskDto taskDto, Authentication auth) {
    AuthUser authenticatedUser = (AuthUser) auth.getPrincipal();
    if (authenticatedUser == null)
      return ResponseBuilder.getFailureResponse(
          HttpStatus.UNAUTHORIZED, " The user doesn't have the privileges");
    return taskService.saveTask(taskDto);
  }

  @GetMapping("/tasks/list")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Response getTaskList() {
    return taskService.getAllTasks();
  }

  @GetMapping("/tasks/own/{userId}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response getOwnTaskList(@PathVariable("userId") Long userId) {
    return taskService.getAllOwnTasks(userId);
  }

  @GetMapping("/tasks/listByUser/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Response getTaskListByUser(@PathVariable String username) {
    User user = userService.findByUserName(username);
    if (user != null)
      return taskService.getAllTasksByUser(user.getId());
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, " No user exists by this username");
  }

  @PutMapping("/tasks/update/{taskId}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response updateTask(@RequestBody TaskDto taskDto, @PathVariable("taskId") Long taskId) {
    return taskService.updateTask(taskId, taskDto);
  }

  @GetMapping("/tasks/{taskId}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response getTask(@PathVariable("taskId") Long taskId) {
    return taskService.getTaskById(taskId);
  }

  @GetMapping("/tasks/searchByProject/{projectId}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response getTaskByProject(@PathVariable("projectId") Long projectId) {
    return taskService.getTaskByProjectId(projectId);
  }

  @GetMapping("/tasks/expired/search")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response getExpiredTask() {
    return taskService.getExpiredTaskList();
  }

  @GetMapping("/tasks/searchByStatus/{taskStatus}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response getTask(@PathVariable("taskStatus") String taskStatus) {
    switch (taskStatus.toLowerCase()) {
      case "open":
        return taskService.getTaskByTaskStatus(TaskStatus.OPEN);
      case "inprogress":
        return taskService.getTaskByTaskStatus(TaskStatus.INPROGRESS);
      case "closed":
        return taskService.getTaskByTaskStatus(TaskStatus.CLOSED);
      default:
        return ResponseBuilder.getFailureResponse(HttpStatus.BAD_REQUEST, " Status is not valid");
    }
  }

  @DeleteMapping("/tasks/{taskId}")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
  public Response deleteTask(@PathVariable("taskId") Long taskId) {
    return taskService.deleteTask(taskId);
  }
}
