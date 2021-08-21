package com.taskmanager.app.repository;

import com.taskmanager.app.core.model.User;
import com.taskmanager.app.core.model.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {

  UserVerification findByTokenAndVerified(String token, boolean verified);

  UserVerification findByUser(User user);
}
