package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.ProjectDto;
import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.entity.Project;
import com.taskmanager.app.repository.ProjectRepository;
import com.taskmanager.app.service.ProjectService;
import com.taskmanager.app.util.ResponseBuilder;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends CommonServiceImpl<Project> implements ProjectService {

  private final String root = "project";
  private final ProjectRepository projectRepository;
  private final ModelMapper modelMapper;

  public ProjectServiceImpl(
      JpaRepository<Project, Long> repository,
      ProjectRepository projectRepository,
      ModelMapper modelMapper) {
    super(repository);
    this.projectRepository = projectRepository;
    this.modelMapper = modelMapper;
  }

  @Transactional
  @Override
  public Response saveProject(ProjectDto projectDto) {
    Project project;
    project = modelMapper.map(projectDto, Project.class);
    project = projectRepository.save(project);
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.CREATED,
        " The " + root + " (" + project.getName() + ") has been created successfully",
        project);
  }
}
