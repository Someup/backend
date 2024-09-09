package project.backend.dao.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.dao.BaseEntity;
import project.backend.dao.post.entity.Post;
import project.backend.dao.user.entity.User;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "category_name", nullable = false)
    private String name;

    @Builder
    private Category(User user, Post post, String name) {
        this.user = user;
        this.post = post;
        this.name = name;
    }

    public static Category createCategory(User user, Post post, String name) {
        return Category.builder()
                .user(user)
                .post(post)
                .name(name)
                .build();
    }
}
