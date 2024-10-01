package project.backend.entity.archive;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.entity.BaseEntity;
import project.backend.entity.user.User;

@Entity
@Getter
@Table(name = "archive")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Archive extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private User user;

  @Column(nullable = false)
  private String name;

  @Builder
  private Archive(User user, String name) {
    this.user = user;
    this.name = name;
  }

  public static Archive createArchive(User user, String name) {
    return Archive.builder()
                  .user(user)
                  .name(name)
                  .build();
  }

  public void updateName(String newName) {
    this.name = newName;
  }
}
