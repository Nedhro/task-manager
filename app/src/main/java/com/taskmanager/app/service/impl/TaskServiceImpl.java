package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.TaskDto;
import com.taskmanager.app.core.entity.Task;
import com.taskmanager.app.repository.TaskRepository;
import com.taskmanager.app.service.TaskService;
import com.taskmanager.app.util.ResponseBuilder;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl extends CommonServiceImpl<Task> implements TaskService {

  private final String root = "task";
  private final TaskRepository taskRepository;
  private final ModelMapper modelMapper;

  public TaskServiceImpl(
      JpaRepository<Task, Long> repository,
      TaskRepository taskRepository,
      ModelMapper modelMapper) {
    super(repository);
    this.taskRepository = taskRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  @Transactional
  public Response saveTask(TaskDto taskDto) {
    Task task;
    task = modelMapper.map(taskDto, Task.class);
    task = taskRepository.save(task);
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.CREATED,
        " The " + root + " (" + task.getId() + ") has been created successfully",
        task);
  }
}
