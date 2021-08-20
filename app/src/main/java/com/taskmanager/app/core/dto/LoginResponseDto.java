package com.taskmanager.app.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class LoginResponseDto {
  @JsonInclude(JsonInclude.Include.ALWAYS)
  private String token;

  @JsonInclude(JsonInclude.Include.ALWAYS)
  private String username;
}
