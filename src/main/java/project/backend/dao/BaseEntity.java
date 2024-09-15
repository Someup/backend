package project.backend.dao;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @Column(columnDefinition = "boolean default true")
  private Boolean activated = true;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDateTime;

  @UpdateTimestamp
  @Column(insertable = false)
  private LocalDateTime updatedDateTime;
}
