package project.backend.business.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostTagDto {
    private Long postId;
    private List<String> tagList;
}
