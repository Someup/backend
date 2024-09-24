package project.backend.presentation.post.request;

import java.util.List;
import lombok.Getter;
import project.backend.business.post.request.PostDetailServiceRequest;

@Getter
public class UpdatePostRequest {
    private String title;
    private String content;
    private List<String> tagList;
    private int archiveId;
    private String memo;

    public PostDetailServiceRequest toServiceRequest() {
        return PostDetailServiceRequest.builder()
                                       .title(title)
                                       .content(content)
                                       .tagList(tagList)
                                       .archiveId(archiveId)
                                       .memoContent(memo)
                                       .build();
    }
}
