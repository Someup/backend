package project.backend.business.post.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.request.PostListServiceRequest;

@Getter
public class PostListResponse {

  List<PostListServiceRequest> postList;

  @Builder
  public PostListResponse(List<PostListServiceRequest> postList) {
    this.postList = postList;
  }
}
