package project.backend.dao.post.entity;

import lombok.Getter;

@Getter
public enum PostType {
    PUBLIC("public"),
    PRIVATE("private");

    private String value;

    PostType(String value) {
        this.value = value;
    }
}

