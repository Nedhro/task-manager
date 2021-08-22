package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.entity.Project;
import com.taskmanager.app.repository.ProjectRepository;
import com.taskmanager.app.service.ProjectService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends CommonServiceImpl<Project> implements ProjectService {

  private final ProjectRepository projectRepository;

  public ProjectServiceImpl(
      JpaRepository<Project, Long> repository, ProjectRepository projectRepository) {
    super(repository);
    this.projectRepository = projectRepository;
  }
}
