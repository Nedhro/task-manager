package com.taskmanager.app.repository;

import com.taskmanager.app.core.entity.Task;
import com.taskmanager.app.core.enums.TaskStatus;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  Collection<Task> findAllByTaskStatus(TaskStatus taskStatus);

  Collection<Task> findAllByCreatedByOrderByCreatedDateDesc(Long userId);

  Collection<Task> findAllByProject_Id(Long projectId);
}
