package project.backend.business.post.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTagRequest {
    private Long postId;
    private List<String> tagList;
}
