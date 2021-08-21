package com.taskmanager.app.service;

import java.util.List;
import org.springframework.data.domain.Page;

public interface CommonService<T> {

  List<T> findAll();

  Page<T> findAll(int page, int size);

  Page<T> findAllNotSort(int page, int size);

  T save(T obj);

  void delete(T obj);

  T findById(long id);
}
