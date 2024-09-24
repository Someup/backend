package project.backend.business.post.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailServiceRequest {
    private String title;
    private String content;
    private String url;
    private List<String> tagList;
    private int archiveId;
    private String createdAt;
    private String memoContent;
    private String memoCreatedAt;
}
