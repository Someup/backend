package project.backend.dao.tag.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.dao.BaseEntity;
import project.backend.dao.post.entity.Post;

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
    @JoinColumn(nullable = false, insertable=false, updatable=false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, insertable=false, updatable=false)
    private Tag tag;
}
