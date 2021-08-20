package com.taskmanager.app.repository;

import com.taskmanager.app.core.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("from User where activeStatus =(:activeStatus)")
  List<User> list(@Param("activeStatus") Integer activeStatus);

  User findByIdAndActiveStatusTrue(Long id);

  User findByUserNameAndActiveStatusTrue(String userName);
}
