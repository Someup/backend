package project.backend.entity.archive;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.entity.BaseEntity;
import project.backend.entity.post.Post;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Post post;

  @Column(nullable = false)
  private String name;

  @Builder
  private Archive(User user, Post post, String name) {
    this.user = user;
    this.post = post;
    this.name = name;
  }

  public static Archive createArchive(User user, Post post, String name) {
    return Archive.builder()
                  .user(user)
                  .post(post)
                  .name(name)
                  .build();
  }

  public static Archive createArchiveWithoutPost(User user, String name) {
    return Archive.builder()
                  .user(user)
                  .name(name)
                  .build();
  }
}
