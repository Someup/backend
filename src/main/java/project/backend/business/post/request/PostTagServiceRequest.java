package project.backend.business.post.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTagServiceRequest {

  private final Long postId;
  private final List<String> tagList;
}
