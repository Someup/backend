package project.backend.business.post.request;

import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.request.summary.SummaryOption;

@Getter
@Builder
public class CreatePostServiceRequest {

  private final String url;
  private final SummaryOption option;
}
