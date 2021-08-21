package com.taskmanager.app.core.enums;

public enum TaskStatus {
  OPEN("open"),
  INPROGRESS("in progress"),
  CLOSED("closed");

  private final String value;

  TaskStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
