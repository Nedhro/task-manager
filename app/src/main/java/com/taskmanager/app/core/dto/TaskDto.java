package com.taskmanager.app.core.dto;

import com.taskmanager.app.core.enums.TaskStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode()
public class TaskDto {

  private Long id;
  private String description;
  private TaskStatus taskStatus;
  private ProjectDto project;
  private Date dueDate;
}
