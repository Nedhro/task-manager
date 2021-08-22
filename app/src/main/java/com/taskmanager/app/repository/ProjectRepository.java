package com.taskmanager.app.repository;

import com.taskmanager.app.core.entity.Project;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

  List<Project> findAllByModifiedBy(Long id);
}
