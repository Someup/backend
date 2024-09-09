package project.backend.dao.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.dao.BaseEntity;
import project.backend.dao.user.entity.PostType;
import project.backend.dao.user.entity.User;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "post_title", nullable = false)
    private String title;

    @Column(name = "post_content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated
    @Column(name = "post_type", nullable = false)
    private PostType type;

    @Column(name = "post_url", nullable = false)
    private String url;

    @Column(name = "post_memo")
    private String memo;

    @Builder
    private Post(User user, String title, String content, PostType type, String url) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
        this.url = url;
    }

    public static Post createPost(User user, String title, String content, PostType type, String url) {
        return Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .type(type)
                .url(url)
                .build();
    }
}
