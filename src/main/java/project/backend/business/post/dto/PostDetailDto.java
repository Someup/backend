package project.backend.business.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostDetailDto {
    private String title;
    private String content;
    private String url;
    private List<String> tagList;
    private String createdAt;
    private String memoContent;
    private String memoCreatedAt;
}
