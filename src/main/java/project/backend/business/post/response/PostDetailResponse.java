package project.backend.business.post.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.request.PostDetailServiceRequest;

@Getter
public class PostDetailResponse {

  private final String title;
  private final String content;
  private final String url;
  private final List<String> tagList;
  private final String createdAt;
  private final String memoContent;
  private final String memoCreatedAt;

  @Builder
  public PostDetailResponse(PostDetailServiceRequest postDetailServiceRequest) {
    this.title = postDetailServiceRequest.getTitle();
    this.content = postDetailServiceRequest.getContent();
    this.url = postDetailServiceRequest.getUrl();
    this.tagList = postDetailServiceRequest.getTagList();
    this.createdAt = postDetailServiceRequest.getCreatedAt();
    this.memoContent = postDetailServiceRequest.getMemoContent();
    this.memoCreatedAt = postDetailServiceRequest.getMemoCreatedAt();
  }
}
