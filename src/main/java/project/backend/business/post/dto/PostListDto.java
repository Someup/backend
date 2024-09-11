package project.backend.business.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostListDto {
    private Long id;
    private String title;
    private String createdAt;
    private List<String> tagList;
}
