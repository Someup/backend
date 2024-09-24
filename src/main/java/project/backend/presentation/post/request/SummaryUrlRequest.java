package project.backend.presentation.post.request;

import lombok.Getter;
import project.backend.business.post.request.CreatePostServiceRequest;
import project.backend.business.post.request.summary.SummaryOption;

@Getter
public class SummaryUrlRequest {

  String url;
  SummaryOptionRequest options;

  public CreatePostServiceRequest toServiceRequest() {
    return CreatePostServiceRequest.builder()
                                   .url(this.url)
                                   .option(new SummaryOption(options.level, options.tone, options.language))
                                   .build();
  }
}
