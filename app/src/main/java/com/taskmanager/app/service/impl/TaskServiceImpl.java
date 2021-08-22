package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.entity.Task;
import com.taskmanager.app.repository.TaskRepository;
import com.taskmanager.app.service.TaskService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends CommonServiceImpl<Task> implements TaskService {

  private final TaskRepository taskRepository;

  public TaskServiceImpl(JpaRepository<Task, Long> repository, TaskRepository taskRepository) {
    super(repository);
    this.taskRepository = taskRepository;
  }
}
