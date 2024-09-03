package project.backend.user;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // BaseEntity 를 상속한 엔티티들은 아래 필드들을 컬럼 인식
@EntityListeners(AuditingEntityListener.class) // 자동 값 매핑
public class BaseEntity {

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDateTime;

  @UpdateTimestamp
  @Column(insertable = false)
  private LocalDateTime updatedDateTime;
}
