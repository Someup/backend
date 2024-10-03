package project.backend.business.post.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.response.dto.PostDetailDto;

@Getter
@Builder
public class PostDetailResponse {

  private final String title;
  private final String content;
  private final String url;
  private final List<String> tagList;
  private final String createdAt;
  private final String memoContent;
  private final String memoCreatedAt;

  public static PostDetailResponse from(PostDetailDto postDetailDto) {
    return PostDetailResponse.builder()
                             .title(postDetailDto.getTitle())
                             .content(postDetailDto.getContent())
                             .url(postDetailDto.getUrl())
                             .tagList(postDetailDto.getTagList())
                             .createdAt(postDetailDto.getCreatedAt())
                             .memoContent(postDetailDto.getMemoContent())
                             .memoCreatedAt(postDetailDto.getMemoCreatedAt())
                             .build();
  }
}
