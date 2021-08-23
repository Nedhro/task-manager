package com.taskmanager.app.service;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.TaskDto;
import com.taskmanager.app.core.entity.Task;
import com.taskmanager.app.core.enums.TaskStatus;

public interface TaskService extends CommonService<Task> {

  Response saveTask(TaskDto taskDto);

  Response getAllTasks();

  Response getAllOwnTasks(Long userId);

  Response getAllTasksByUser(Long id);

  Response updateTask(Long taskId, TaskDto taskDto);

  Response getTaskById(Long taskId);

  Response deleteTask(Long taskId);

  Response getTaskByTaskStatus(TaskStatus taskStatus);

  Response getTaskByProjectId(Long projectId);

  Response getExpiredTaskList();

}
