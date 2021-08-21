package com.taskmanager.app.service.impl;

import com.taskmanager.app.core.dto.UserDto;
import com.taskmanager.app.core.model.User;
import com.taskmanager.app.core.model.UserVerification;
import com.taskmanager.app.repository.UserRepository;
import com.taskmanager.app.repository.UserVerificationRepository;
import com.taskmanager.app.service.UserService;
import java.util.Calendar;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CommonServiceImpl<User> implements UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserVerificationRepository userVerificationRepository;

  public UserServiceImpl(JpaRepository<User, Long> repository) {
    super(repository);
    this.userRepository = (UserRepository) repository;
  }

  @Override
  public User findByUserName(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  @Transactional
  public User registration(UserDto userDto, String password) throws Exception {

    if (this.findByUserName(userDto.getUsername()) != null) {
      throw new Exception("User already exist with this username " + userDto.getUsername());
    }

    User user = new User();
    user.setName(userDto.getName().trim());
    user.setPassword(passwordEncoder.encode(password));
    user.setUsername(userDto.getUsername().trim());
    user.setEnabled(false);
    user.setLastPasswordResetDate(Calendar.getInstance().getTime());
    return userRepository.save(user);
  }

  @Override
  public User update(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public void generateVerificationToken(User user, String token) {

    UserVerification verifyToken = new UserVerification(token, user);
    userVerificationRepository.save(verifyToken);
  }

  @Override
  public UserVerification getUserByverificationToken(String token) {
    return userVerificationRepository.findByTokenAndVerified(token, false);
  }
}
