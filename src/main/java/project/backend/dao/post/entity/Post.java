package project.backend.dao.post.entity;

import jakarta.persistence.*;
import lombok.*;
import project.backend.dao.BaseEntity;
import project.backend.dao.post.converter.PostStatusConverter;
import project.backend.dao.post.converter.PostTypeConverter;
import project.backend.dao.tag.entity.PostTag;
import project.backend.dao.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTagList = new ArrayList<>();

    @Builder
    private Post(User user, String title, String content, PostStatus status, PostType type, String url, boolean activated) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
        this.url = url;
        this.setActivated(activated);
    }

    public static Post createPost(String title, String content, PostStatus status, String url) {
        return Post.builder()
                .title(title)
                .content(content)
                .type(PostType.PRIVATE)
                .status(status)
                .url(url)
                .activated(true)
                .build();
    }

    public static Post createPost(User user, String title, String content, PostStatus status, String url) {
        return Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .type(PostType.PRIVATE)
                .status(status)
                .url(url)
                .activated(true)
                .build();
    }

}
