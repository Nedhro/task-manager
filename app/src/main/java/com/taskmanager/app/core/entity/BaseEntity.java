package com.taskmanager.app.core.entity;

import com.taskmanager.app.core.enums.Status;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable {

  private static final long serialVersionUID = 1L;

  @CreationTimestamp
  @Column(name = "created_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdDate;

  @CreationTimestamp
  @Column(name = "last_modified_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date lastModifiedDate;

  @Column(name = "modified_by")
  private Long modifiedBy;

  private Status status = Status.ACTIVE;

  public abstract ID getId();

  @PrePersist
  public void prePersist() {
    this.createdDate = new Date();
    this.lastModifiedDate = new Date();
    this.status = Status.ACTIVE;
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal()
          instanceof String)) {
        AuthUser user =
            (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.modifiedBy = user.getId();
      }
    }
  }

  @PreUpdate
  public void preUpdate() {
    this.lastModifiedDate = new Date();
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      if (!(SecurityContextHolder.getContext().getAuthentication().getPrincipal()
          instanceof String)) {
        AuthUser user =
            (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.modifiedBy = user.getId();
      }
    }
  }
}
