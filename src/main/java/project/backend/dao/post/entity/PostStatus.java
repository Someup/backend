package project.backend.dao.post.entity;

import lombok.Getter;

@Getter
public enum PostStatus {
    DRAFT("draft"), // 임시저장
    PUBLISHED("published"); // 게시 완료

    private String value;

    PostStatus(String value) {
        this.value = value;
    }
}
