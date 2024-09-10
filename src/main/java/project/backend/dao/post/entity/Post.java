package project.backend.dao.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.dao.BaseEntity;
import project.backend.dao.tag.entity.PostTag;
import project.backend.dao.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    @Convert(converter = PostTypeConverter.class)
    private PostType type;

    @Column(nullable = false, length = 2084)
    private String url;

    @Column
    private String memo;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTagList = new ArrayList<>();

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
