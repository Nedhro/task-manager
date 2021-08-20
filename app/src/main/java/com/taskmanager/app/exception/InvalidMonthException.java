package com.taskmanager.app.exception;

public class InvalidMonthException extends Exception {
  public InvalidMonthException() {
    super();
  }

  public InvalidMonthException(String message) {
    super(message);
  }
}
