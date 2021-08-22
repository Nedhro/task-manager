package com.taskmanager.app.service;

import com.taskmanager.app.core.dto.ProjectDto;
import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.entity.Project;

public interface ProjectService extends CommonService<Project> {

  Response saveProject(ProjectDto projectDto);

  Response getProjectById(Long projectId);

  Response getAllProjects();

  Response getAllProjectsByUser(Long id);

  Response updateProject(Long projectId, ProjectDto projectDto);

  Response deleteProject(Long projectId);
}
