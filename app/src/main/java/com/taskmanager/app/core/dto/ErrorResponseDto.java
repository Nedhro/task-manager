package com.taskmanager.app.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponseDto {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String field;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message;
}
