package project.backend.business.post.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListServiceRequest {
    private Long id;
    private String title;
    private String createdAt;
    private List<String> tagList;
}
