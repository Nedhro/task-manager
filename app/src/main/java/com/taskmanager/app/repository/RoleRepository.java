package com.taskmanager.app.repository;

import com.taskmanager.app.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByName(String username);

  @SuppressWarnings("unchecked")
  Role save(Role role);
}
