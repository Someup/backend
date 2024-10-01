package project.backend.business.post.response;

import java.util.List;
import lombok.Getter;
import project.backend.business.post.response.dto.PostListDto;

@Getter
public class PostListResponse {

  private final List<PostListDto> postList;

  public PostListResponse(List<PostListDto> postList) {
    this.postList = postList;
  }
}
