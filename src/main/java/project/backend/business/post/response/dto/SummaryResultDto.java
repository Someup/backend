package project.backend.business.post.response.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SummaryResultDto {

  private final String title;
  private final String content;
}
