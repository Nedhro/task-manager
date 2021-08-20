package com.taskmanager.app.core.model;

import javax.persistence.Entity;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

@Data
@Entity
public class Role extends BaseEntity {
  @NaturalId private String name;
}
