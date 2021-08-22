package com.taskmanager.app.core.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class TaskDto {

  private Long id;
  @NotNull
  private String description;
  @NotNull
  private String taskStatus;
  @NotNull
  private ProjectDto project;
  private Date dueDate;
}
