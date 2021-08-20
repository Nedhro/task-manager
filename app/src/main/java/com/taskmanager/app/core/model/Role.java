package com.taskmanager.app.core.model;

import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Role extends BaseEntity {

  @NaturalId private String name;
}
