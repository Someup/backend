package project.backend.business.post.response;

import lombok.Getter;

@Getter
public class CreateUpdatePostResponse {

  private final Long postId;

  public CreateUpdatePostResponse(Long postId) {
    this.postId = postId;
  }
}
