package com.taskmanager.app.controller;

import com.taskmanager.app.core.dto.Response;
import com.taskmanager.app.core.dto.RoleDto;
import com.taskmanager.app.service.RoleService;
import com.taskmanager.app.util.UrlConstraint;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstraint.RoleManagement.ROOT)
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public Response saveRole(@RequestBody RoleDto roleDto) {
        return roleService.save(roleDto);
    }

    @GetMapping
    public Response getAll() {
        return roleService.getAll();
    }

    @GetMapping(value = UrlConstraint.RoleManagement.GET)
    public Response getById(@PathVariable("roleId") Long roleId) {
        return roleService.getById(roleId);
    }

    @DeleteMapping(value = UrlConstraint.RoleManagement.DELETE)
    public Response delRole(@PathVariable("roleId") Long roleId) {
        return roleService.del(roleId);
    }

    @PutMapping(value = UrlConstraint.RoleManagement.PUT)
    public Response updateRole(@RequestBody RoleDto roleDto, @PathVariable("roleId") Long roleId) {
        return roleService.update(roleId, roleDto);
    }
}
