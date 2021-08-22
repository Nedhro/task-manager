package com.taskmanager.app.service;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.TaskDto;
import com.taskmanager.app.core.entity.Task;

public interface TaskService extends CommonService<Task> {

  Response saveTask(TaskDto taskDto);
}
