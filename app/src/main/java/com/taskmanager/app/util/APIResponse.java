package com.taskmanager.app.util;

public class APIResponse<T> {

  private int statusCode;
  private String message;
  private T body;

  public APIResponse() {
  }

  public APIResponse(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  public APIResponse(T body, int statusCode, String message) {
    this.body = body;
    this.statusCode = statusCode;
    this.message = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getBody() {
    return body;
  }

  public void setBody(T body) {
    this.body = body;
  }
}
