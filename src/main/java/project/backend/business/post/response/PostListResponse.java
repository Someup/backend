package project.backend.business.post.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.response.dto.PostListDto;

@Getter
@Builder
public class PostListResponse {

  private final List<PostListDto> postList;

  public static PostListResponse from(List<PostListDto> postList) {
    return PostListResponse.builder()
                           .postList(postList)
                           .build();
  }
}
