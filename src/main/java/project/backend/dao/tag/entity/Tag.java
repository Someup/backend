package project.backend.dao.tag.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.dao.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTagList = new ArrayList<>();

    @Column
    private String name;

    @Builder
    private Tag(String name) {
        this.name = name;
    }

    public static Tag createTag(String name) {
        return Tag.builder().name(name).build();
    }
}
