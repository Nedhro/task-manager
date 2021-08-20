package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.RoleDto;
import com.taskmanager.app.core.enums.ActiveStatus;
import com.taskmanager.app.core.model.Role;
import com.taskmanager.app.repository.RoleRepository;
import com.taskmanager.app.service.RoleService;
import com.taskmanager.app.util.ResponseBuilder;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
  private final ModelMapper modelMapper;
  private final RoleRepository roleRepository;
  private final String root = "Role";

  public RoleServiceImpl(ModelMapper modelMapper, RoleRepository roleRepository) {
    this.modelMapper = modelMapper;
    this.roleRepository = roleRepository;
  }

  @Override
  public Response save(RoleDto roleDto) {
    Role roleName = getRoleByName(roleDto);
    if (roleName != null) {
      return ResponseBuilder.getFailureResponse(
          HttpStatus.IM_USED, "This" + root + "Already Created");
    }
    Role role;
    role = modelMapper.map(roleDto, Role.class);
    role = roleRepository.save(role);
    return ResponseBuilder.getSuccessResponse(HttpStatus.CREATED, root + "Has been Created", role);
  }

  @Override
  public Response update(Long id, RoleDto roleDto) {
    Role roleName = getRoleByName(roleDto);
    if (roleName != null) {
      return ResponseBuilder.getFailureResponse(
          HttpStatus.IM_USED, "This" + root + "Already Created");
    }
    Role role = roleRepository.findByIdAndActiveStatusTrue(id);
    if (role != null) {
      modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      role = modelMapper.map(roleDto, Role.class);
      role = roleRepository.save(role);
      return ResponseBuilder.getSuccessResponse(
          HttpStatus.OK, root + " updated Successfully", role);
    }
    return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, root + " not found");
  }

  @Override
  public Response getById(Long id) {
    Role role = roleRepository.findByIdAndActiveStatusTrue(id);
    if (role != null) {
      modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      RoleDto roleDto = modelMapper.map(role, RoleDto.class);
      return ResponseBuilder.getSuccessResponse(
          HttpStatus.OK, root + " retrieved Successfully", roleDto);
    }
    return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, root + " not found");
  }

  @Override
  public Response del(Long id) {
    Role role = roleRepository.findByIdAndActiveStatusTrue(id);
    if (role != null) {
      role.setActiveStatus(ActiveStatus.DELETE.getValue());
      role = roleRepository.save(role);
      return ResponseBuilder.getSuccessResponse(HttpStatus.OK, root + "Delete SuccessFully", role);
    }
    return ResponseBuilder.getFailureResponse(HttpStatus.NOT_FOUND, root + " not found");
  }

  @Override
  public Response getAll() {
    List<Role> roleList = roleRepository.list(ActiveStatus.ACTIVE.getValue());
    List<RoleDto> roleDtoList = this.getRoles(roleList);
    if (roleDtoList.isEmpty()) {
      return ResponseBuilder.getSuccessResponse(HttpStatus.OK, "There is no role", null);
    }
    return ResponseBuilder.getSuccessResponse(
        HttpStatus.OK, root + "Data Retrieve Successfully", roleDtoList);
  }

  private List<RoleDto> getRoles(List<Role> roles) {
    List<RoleDto> roleDtoList = new ArrayList<>();
    roles.forEach(
        role -> {
          modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
          RoleDto roleDto = modelMapper.map(role, RoleDto.class);
          roleDtoList.add(roleDto);
        });
    return roleDtoList;
  }

  private Role getRoleByName(RoleDto roleDto) {
    return roleRepository.findByName(roleDto.getName());
  }
}
