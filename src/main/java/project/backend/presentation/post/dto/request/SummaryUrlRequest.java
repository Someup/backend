package project.backend.presentation.post.dto.request;

import lombok.Getter;
import project.backend.business.post.dto.CreatePostDto;
import project.backend.business.post.dto.summary.SummaryOption;

@Getter
public class SummaryUrlRequest {

  String url;
  SummaryOptionRequest options;

  public CreatePostDto toServiceDto() {
    return CreatePostDto.builder()
                        .url(this.url)
                        .option(new SummaryOption(options.level, options.tone, options.language))
                        .build();
  }
}
