package project.backend.business.post.request;

import lombok.Builder;
import lombok.Getter;
import project.backend.entity.post.PostStatus;
import project.backend.entity.post.converter.PostStatusConverter;

@Getter
@Builder
public class PostDetailServiceRequest {
  private Long postId;
  private PostStatus status;

  public static PostDetailServiceRequest of(Long postId, String status) {
    PostStatusConverter converter = new PostStatusConverter();
    PostStatus postStatus = converter.convertToEntityAttribute(status);

    return PostDetailServiceRequest.builder()
                                   .postId(postId)
                                   .status(postStatus)
                                   .build();
  }

}
