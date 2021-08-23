package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.ProjectDto;
import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.entity.Project;
import com.taskmanager.app.repository.ProjectRepository;
import com.taskmanager.app.service.ProjectService;
import com.taskmanager.app.util.ResponseBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Log4j2
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

  @Override
  public Response getProjectById(Long projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.OK,
        root + " has been retrieved successfully.",
        project.isPresent() ? project.get() : "No Element");
  }

  @Override
  public Response getAllProjects() {
    List<Project> projectList = projectRepository.findAll();
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.OK,
        root + " list have been retrieved successfully. Total : " + projectList.size(),
        projectList);
  }

  @Override
  public Response getAllOwnProjects(Long userId) {
    Collection<Project> projectList = projectRepository
        .findAllByCreatedByOrderByCreatedDateDesc(userId);
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.OK,
        root + " list have been retrieved successfully. Total : " + projectList.size(),
        projectList);
  }

  @Override
  public Response getAllProjectsByUser(Long id) {
    Collection<Project> projectList = projectRepository
        .findAllByCreatedByOrderByCreatedDateDesc(id);
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.OK,
        root + " list have been retrieved successfully. Total : " + projectList.size(),
        projectList);
  }

  @Override
  public Response updateProject(Long projectId, ProjectDto projectDto) {
    Project project = projectRepository.getById(projectId);
    if (projectDto.getId() != null) {
      project.setId(projectDto.getId());
      project.setName(projectDto.getName());
      project = projectRepository.save(project);
      return ResponseBuilder.getSuccessResponse(
          HttpStatus.ACCEPTED, "The " + root + " has been updated Successfully", project);
    }
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, root + " doesn't exist with this id (" + projectId + ")");
  }

  @Override
  public Response deleteProject(Long projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    if (project.isPresent()) {
      try {
        projectRepository.deleteById(projectId);
        return ResponseBuilder.getSuccessResponse(
            HttpStatus.ACCEPTED, root + " has been deleted successfully ", project.get());
      } catch (DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return ResponseBuilder.getSuccessResponse(
            HttpStatus.NOT_ACCEPTABLE, root + " couldn't be deleted successfully ", project.get());
      }
    }
    return ResponseBuilder.getFailureResponse(
        HttpStatus.NOT_FOUND, root + " doesn't exist with this id (" + projectId + ")");
  }
}
