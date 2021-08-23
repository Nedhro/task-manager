package com.taskmanager.app.service.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public class CommonServiceImpl<T> {

  private JpaRepository<T, Long> repository;

  public CommonServiceImpl(JpaRepository<T, Long> repository) {
    this.repository = repository;
  }

  public List<T> findAll() {
    return repository.findAll();
  }

  public Page<T> findAll(int page, int size) {
    return repository.findAll(PageRequest.of(page, size, Sort.by("createdDate").descending()));
  }

  public Page<T> findAllNotSort(int page, int size) {
    return repository.findAll(PageRequest.of(page, size));
  }

  @Transactional
  public T save(T obj) {
    return repository.save(obj);
  }

  @Transactional
  public void delete(T obj) {
    repository.delete(obj);
  }

  public T findById(long id) {
    return repository.findById(id).orElse(null);
  }
}
