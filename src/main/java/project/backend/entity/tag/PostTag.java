package project.backend.entity.tag;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.entity.BaseEntity;
import project.backend.entity.post.Post;

@Entity
@Getter
@Table(name = "post_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Tag tag;

  @Builder
  private PostTag(Post post, Tag tag) {
    this.post = post;
    this.tag = tag;
  }

  public static PostTag create(Post post, Tag tag) {
    return PostTag.builder()
                  .post(post)
                  .tag(tag)
                  .build();
  }
}
