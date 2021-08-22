package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.TaskDto;
import com.taskmanager.app.core.entity.Task;
import com.taskmanager.app.core.enums.TaskStatus;
import com.taskmanager.app.repository.TaskRepository;
import com.taskmanager.app.service.TaskService;
import com.taskmanager.app.util.ResponseBuilder;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Log4j2
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

  public static Boolean isDueDateExpired(Date startDate, Date endDate) {
    return Math.abs(startDate.getTime()) < Math.abs(endDate.getTime());
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

  @Override
  public Response getAllTasks() {
    List<Task> taskList = taskRepository.findAll();
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.OK,
        root + " list have been retrieved successfully. Total : " + taskList.size(),
        taskList);
  }

  @Override
  public Response getAllTasksByUser(Long id) {
    List<Task> taskList = taskRepository.findAllByModifiedBy(id);
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.OK,
        root + " list have been retrieved successfully. Total : " + taskList.size(),
        taskList);
  }

  @Override
  public Response updateTask(Long taskId, TaskDto taskDto) {
    Task task = taskRepository.getById(taskId);
    if (taskDto.getId() != null) {
      if (task.getTaskStatus().equals(TaskStatus.CLOSED))
        return ResponseBuilder.getFailureResponse(
            HttpStatus.BAD_REQUEST, "Closed task cannot be edited.");

      if (!task.getProject().getId().equals(taskDto.getProject().getId()))
        return ResponseBuilder.getFailureResponse(
            HttpStatus.BAD_REQUEST, "You don't have the permission to change the project");

      task.setId(taskDto.getId());
      task.setDescription(taskDto.getDescription());
      task.setTaskStatus(taskDto.getTaskStatus());
      task.setDueDate(taskDto.getDueDate());
      task = taskRepository.save(task);

      return ResponseBuilder.getSuccessResponse(
          HttpStatus.ACCEPTED, "The " + root + " has been updated Successfully", task);
    }
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, root + " doesn't exist with this id (" + taskId + ")");
  }

  @Override
  public Response getTaskById(Long taskId) {
    Optional<Task> task = taskRepository.findById(taskId);
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.OK,
        root + " has been retrieved successfully.",
        task.isPresent() ? task.get() : "No Element");
  }

  @Override
  public Response deleteTask(Long taskId) {
    Optional<Task> task = taskRepository.findById(taskId);
    if (task.isPresent()) {
      try {
        taskRepository.deleteById(taskId);
        return ResponseBuilder.getSuccessResponse(
            HttpStatus.ACCEPTED, root + " has been deleted successfully ", task.get());
      } catch (DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return ResponseBuilder.getSuccessResponse(
            HttpStatus.NOT_ACCEPTABLE, root + " couldn't be deleted successfully ", task.get());
      }
    }
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, root + " doesn't exist with this id (" + taskId + ")");
  }

  @Override
  public Response getTaskByTaskStatus(TaskStatus taskStatus) {
    Collection<Task> taskList = taskRepository.findAllByTaskStatus(taskStatus);
    if (taskList.size() > 0)
      return ResponseBuilder.getSuccessResponse(
          HttpStatus.OK,
          root + " list have been retrieved successfully. Total : " + taskList.size(),
          taskList);
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, root + " doesn't exist with this status (" + taskStatus + ")");
  }

  @Override
  public Response getTaskByProjectId(Long projectId) {
    Collection<Task> taskList = taskRepository.findAllByProject_Id(projectId);
    if (taskList.size() > 0)
      return ResponseBuilder.getSuccessResponse(
          HttpStatus.OK,
          root + " list have been retrieved successfully. Total : " + taskList.size(),
          taskList);
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, root + " doesn't exist with this project (" + projectId + ")");
  }

  // task.getDueDate().before(new Date())
  @Override
  public Response getExpiredTaskList() {
    Collection<Task> tasks = taskRepository.findAll();
    Set<Task> set = new HashSet<>();
    for (Task entry : tasks) {
      Boolean expired = isDueDateExpired(entry.getDueDate(), new Date());
      if (expired)
        set.add(entry);
    }
    tasks = set;
    if (tasks.size() > 0)
      return ResponseBuilder.getSuccessResponse(
          HttpStatus.OK,
          "Expired " + root + " list have been retrieved successfully. Total : " + tasks.size(),
          tasks);
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, "No expired " + root + " is found");
  }
}
