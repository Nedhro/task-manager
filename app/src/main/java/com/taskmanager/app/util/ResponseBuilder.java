package com.taskmanager.app.util;

import com.taskmanager.app.core.dto.ErrorResponseDto;
import com.taskmanager.app.core.dto.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public final class ResponseBuilder {
  private ResponseBuilder() {}

  private static List<ErrorResponseDto> getCustomError(BindingResult result) {
    List<ErrorResponseDto> dtoList = new ArrayList<>();
    result
        .getFieldErrors()
        .forEach(
            fieldError -> {
              ErrorResponseDto errorResponseDto =
                  ErrorResponseDto.builder()
                      .field(fieldError.getField())
                      .message(fieldError.getDefaultMessage())
                      .build();
              dtoList.add(errorResponseDto);
            });
    return dtoList;
  }

  public static Response getFailureResponse(BindingResult result, String message) {
    return Response.builder()
        .message(message)
        .errors(getCustomError(result))
        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .timeStamp(new Date().getTime())
        .build();
  }

  public static Response getFailureResponse(HttpStatus status, String message) {
    return Response.builder()
        .message(message)
        .status(status.getReasonPhrase())
        .statusCode(status.value())
        .content(null)
        .timeStamp(new Date().getTime())
        .build();
  }

  public static Response getSuccessResponse(HttpStatus status, String message, Object content) {
    return Response.builder()
        .message(message)
        .status(status.getReasonPhrase())
        .statusCode(status.value())
        .content(content)
        .timeStamp(new Date().getTime())
        .build();
  }
}
