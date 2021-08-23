package com.taskmanager.app.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.taskmanager.app.core.enums.TaskStatus;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "task")
public class Task extends BaseEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  private String description;

  @Basic(optional = false)
  @NotNull
  @Enumerated(EnumType.STRING)
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private TaskStatus taskStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(
      name = "task_project",
      joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "project_id", referencedColumnName = "id")})
  private Project project;

  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date dueDate;

  public Task(String description, TaskStatus taskStatus, Project project) {
    this.description = description;
    this.taskStatus = taskStatus;
    this.project = project;
  }
}
