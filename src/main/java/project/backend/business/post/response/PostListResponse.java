package project.backend.business.post.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostListResponse {

  List<PostListDto> postList;

  @Builder
  public PostListResponse(List<PostListDto> postList) {
    this.postList = postList;
  }
}
