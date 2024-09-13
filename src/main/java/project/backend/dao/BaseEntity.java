package project.backend.dao;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass // BaseEntity 를 상속한 엔티티들은 아래 필드들을 컬럼 인식
@EntityListeners(AuditingEntityListener.class) // 자동 값 매핑
public class BaseEntity {

  @Column
  private Boolean activated = true;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(insertable = false)
  private LocalDateTime updatedAt;
}
