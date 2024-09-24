package project.backend.business.post.request;

import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.request.summary.SummaryOption;

@Getter
@Builder
public class CreatePostServiceRequest {

  String url;
  SummaryOption option;
}
