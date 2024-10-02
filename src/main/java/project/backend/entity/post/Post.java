package project.backend.entity.post;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.backend.entity.BaseEntity;
import project.backend.entity.archive.Archive;
import project.backend.entity.post.converter.PostStatusConverter;
import project.backend.entity.post.converter.PostTypeConverter;
import project.backend.entity.tag.PostTag;
import project.backend.entity.user.User;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private User user;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @Column(nullable = false)
  @Convert(converter = PostTypeConverter.class)
  private PostType type;

  @Column(nullable = false)
  @Convert(converter = PostStatusConverter.class)
  private PostStatus status;

  @Column(nullable = false, length = 2084)
  private String url;

  @Column
  private String memo;

  @Column
  private LocalDateTime memoCreatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private Archive archive;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostTag> postTagList = new ArrayList<>();

  @Builder
  private Post(User user, String title, String content, PostStatus status, PostType type,
      String url, boolean activated) {
    this.user = user;
    this.title = title;
    this.content = content;
    this.type = type;
    this.status = status;
    this.url = url;
    this.setActivated(activated);
  }

  public static Post createPost(User user, String title, String content, PostStatus status,
      String url) {
    Post post = Post.builder()
                    .title(title)
                    .content(content)
                    .type(PostType.PRIVATE)
                    .status(status)
                    .url(url)
                    .activated(true)
                    .build();

    if (user != null) {
      post.setUser(user);
    }

    return post;
  }
}
