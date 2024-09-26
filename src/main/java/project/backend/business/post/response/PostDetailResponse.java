package project.backend.business.post.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;


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
  public PostDetailResponse(PostDetailDto postDetailDto) {
    this.title = postDetailDto.getTitle();
    this.content = postDetailDto.getContent();
    this.url = postDetailDto.getUrl();
    this.tagList = postDetailDto.getTagList();
    this.createdAt = postDetailDto.getCreatedAt();
    this.memoContent = postDetailDto.getMemoContent();
    this.memoCreatedAt = postDetailDto.getMemoCreatedAt();
  }
}
