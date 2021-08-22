package com.taskmanager.app.service;

import com.taskmanager.app.core.dto.ProjectDto;
import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.entity.Project;

public interface ProjectService extends CommonService<Project> {

  Response saveProject(ProjectDto projectDto);
}
