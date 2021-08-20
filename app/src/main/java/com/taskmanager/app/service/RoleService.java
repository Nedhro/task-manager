package com.taskmanager.app.service;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.RoleDto;

public interface RoleService {
  Response save(RoleDto roleDto);

  Response update(Long id, RoleDto roleDto);

  Response getById(Long id);

  Response del(Long id);

  Response getAll();
}
