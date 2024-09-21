package project.backend.business.post.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

  Long postId;

  @Builder
  public PostResponse(Long postId) {
    this.postId = postId;
  }
}
