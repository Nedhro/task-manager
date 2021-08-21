package com.taskmanager.app.repository;

import com.taskmanager.app.core.model.Permission;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

  Permission findByName(String name);

  Set<Permission> findByGroup(String group);
}
