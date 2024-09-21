package project.backend.business.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailDto {

  private final String title;
  private final String content;
  private final String url;
  private final List<String> tagList;
  private final Long archiveId;
  private final String createdAt;
  private final String memoContent;
  private final String memoCreatedAt;
}
