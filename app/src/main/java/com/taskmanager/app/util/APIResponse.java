package com.taskmanager.app.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> {

  private int statusCode;
  private String message;
  private T body;

  public APIResponse(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }
}
