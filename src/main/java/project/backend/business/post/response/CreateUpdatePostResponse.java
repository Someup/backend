package project.backend.business.post.response;

import lombok.Builder;
import lombok.Getter;
import project.backend.entity.post.Post;

@Getter
@Builder
public class CreateUpdatePostResponse {

  private final Long postId;

  public static CreateUpdatePostResponse from(Post post) {
    return CreateUpdatePostResponse.builder()
                                   .postId(post.getId())
                                   .build();
  }
}
